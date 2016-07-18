package miner.parse.util;

import junit.framework.TestCase;
import miner.parse.DocObject;
import miner.parse.DocType;
import miner.parse.Generator;
import miner.parse.RuleItem;
import miner.parse.data.DataItem;
import miner.parse.data.Packer;
//import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * 针对各个项目解析测试类
 * User: cutoutsy
 * Date: 4/25/2016
 */
public class ProjectParseTest extends TestCase{

    /**
     * 针对麦轮胎轮胎基本信息抽取的测试
     */
    public void testMaiLunTaiParse(){
        /* 抽取单个doc数据的规则库，多个set组成map */
        Map<String, RuleItem> data_rule_map = new HashMap<String, RuleItem>();
        data_rule_map.put("id_price", new RuleItem("name_price", "html0.body0.div3.div1.div1.div1.div0.dl0.dd0.strong0.text"));
        data_rule_map.put("id_brand", new RuleItem("name_brand", "html0.body0.div3.div1.div1.div1.div0.dl1.dd0.span0.text"));
        data_rule_map.put("id_tread", new RuleItem("name_tread", "html0.body0.div3.div1.div1.div1.div0.dl2.dd0.text"));
        data_rule_map.put("id_formate", new RuleItem("name_formate", "html0.body0.div3.div1.div1.div1.div0.dl3.dd0.span0.text"));
        data_rule_map.put("id_weight", new RuleItem("name_weight", "html0.body0.div3.div1.div1.div1.div0.dl4.dd0.text"));
        data_rule_map.put("id_speed", new RuleItem("name_speed", "html0.body0.div3.div1.div1.div1.div0.dl5.dd0.text"));
        data_rule_map.put("id_area", new RuleItem("name_area", "html0.body0.div3.div1.div1.div1.div0.dl6.dd0.text"));
        data_rule_map.put("id_index", new RuleItem("name_index", "html0.body0.div3.div1.div1.div1.div0.div6.em0.text"));
        data_rule_map.put("id_traction", new RuleItem("name_traction", "html0.body0.div3.div1.div1.div1.div0.div6.em1.text"));
        data_rule_map.put("id_temp", new RuleItem("name_temp", "html0.body0.div3.div1.div1.div1.div0.div6.em2.text"));
        /* 封装数据的规则库map */
        Set<DataItem> data_item_set = new HashSet<DataItem>();

        data_item_set.add(new DataItem("1", "1", "1", "1", "none", "none", "none", "none", "id_price", "id_brand", "id_tread", "id_formate", "id_weight", "id_speed", "id_area", "id_index", "id_traction", "id_temp"));
//        data_item_set.add(new DataItem("1", "1", "1", "1", "none", "none", "none", "none", "id_price"));
        /* 数据生成器 */
        Generator g = new Generator();
        StringBuffer doc_str = new StringBuffer();
        try {
            URL url = new URL("http://mailuntai.cn/product/4791.html");
            URLConnection uc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                doc_str.append(inputLine);
        }catch (Exception ex){
            ex.printStackTrace();
        }
//        System.out.println(doc_str);
        g.create_obj(doc_str.toString());
        for (Map.Entry<String, RuleItem> entry : data_rule_map.entrySet()) {
            g.set_rule(entry.getValue());
        }
        g.generate_data();
        Map<String, Object> m = g.get_result();// m里封装了所有抽取的数据
        Iterator<DataItem> data_item_it = data_item_set.iterator();
        while (data_item_it.hasNext()) {
            Packer packer = new Packer(data_item_it.next(), m, data_rule_map);
            String[] result_str=packer.pack();
            System.out.println(result_str[0]);
//            assertEquals(321, result_str[0].length());
        }
    }

    public void testPath(){
        File input = new File("/home/mafu/Templates/airbnb.html");
        BufferedReader reader = null;
        String result = "";
        try {
//            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(input));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
//                System.out.println("line " + line + ": " + tempString);
                result = result+tempString;
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println(doc_str);
        DocObject docObject = new DocObject(result.toString(), DocType.HTML);
        docObject.parse();
        String[] path = new String[10];
        String[] value = {"(11)"};

        for (int i = 0; i < value.length; i++){
            path[i] = docObject.search(value[i]);
        }

        for(int i=0; i < path.length; i++){
            System.out.println(path[i]+"--------"+value[0]);
        }

        //载重指数:html0.body0.div3.div1.div1.div1.div0.dl4.dd0
        //速度级别:html0.body0.div3.div1.div1.div1.div0.dl5.dd0
    }
}
