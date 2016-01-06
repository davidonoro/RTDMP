package com.pebd.rtdmp;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.io.Serializable;
import java.util.List;


/**
 * Created by david on 13/12/15.
 */
public class JedisManager implements Serializable{


    private static final int TOPKHITTERS = -101;

    public void insertData(Navigation data){
        try{
            Jedis cluster = JedisManagerFactory.getJedisCluster();
            boolean nuevoUsuario = cluster.exists(data.getUser());

            // Estadisticas totales
            cluster.incrBy("TOTALHITS",1);
            if(data.getPais() != ""){
                cluster.incrBy("TOTALHITS_"+data.getPais(),1);
            }


            if(nuevoUsuario){
                cluster.incrBy("TOTALUNIQUE",1);
                if(data.getPais() != "") {
                    cluster.incrBy("TOTALUNIQUE_" + data.getPais(), 1);
                }
            }

            // Estadisticas de usuario
            cluster.hset(data.getUser(),"USER",data.getUser());
            cluster.hset(data.getUser(),"COUNTRY",data.getPais());
            cluster.hset(data.getUser(),"LAST_UPDATE",data.getFecha());
            long totalHits = cluster.hincrBy(data.getUser(),"TOTALHITS",1);

            cluster.zadd("HEAVYHITTERS",totalHits,data.getUser());
            cluster.zremrangeByRank("HEAVYHITTERS",0,TOPKHITTERS);

            if(data.getPais() != "") {
                cluster.zadd("HEAVYHITTERS_" + data.getPais(), totalHits, data.getUser());
                cluster.zremrangeByRank("HEAVYHITTERS_" + data.getPais(), 0, TOPKHITTERS);
            }


            // Por categoria
            List<String> categorias = data.getCategorias();
            for (String categoria:categorias) {

                cluster.incrBy("TOTALCAT_"+categoria.toUpperCase(),1);
                if(data.getPais() != "") {
                    cluster.incrBy("TOTALCAT_" + categoria.toUpperCase() + "_" + data.getPais(), 1);
                }

                long totalCatHits = cluster.hincrBy(data.getUser(),"CAT_"+categoria.toUpperCase(),1);
                cluster.hset(data.getUser(),"LAST-UPDATE_"+categoria.toUpperCase(),data.getFecha());

                cluster.zadd("HEAVYHITTERS_"+categoria.toUpperCase(),totalHits,data.getUser());
                cluster.zremrangeByRank("HEAVYHITTERS_"+categoria.toUpperCase(),0,TOPKHITTERS);

                if(data.getPais() != "") {
                    cluster.zadd("HEAVYHITTERS_" + categoria.toUpperCase() + "_" + data.getPais(), totalHits, data.getUser());
                    cluster.zremrangeByRank("HEAVYHITTERS_" + categoria.toUpperCase() + "_" + data.getPais(), 0, TOPKHITTERS);
                }
            }
            cluster.close();

        }catch(Exception e){
            System.out.println("Error con Redis: "+e.toString());
        }

    }
}
