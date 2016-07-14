package miner.parse.util;

import junit.framework.TestCase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by mafu on 16-7-11.
 */
public class reflectTest extends TestCase{
    public void testGetValue() throws IOException {
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
//        System.out.print(input);
        Document doc = Jsoup.parse(result, "UTF-8");
//        System.out.print(doc);
        Elements contents = doc.getElementsByClass("text-center-sm");
//        Element uniContent = doc.getElementById("review-37240988");
//        System.out.print(content);
//        System.out.print(uniContent);
//        Elements links = content.getElementsByTag("a");
        int i = 0;
        String replyRes = "{" ;
        for (Element content : contents) {
            String com_name =  content.getElementsByClass("profile-name").text();
            String com_link = "wwww.airbnb.com"+content.select("a[href]").first().attr("href");
            String com_comment = content.getElementsByClass("expandable-content").text();
            String com_city = "none";
            if (content.getElementsByClass("link-reset").text().toString().length() >= 1){
                com_city = content.getElementsByClass("link-reset").text();
            }
            String com_time = "none";
            if (content.getElementsByClass("show-sm").text().toString().length() >= 1){
                com_time = content.getElementsByClass("show-sm").text();
            }
            replyRes = replyRes+"\"comment"+i+"\":\""+com_name+"$$"+com_link+"$$"+com_comment+"$$"+com_city+"$$"+com_time+"\",";
//            System.out.print(com_name+"  + ");
//            System.out.print(com_link +"  + ");
//            System.out.print(com_time+"  + ");
//            System.out.print(com_city+"  + ");
//            System.out.print(com_comment+"   \n-------------------------\n");
            i++;
        }
        replyRes = replyRes.substring(0,replyRes.length()-1)+"}";
        System.out.print(replyRes);
    }
}
