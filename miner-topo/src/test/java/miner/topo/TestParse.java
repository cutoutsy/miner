package miner.topo;


import miner.utils.MySysLogger;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by cutoutsy on 8/12/15.
 */
public class TestParse {

    public static void main(String[] args){
       try{

           int k = 2/0;
       }catch (Exception ex){
           StackTraceElement[] aa = ex.getStackTrace();
           for(int i = 0;  i < aa.length; i++){
               System.out.println(aa[i].toString());
           }
       }

    }
}
