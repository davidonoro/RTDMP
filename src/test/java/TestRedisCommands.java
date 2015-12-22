import com.pebd.rtdmp.JedisManagerFactory;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Created by david on 20/12/15.
 */
public class TestRedisCommands {

    public static void main(String args[]){
        Jedis cluster = JedisManagerFactory.getJedisCluster();

        cluster.flushAll();
        cluster.zincrby("clave",1,"valor1");
        cluster.zincrby("clave",1,"valor1");
        cluster.zincrby("clave",1,"valor2");
        cluster.zincrby("clave",1,"valor4");
        cluster.zincrby("clave",1,"valor2");
        Long i = cluster.zrank("clave","valor1");
        System.out.println("Clave que existe: "+i);

        if(i==1){
            System.out.println("Condicion existe OK");
        }

        Long j = cluster.zrank("clave","valor3");
        System.out.println("Clave que no existe: "+j);

        if(j==null){
            System.out.println("Condicion no existe OK");
        }

        Set<String> last = cluster.zrange("clave",0,0);
        System.out.println(last.toString());
    }
}
