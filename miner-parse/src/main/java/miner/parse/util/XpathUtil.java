package miner.parse.util;

import miner.parse.DocObject;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.jsoup.nodes.Element;

import java.util.Map;
import java.util.Queue;

/**
 * Created by mafu on 16-7-14.
 */
public class XpathUtil {
    private  String document;
    private  String result;

    public XpathUtil(String document){
        this.document = document;

    }

    public void parse(String xPath)  {
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        String XpathRes = "none";
        TagNode root = htmlCleaner.clean(this.document);
        Object[] nameArray = new Object[0];
        try {
            nameArray = root.evaluateXPath(xPath);
        } catch (XPatherException e) {
            e.printStackTrace();
        }
        for (Object thenode : nameArray) {
            TagNode tna = (TagNode) thenode;
            XpathRes = tna.getText().toString();
        }
        this.result =  XpathRes;
    }

    public String get_result(){
        return this.result;
    }
}
