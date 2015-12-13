package com.pebd.rtdmp;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.io.Serializable;
import java.util.List;

/**
 * Created by david on 13/12/15.
 */
public class JedisManager implements Serializable{

    public void insertData(Navigation data){

        Jedis cluster = JedisManagerFactory.getJedisCluster();

        cluster.hset(data.getUser(),"USER",data.getUser());
        cluster.hset(data.getUser(),"COUNTRY",data.getPais());
        cluster.hset(data.getUser(),"LAST-UPDATE",data.getFecha());

        List<String> categorias = data.getCategorias();
        for (String categoria:categorias) {
            cluster.hincrBy(data.getUser(),categoria.toUpperCase(),1);
            cluster.hset(data.getUser(),categoria.toUpperCase()+"#LAST-UPDATE",data.getFecha());
        }
    }
}
