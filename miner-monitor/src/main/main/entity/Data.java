package entity;

/**
 * Workspace class
 *
 * Created by cutoutsy on 8/5/15.
 */
public class Data {

    private int id;
    private String wid;
    private String pid;
    private String tid;
    private String dataid;
    private String description;
    private String property;
    private String rowKey;     //主键
    private String foreignKey;  //外键
    private String foreignValue;  //外键值
    private String link;    //链接的表名
    private String processWay;  //数据处理方式
    private String lcondition;  //循环的task

    public Data() {
    }

    public Data(int id, String wid, String pid, String tid, String dataid, String description, String property, String rowKey, String foreignKey, String foreignValue, String link, String processWay, String lcondition) {
        this.id = id;
        this.wid = wid;
        this.pid = pid;
        this.tid = tid;
        this.dataid = dataid;
        this.description = description;
        this.property = property;
        this.rowKey = rowKey;
        this.foreignKey = foreignKey;
        this.foreignValue = foreignValue;
        this.link = link;
        this.processWay = processWay;
        this.lcondition = lcondition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDataid() {
        return dataid;
    }

    public void setDataid(String dataid) {
        this.dataid = dataid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
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

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", wid='" + wid + '\'' +
                ", pid='" + pid + '\'' +
                ", tid='" + tid + '\'' +
                ", dataid='" + dataid + '\'' +
                ", description='" + description + '\'' +
                ", property='" + property + '\'' +
                ", rowKey='" + rowKey + '\'' +
                ", foreignKey='" + foreignKey + '\'' +
                ", foreignValue='" + foreignValue + '\'' +
                ", link='" + link + '\'' +
                ", processWay='" + processWay + '\'' +
                ", lcondition='" + lcondition + '\'' +
                '}';
    }
}
