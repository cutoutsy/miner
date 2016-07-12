package miner.parse.util;

import junit.framework.TestCase;
import miner.parse.Generator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by mafu on 16-7-6.
 */
public class getType extends TestCase{
    public void testType(){
        Generator My_Gg = new Generator();
        String data = getPath();
        System.out.print("Type is "+My_Gg.judge_doc_type(data));

    }
    public String getPath(){
        StringBuffer doc_str = new StringBuffer();
        try {
//            String productUrl = "https://zh.airbnb.com/users/show/20928619";
////            String productUrl = "https://www.linkedin.com/in/jeffweiner08";
////            String proxy = "120.52.73.26:8080";
//            String pageSource = Crawl4HttpClient.downLoadPage(productUrl);
//            doc_str.append(pageSource);
            URL url = new URL("https://zh.airbnb.com/users/show/20928619");
            URLConnection uc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                doc_str.append(inputLine);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return doc_str.toString();
    }
}
