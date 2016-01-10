package com.pebd.rtdmp

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}


/**
  * Created by david on 10/12/15.
  */
object RTDMP {

  final var BROKER = "192.168.1.138:9092"
  //final var BROKER = "localhost:9092"
  final var TOPIC = "TOPICO.TEST.PUBLICADOR"

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[8]").setAppName("RTDMP")
    val ssc = new StreamingContext(sparkConf, Seconds(5));


    val kafkaParams = Map[String, String]("metadata.broker.list" -> BROKER, "auto.offset.reset"->"largest")
    val topics = Map[String,Integer](TOPIC->8)
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc,kafkaParams,Set(TOPIC))


    //val heavyHitters = collection.mutable.Map[String,Long]()

    messages.foreachRDD(rdd => {
      val rulesExecutor = new RulesExecutor("/home/david/PEBD/data/ReglasTest.xlsx")

      println("Recibidos "+rdd.count()+" mensajes")

      val navList = rdd.mapPartitions(listMsg=>{

        val navs = NavigationDataExtractor.parseMultipleData(listMsg)
        val evalNavs = rulesExecutor.evaluarMultipleReglas(navs)
        val navFilteredList = evalNavs.filter(nav=> nav.getUser != null && !nav.getCategorias.isEmpty  && nav.getPais != null)
        navFilteredList
      })

      val jedisManager = new JedisManager
      navList.foreach(nav=> {
        jedisManager.insertData(nav)
      })

      println("Batch insertando "+navList.count()+" registros en Redis")

    })

    ssc.start()
    ssc.awaitTermination()
  }
}
