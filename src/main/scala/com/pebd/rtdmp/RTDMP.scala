package com.pebd.rtdmp

import java.nio.file.{Paths, Files}

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}


/**
  * Created by david on 10/12/15.
  */
object RTDMP {

  var BROKER = "localhost:9092"
  var TOPIC = "TOPICO.TEST.PUBLICADOR"
  var RULES_FILE = "/home/david/PEBD/workspace/RTDMP/src/main/resources/ReglasTest.xlsx"

  def main(args: Array[String]): Unit = {

    if (args.length != 3){
      print("Usage: RTDMP <KafkaBrokerHost:KafkaBrokerPort> <KafkaTopic> <RulesFilePath>")
      print("Will execute default params")
    }else{
      BROKER = args(0)
      TOPIC = args(1)
      RULES_FILE = args(2)
    }


    if (!Files.exists(Paths.get(RULES_FILE))) {
      println("Error: Rules file [" + RULES_FILE + "] is missing")
      System.exit(1)
    }

    // para ejecucion local
    //val sparkConf = new SparkConf().setMaster("local[8]").setAppName("RTDMP")

    val sparkConf = new SparkConf().setAppName("RTDMP")
    val ssc = new StreamingContext(sparkConf, Seconds(3))
    ssc.sparkContext.setLogLevel("ERROR")

    val kafkaParams = Map[String, String]("metadata.broker.list" -> BROKER, "auto.offset.reset"->"largest")
    val topics = Map[String,Integer](TOPIC->8)
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc,kafkaParams,Set(TOPIC))

    val rulesExecutor = new RulesExecutor(RULES_FILE)

    messages.foreachRDD(rdd => {

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
