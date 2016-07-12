package miner.parse.util;

import junit.framework.TestCase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Created by mafu on 16-7-6.
 */
public class searchPath extends TestCase {


	/*
	 *map示例:html0.body0.div6.div1.div0.div2.div0.div1.div1.div0.ul0.li12.p1==<p>帖数:10</p>
	 */

        public void testParse() {
            Queue<Element> ele_queue = new LinkedList<Element>();
            Queue<String> str_queue =new LinkedList<String>();
            Map<String, Element> map = new HashMap<String, Element>();
            String document = getPath();
            Document doc = Jsoup.parse(document);
            // out.println(doc.html());
		/* 根节点入队 html文件:ele等于整个html文档,ele.tagname等于html*/
            for (Element ele : doc.children()) {
                ele_queue.offer(ele);
//			System.out.println(ele + "==");
                str_queue.offer(ele.tagName() + "0");
//			System.out.println(ele.tagName());
                map.put(ele.tagName(), ele);
            }
		/* 遍历 */
            Element e;
            String s;
            while ((e = ele_queue.poll()) != null && (s = str_queue.poll()) != null) {
                String key = null;
                int idx = 0;
//			out.println(s);
                for (Element ee : e.children()) {
				/* 入队 */
                    ele_queue.offer(ee);
                    key = s + "." + ee.tagName() + idx;
                    while (map.containsKey(key)) {
                        idx++;
                        key = s + "." + ee.tagName() + idx;
                    }
//                out.println(key);
                    map.put(key, ee);
                    if (ee.toString().contains("注册时间")){
                        System.out.print(key+"=======================>"+ee);
//                        System.out.print(key);
                    }
                    str_queue.offer(key);
                }
            }
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
