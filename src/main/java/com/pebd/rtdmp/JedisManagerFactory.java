package com.pebd.rtdmp;

import redis.clients.jedis.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by david on 13/12/15.
 */
public class JedisManagerFactory implements Serializable{

    final static String HOST = "localhost";
    final static int port = 6379;

    /**
     * Gestiona las conexiones con el servidor
     */
    static JedisPool pool = new JedisPool(new JedisPoolConfig(),HOST);

    /**
     * Devuelve un objeto Jedis para insertar los datos en Redis
     * @return
     */
    public static Jedis getJedisCluster(){
        return pool.getResource();
    }
}
