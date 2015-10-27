package miner.spider;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by cutoutsy on 15/10/27.
 */
public class Main {

    public static void main(String args[]){
        Main main = new Main();
        main.test();
    }

    public void test(){
        Map<String, String> bb = new HashMap();
        bb.put("1", "1111");
        bb.put("2", "2222");
        
        for(Map.Entry<String, String> entry : bb.entrySet()){
            String key = entry.getKey();
            bb.remove(key);
        }
        System.out.println("Success!");
    }
}
