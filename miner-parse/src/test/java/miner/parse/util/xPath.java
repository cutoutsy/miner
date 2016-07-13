package miner.parse.util;

import junit.framework.TestCase;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by mafu on 16-7-13.
 */
public class xPath extends TestCase{
    public  void testXpath() throws XPatherException {
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
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode root = htmlCleaner.clean(result);
        Object[] nameArray = root.evaluateXPath("//*[@id=\"review-31268345\"]/div[2]/div/div[1]/div/p");
        for (Object thenode : nameArray) {
            TagNode tna = (TagNode) thenode;
            System.out.print(tna.getText().toString());
        }
    }
}
