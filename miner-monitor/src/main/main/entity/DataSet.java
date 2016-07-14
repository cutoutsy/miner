package entity;

/**
 * 数据集模型
 */
public class DataSet {

    private String tableName;   //表名
    private String rowKey;  //主键
    private long timestamp; //时间戳
    private String property;    //数据属性
    private String value;       //数据属性值

    public DataSet() {
    }

    public DataSet(String tableName, String rowKey, long timestamp, String property, String value) {
        this.tableName = tableName;
        this.rowKey = rowKey;
        this.timestamp = timestamp;
        this.property = property;
        this.value = value;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DataSet{" +
                "tableName='" + tableName + '\'' +
                ", rowKey='" + rowKey + '\'' +
                ", timestamp=" + timestamp +
                ", property='" + property + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
