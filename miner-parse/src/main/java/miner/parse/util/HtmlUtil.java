package miner.parse.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static java.lang.System.out;

import miner.parse.DocObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/*
 * @ name:      HtmlUtil.java
 * @ author:    white
 * @ info:      处理HTML文档的类
 * */
public class HtmlUtil {
	private String document;
	private Queue<Element> ele_queue;
	private Queue<String> str_queue;
	private Map<String, Element> map;

	public HtmlUtil(String document, DocObject object) {
		this.ele_queue = new LinkedList<Element>();
		this.str_queue = new LinkedList<String>();
		this.document = document;
		this.map = object.get_html_map();
	}

	/*
	 *map示例:html0.body0.div6.div1.div0.div2.div0.div1.div1.div0.ul0.li12.p1==<p>帖数:10</p>
	 */

	public void parse() {
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
				str_queue.offer(key);
			}
		}
	}
}
