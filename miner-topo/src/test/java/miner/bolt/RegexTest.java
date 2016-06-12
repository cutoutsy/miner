package miner.bolt;

import junit.framework.TestCase;
import miner.spider.pojo.Data;
import miner.spider.utils.MysqlUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cutoutsy on 5/8/16.
 */
public class RegexTest extends TestCase{

    public void testReadData(){
        HashMap<String, Data> dataScheme = new HashMap<String, Data>();

        dataScheme = MysqlUtil.getData();

        for (Map.Entry<String, Data> entry : dataScheme.entrySet()) {
            String dataInfo = entry.getKey();
            System.out.println(dataInfo);
            String tempProjectInfo = dataInfo.split("-")[0]+dataInfo.split("-")[1]+dataInfo.split("-")[2];
            if("411".equals(tempProjectInfo)){
                System.out.println(dataInfo+"====");
                System.out.println(entry.getValue().getProperty());
            }
        }
    }
}
