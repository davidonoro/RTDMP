package com.pebd.rtdmp

/**
  * Created by david on 13/12/15.
  */
class RulesExecutor(rulesFile:String) extends Serializable{

  def evaluarReglas(data:Navigation):Navigation={
    val ksession = KieSessionFactory.getKieSession(rulesFile)
    //val list = List(String)
    //ksession.setGlobal("list",list)
    ksession.execute(data)
    data
  }

  def evaluarMultipleReglas(data:Iterator[Navigation]):Iterator[Navigation]={
    val ksession = KieSessionFactory.getKieSession(rulesFile)

    val reglasEvaluadas = data.map(navigation=>{

      if(navigation.getDominio!=null){
        ksession.execute(navigation)
        navigation
      }else{
        new Navigation()
      }
    })
    reglasEvaluadas
  }
}
