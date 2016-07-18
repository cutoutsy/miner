package miner.parse;

import miner.parse.util.XpathUtil;

/**
 * Created by mafu on 16-7-18.
 */
public class XpathObject {

    private String document;
    private DocType doc_type;
    //工具类的声明
    private XpathUtil x_util;

    public XpathObject(String documnent,DocType type){
        this.document = documnent;
        this.doc_type = type;
    }

    //如果要增加新的解析方式，就必须更改get_value函数，由外部进行判断后调用
    public String get_value(String path){
        x_util = new XpathUtil(this.document);
        x_util.parse(path);
        return x_util.get_result();
    }

    //先写着不知道有啥用
    public DocType get_type() {
        return this.doc_type;
    }
}
