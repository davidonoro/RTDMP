package com.pebd.rtdmp;

import redis.clients.jedis.Jedis;
import java.io.Serializable;
import java.util.List;


/**
 * Created by david on 13/12/15.
 */
public class JedisManager implements Serializable{

    private static final int TOPKHITTERS = 100;

    public void insertData(Navigation data){
        try{
            Jedis cluster = JedisManagerFactory.getJedisCluster();

            cluster.hset(data.getUser(),"USER",data.getUser());
            cluster.hset(data.getUser(),"COUNTRY",data.getPais());
            cluster.hset(data.getUser(),"LAST-UPDATE",data.getFecha());

            List<String> categorias = data.getCategorias();
            for (String categoria:categorias) {
                cluster.hincrBy(data.getUser(),"CAT#"+categoria.toUpperCase(),1);
                cluster.hset(data.getUser(),"LAST-UPDATE#"+categoria.toUpperCase(),data.getFecha());
            }

            cluster.close();

        }catch(Exception e){
            System.out.println("Error con Redis: "+e.getMessage());
        }

    }

    public void insertMultipleData(Navigation[] multipleData){
        try{
            Jedis cluster = JedisManagerFactory.getJedisCluster();

            Navigation data = null;
            for (int i = 0;i<multipleData.length;i++) {
                data = multipleData[i];

                System.out.println("Usuario: "+data.getUser()+" Cats "+data.printCategorias());

                cluster.hset(data.getUser(), "USER", data.getUser());
                cluster.hset(data.getUser(), "COUNTRY", data.getPais());
                cluster.hset(data.getUser(), "LAST-UPDATE", data.getFecha());

                List<String> categorias = data.getCategorias();
                for (String categoria : categorias) {
                    cluster.hincrBy(data.getUser(), "CAT#" + categoria.toUpperCase(), 1);
                    cluster.hset(data.getUser(), "LAST-UPDATE#" + categoria.toUpperCase(), data.getFecha());
                }

                // Actualizacion HeavyHitters Global
                if (cluster.zrank("HEAVYHITTERS",data.getUser()) != null){
                    cluster.zincrby("HEAVYHITTERS",1,data.getUser());
                }else {
                    if(cluster.zcard("HEAVYHITTERS")<TOPKHITTERS){
                        cluster.zincrby("HEAVYHITTERS",1,data.getUser());
                    }else{
                        String last = cluster.zrange("HEAVYHITTERS",0,0).iterator().next();
                        Long score = cluster.zrank("HEAVYHITTERS",last);
                        cluster.zrem("HEAVYHITTERS",last);
                        cluster.zincrby("HEAVYHITTERS",1,data.getUser());
                    }
                }

            }
            cluster.close();

            System.out.println("Batch ha insertado "+multipleData.length+" registros");

        }catch(Exception e){
            System.out.println("Error con Redis: "+e.getMessage());
        }


    }
}
