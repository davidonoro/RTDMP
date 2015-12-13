package com.pebd.rtdmp

/**
  * Created by david on 13/12/15.
  */
class RulesExecutor(rulesFile:String) extends Serializable{

  def evaluarReglas(data:Navigation):Navigation={
    val ksession = KieSessionFactory.getNewKieSession(rulesFile)
    //val list = List(String)
    //ksession.setGlobal("list",list)
    ksession.execute(data)
    data
  }
}
