package com.pebd.rtdmp

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

/**
  * Created by david on 17/12/15.
  */
object ReadFile{

  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setMaster("local[6]").setAppName("READFILE")
    val sc = new SparkContext(sparkConf)
    val file = sc.textFile("/home/david/Descargas/raw_pageviews_day_20151203.json")

    val docsRDD = file.map{line=> NavigationDataExtractor.parseData(line)}
    val sumOrderRDD = docsRDD.map(doc => (doc.getDominio,1)).
      reduceByKey(_+_).
      filter(x=>{x._1!="null"}).
      sortBy(-_._2)
    sumOrderRDD.take(1000).foreach(println)
  }

}
