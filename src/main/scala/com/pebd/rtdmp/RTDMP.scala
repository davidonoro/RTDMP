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

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[3]").setAppName("RTDMP")
    val ssc = new StreamingContext(sparkConf, Seconds(5));

    val kafkaParams = Map[String, String]("metadata.broker.list" -> BROKER)
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc,kafkaParams,Set(TOPIC))

    messages.foreachRDD(rdd => {
      val rulesExecutor = new RulesExecutor("/home/david/PEBD/data/ReglasTest.xlsx")

      val navList = rdd.mapPartitions(listMsg=>{
        val navs = NavigationDataExtractor.parseMultipleData(listMsg)
        val evalNavs = rulesExecutor.evaluarMultipleReglas(navs)
        evalNavs
      })
      val jedisManager = new JedisManager
      val navFilteredList = navList.filter(nav=> nav.getUser != null && !nav.getCategorias.isEmpty)
      jedisManager.insertMultipleData(navFilteredList.collect())
    })

    ssc.start()
    ssc.awaitTermination()
  }
}
