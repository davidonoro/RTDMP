package com.pebd.rtdmp

import org.joda.time.format.DateTimeFormat
import org.json4s._
import org.json4s.jackson.JsonMethods._

/**
  * Created by david on 11/12/15.
  */
object NavigationDataExtractor {

  implicit val formats = DefaultFormats

  /**
    * Case class que define el documento recibido
    * @param UUIDC
    * @param _F
    * @param _GPID
    * @param _HTM
    * @param _TW
    * @param _gcmID
    * @param _id
    * @param _lp
    * @param browser
    * @param dominio
    * @param dominio_md5
    * @param dominio_origen
    * @param extension_version
    * @param fecha
    * @param fecha_insercion
    * @param fecha_local
    * @param frame_id
    * @param gaclient
    * @param gcmid_md5
    * @param id_machine
    * @param id_session
    * @param idextension
    * @param idpageview
    * @param ip
    * @param pais_iso2
    * @param parentframe_id
    * @param partner
    * @param requestid
    * @param source
    * @param tabid
    * @param timestamp_browser
    * @param top_id
    * @param url
    * @param url_md5
    * @param url_referer
    * @param urlorigen
    * @param user_agent
    * @param ventana
    */
  case class event(UUIDC:String,
                   _F:String,
                   _GPID:String,
                   _HTM:String,
                   _TW:String,
                   _gcmID:String,
                   _id:Map[String, String],
                   _lp:String,
                   browser:String,
                   dominio:String,
                   dominio_md5:String,
                   dominio_origen:String,
                   extension_version:String,
                   fecha:String,
                   fecha_insercion:String,
                   fecha_local:String,
                   frame_id:String,
                   gaclient:String,
                   gcmid_md5:String,
                   id_machine:String,
                   id_session:String,
                   idextension:String,
                   idpageview:String,
                   ip:String,
                   pais_iso2:String,
                   parentframe_id:String,
                   partner:String,
                   requestid:String,
                   source:String,
                   tabid:String,
                   timestamp_browser:String,
                   top_id:String,
                   url:String,
                   url_md5:String,
                   url_referer:String,
                   urlorigen:String,
                   user_agent:String,
                   ventana:String
                  )


  /**
    * Parsea un docuemnto y devuelve un objeto Navigation
    * @param json
    * @return
    */
  def parseData(json:String): Navigation = {
    val parsedJson = parse(json)
    try{
      val doc = parsedJson.extract[event]
      val data = new Navigation(doc.UUIDC,doc.pais_iso2,timeToStr(doc.fecha),doc.dominio,doc.url)
      data
    } catch{
        case mpe:MappingException => {
         // println("Error parseando doc: " + mpe.msg)
          val data = new Navigation()
          data
        }
    }
  }

  /**
    * Parsea un iterador de documentos en un iterador de Navigation
    * @param jsonList
    * @return
    */
  def parseMultipleData(jsonList: Iterator[(String,String)]): Iterator[Navigation]={
    val navigations = jsonList.map(kfkMsg=>{
      val parsedJson = parse(kfkMsg._2)
      try{
        val doc = parsedJson.extract[event]
        val data = new Navigation(doc.UUIDC,doc.pais_iso2,timeToStr(doc.fecha),doc.dominio,doc.url)
        data
      } catch{
        case mpe:MappingException => {
          val data = new Navigation()
          data
        }
      }
    })
    navigations
  }

  /**
    * Helper que cambia el formato de fecha
    * @param epochMillis
    * @return
    */
  def timeToStr(epochMillis: String): String =  {
    DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss").print(java.lang.Long.valueOf(epochMillis)*1000)
  }

}
