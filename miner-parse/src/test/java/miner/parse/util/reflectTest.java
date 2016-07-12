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
        for (Element content : contents) {
            System.out.print(content.getElementsByClass("profile-name").text()+"  + ");
            System.out.print(content.getElementsByClass("show-sm").text()+"  + ");
            System.out.print(content.getElementsByClass("link-reset").text()+"  + ");
            System.out.print(content.getElementsByClass("expandable-content").text()+"   \n-------------------------\n");
        }

    }
}
