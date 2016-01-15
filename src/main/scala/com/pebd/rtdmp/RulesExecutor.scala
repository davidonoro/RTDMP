package com.pebd.rtdmp

/**
  * Created by david on 13/12/15.
  */
class RulesExecutor(rulesFile:String) extends Serializable{

  /**
    * EValua una regla de negocio para una navegacion
    * @param data
    * @return
    */
  def evaluarReglas(data:Navigation):Navigation={
    val ksession = KieSessionFactory.getKieSession(rulesFile)
    //val list = List(String)
    //ksession.setGlobal("list",list)
    ksession.execute(data)
    data
  }

  /**
    * Evalua reglas de negocio en un conjunto de objetos
    * @param data
    * @return
    */
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
