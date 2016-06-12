package miner.spider.pojo;

import miner.spider.utils.MysqlUtil;

import java.util.HashMap;

/**
 * Data object
 */
public class Data {
    private String wid;
    private String pid;
    private String tid;
    private String did;
    private String desc;
    private String property;
    private String rowKey;
    private String foreignKey;
    private String foreignValue;
    private String link;
    private String processWay;
    private String lcondition;

    public Data(String dataName) {
        HashMap<String, String> dataMap = MysqlUtil.getData(dataName);
        this.wid = dataMap.get("wid");
        this.pid = dataMap.get("pid");
        this.tid = dataMap.get("tid");
        this.did = dataMap.get("did");
        this.desc = dataMap.get("desc");
        this.property = dataMap.get("property");
        this.rowKey = dataMap.get("rowKey");
        this.foreignKey = dataMap.get("foreignKey");
        this.foreignValue = dataMap.get("foreignValue");
        this.link = dataMap.get("link");
        this.processWay = dataMap.get("processWay");
        this.lcondition = dataMap.get("lcondition");
    }

    public Data(){

    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(String foreignKey) {
        this.foreignKey = foreignKey;
    }

    public String getForeignValue() {
        return foreignValue;
    }

    public void setForeignValue(String foreignValue) {
        this.foreignValue = foreignValue;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getProcessWay() {
        return processWay;
    }

    public void setProcessWay(String processWay) {
        this.processWay = processWay;
    }

    public String getLcondition() {
        return lcondition;
    }

    public void setLcondition(String lcondition) {
        this.lcondition = lcondition;
    }


    public static void main(String[] args){
        Data dt = new Data("1-1-1-1");
        System.out.println(dt.getDesc());
    }

}
