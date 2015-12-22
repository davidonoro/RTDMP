package com.pebd.rtdmp

import java.io.File

import org.apache.spark.{SparkContext, SparkConf}
import scala.collection.JavaConversions._

/**
  * Created by david on 20/12/15.
  */
object BathProcess {

  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setMaster("local[3]").setAppName("BATCHPROCESS")
    val sc = new SparkContext(sparkConf)

    val rulesExecutor = new RulesExecutor("/home/david/PEBD/data/ReglasTest.xlsx")


    val dir = new File("/home/david/Descargas/data")

    val files = dir.listFiles().filter(f=>f.isFile).map(f=>{
      val file = sc.textFile(f.getAbsolutePath)
      println("Reading file "+f.getAbsolutePath)

      val docsInFile = file.map{line=>NavigationDataExtractor.parseData(line)}
      val filterLines = docsInFile.filter(x=> x.getUser!=null && x.getDominio!=null)
      val evalMsgs = filterLines.map(navData=>rulesExecutor.evaluarReglas(navData))
      val filterMsgs = evalMsgs.filter(x=> !x.getCategorias.isEmpty)
      //println("Num "+filterMsgs.count())

      filterMsgs.map(nav=>(nav.getUser,nav)).aggregateByKey(new Navigation())(
        (acc,curr)=>{
          acc.setUser(curr.getUser)
          val cats = curr.getCategorias
          for (cat <- cats){
            acc.addCategoria(cat)
          }
          acc
        },
        (left,rigth)=>{
          left.setUser(rigth.getUser)
          val cats = rigth.getCategorias
          for (cat <- cats){
            left.addCategoria(cat)
          }
          left
        }
      ).foreach(x=>println("Usuario="+x._2.getUser+" Cats="+x._2.printCategorias()))
    })
  }
}
