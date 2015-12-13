package com.pebd.rtdmp

import org.joda.time.format.DateTimeFormat
import org.json4s._
import org.json4s.jackson.JsonMethods._

/**
  * Created by david on 11/12/15.
  */
object NavigationDataExtractor {

  implicit val formats = DefaultFormats

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


  def parseData(json:String): Navigation = {
    val parsedJson = parse(json)
    try{
      val doc = parsedJson.extract[event]
      val data = new Navigation(doc.UUIDC,doc.pais_iso2,timeToStr(doc.fecha),doc.url)
      data
    } catch{
        case mpe:MappingException => println("Error parseando doc: "+mpe.msg)
        val data = new Navigation()
        data
    }

  }

  def timeToStr(epochMillis: String): String =  {
    DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss").print(java.lang.Long.valueOf(epochMillis)*1000)
  }

}
