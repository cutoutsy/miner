package miner.store;

import junit.framework.TestCase;

/**
 * Created by cutoutsy on 15/10/3.
 */
public class StoreTest extends TestCase{

    public static void main(String[] args){
        try {
//            CreateTable.mysqlCheck("3", "1");
//            ImportData.importData();
//            CreateTable.getDataSetByTableName("3121");
            String test = "1468824581065$12233qwer$$1468824583938$$cityname$$Xian";
            System.out.println(test.split("\\$\\$")[0]);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
