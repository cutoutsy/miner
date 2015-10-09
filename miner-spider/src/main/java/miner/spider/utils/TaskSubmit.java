package miner.spider.utils;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.util.Iterator;
import java.util.List;

/**
 * 用于提交任务处理
 */

public class TaskSubmit {

    public static void main(String[] args){
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read("./conf/template.xml");
            Element root = document.getRootElement();

            for ( Iterator i = root.elementIterator(); i.hasNext(); ) {
                Element element = (Element) i.next();
                for(Iterator k = element.elementIterator(); k.hasNext();){
                    Element elementc = (Element) k.next();
                    System.out.println(elementc.getText());
                }
               // System.out.println(element.toString());
            }

            for ( Iterator i = root.elementIterator( "template" ); i.hasNext(); ) {
                Element foo = (Element) i.next();
                // do something
                System.out.println(foo.toString());
            }

            for ( Iterator i = root.attributeIterator(); i.hasNext(); ) {
                Attribute attribute = (Attribute) i.next();
                // do something
                System.out.println(attribute.getText());
            }

            /*
            List list = document.selectNodes( "//foo/bar" );

            Node node = document.selectSingleNode( "//foo/bar/author" );

            String name = node.valueOf( "@name" );
            */

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
