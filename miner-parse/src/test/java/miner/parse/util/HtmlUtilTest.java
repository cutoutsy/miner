package miner.parse.util;

import miner.parse.DocObject;
import miner.parse.DocType;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by cutoutsy on 3/20/16.
 */
public class HtmlUtilTest {

    @Test
    public void testParse(){
        File file = new File("/Users/cutoutsy/Desktop/test.html");
        String doc_str = "";
        if (file.isFile() && file.exists()) {
            InputStreamReader read;
            try {
                read = new InputStreamReader(new FileInputStream(file),
                        "UTF8");
                BufferedReader buffered_reader = new BufferedReader(read);
                String line = null;
                while ((line = buffered_reader.readLine()) != null) {
                    doc_str += line;
                }
                buffered_reader.close();
                read.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        DocObject object = new DocObject(doc_str, DocType.HTML);
        HtmlUtil hu = new HtmlUtil(doc_str, object);

        hu.parse();

    }

    @Test
    public void testJsonParse(){
        File file = new File("/Users/cutoutsy/Desktop/stocks.php");
        String doc_str = "";
        if (file.isFile() && file.exists()) {
            InputStreamReader read;
            try {
                read = new InputStreamReader(new FileInputStream(file),
                        "UTF8");
                BufferedReader buffered_reader = new BufferedReader(read);
                String line = null;
                while ((line = buffered_reader.readLine()) != null) {
                    doc_str += line;
                }
                buffered_reader.close();
                read.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        DocObject object = new DocObject(doc_str, DocType.HTML);
        JsonUtil ju = new JsonUtil(doc_str, object);

        ju.parse();

    }

}
