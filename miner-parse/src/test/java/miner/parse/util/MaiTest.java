package miner.parse.util;

import junit.framework.TestCase;
import org.codehaus.jettison.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MaFu on 2016/5/7.
 */
public class MaiTest extends TestCase {

//    Map<String,String> map = new HashMap();
    public  void testMai() throws IOException {
//         JSONObject root_obj = new JSONObject();


        URL url = new URL("http://mailuntai.cn/product/4937.html");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        InputStream is = con.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "utf-8");
        String result = "";
        int read;
        while ((read = isr.read()) != -1) {
            result += (char) read;
        }
        isr.close();
        String price,sale = "";
        String pattern = "(?<=(price = \\[))\\S+(?=(]))";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(result);
        if (m.find( )) {
             price = (String) m.group(0);
//            try {
//                root_obj.put("price",(String) m.group(0));
//            } catch (org.json.JSONException e) {
//                e.printStackTrace();
//            }
            System.out.println("Found value: " +  m.group(0) );
        } else {
             price = "none";
//            try {
//                root_obj.put("price","none");
//            } catch (org.json.JSONException e) {
//                e.printStackTrace();
//            }
            System.out.println("NO MATCH");
        }

        pattern =  "(?<=(sale = \\\"))\\S+(?=(\\\"))";
         r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
         m = r.matcher(result);
        if (m.find( )) {
             sale = (String) m.group(0);
//            try {
//                root_obj.put("sale",(String) m.group(0));
//            } catch (org.json.JSONException e) {
//                e.printStackTrace();
//            }
            System.out.println("Found value: " + m.group(0) );
        } else {
             sale = "none";
//            try {
//                root_obj.put("sale",(String) m.group(0));
//            } catch (org.json.JSONException e) {
//                e.printStackTrace();
//            }
            System.out.println("NO MATCH");
        }
//        System.out.print(result);
        System.out.print("{\"price\":\""+price+"\""+","+"\"sale\":"+"\""+sale+"\"}");
    }
}
