package miner.parse.util;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static java.lang.System.out;

import miner.parse.DocObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


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

	public void parse() {
		Document doc = Jsoup.parse(document);
		// out.println(doc.html());
		/* 根节点入队 */
		for (Element ele : doc.children()) {
			ele_queue.offer(ele);
			str_queue.offer(ele.tagName() + "0");
			map.put(ele.tagName(), ele);
		}
		/* 遍历 */
		Element e;
		String s;
		while ((e = ele_queue.poll()) != null && (s = str_queue.poll()) != null) {
			String key = null;
			int idx = 0;
			out.println(s);
			for (Element ee : e.children()) {
				/* 入队 */
				ele_queue.offer(ee);
				key = s + "." + ee.tagName() + idx;
				while (map.containsKey(key)) {
					idx++;
					key = s + "." + ee.tagName() + idx;
				}
				map.put(key, ee);
				str_queue.offer(key);
			}
		}
	}
}
