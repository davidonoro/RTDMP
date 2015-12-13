package com.pebd.rtdmp;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

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

    static Jedis jedis;

    public static Jedis getJedisCluster(){
        if (jedis == null) {
            jedis = new Jedis(HOST,port);
        }

        return jedis;
    }
}
