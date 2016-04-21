package miner.store;

import junit.framework.TestCase;

/**
 * Created by cutoutsy on 15/10/3.
 */
public class StoreTest extends TestCase{

    public static void main(String[] args){
        try {
            CreateTable.mysqlCheck("3", "1");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
