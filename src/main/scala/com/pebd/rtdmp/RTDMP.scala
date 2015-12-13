package com.pebd.rtdmp

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}


/**
  * Created by david on 10/12/15.
  */
object RTDMP {

  final var BROKER = "localhost:9092"
  final var TOPIC = "TOPICO.TEST.PUBLICADOR"

  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("RTDMP")
    val ssc = new StreamingContext(sparkConf, Seconds(5))


    val kafkaParams = Map[String, String]("metadata.broker.list" -> BROKER)

    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc,kafkaParams,Set(TOPIC))
    val rulesExecutor = new RulesExecutor("/home/david/PEBD/data/ReglasTest.xlsx")
    val jedisMngr = new JedisManager

    messages.foreachRDD(rdd => {
      rdd.map(msg => msg._2).foreach(text =>{
        val navData = NavigationDataExtractor.parseData(text)
        if (navData.getUser != null && navData.getUrl !=null){
          println("User: "+navData.getUser)
          println("Url: "+navData.getUrl)
          println("Fecha: "+navData.getFecha)
          println("Pais: "+navData.getPais)
          println("Categorias Antes: "+navData.printCategorias)
          val evalData = rulesExecutor.evaluarReglas(navData)
          println("Categorias Despues: "+evalData.printCategorias)
          jedisMngr.insertData(evalData)
        }
      })
    })

    ssc.start()
    ssc.awaitTermination()
  }
}
