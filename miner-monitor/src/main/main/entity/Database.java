package entity;

/**
 * 集群数据库模型
 */
public class Database {

    private String tableName;   //Hbase里的表名
    private int countRow;   //表的总的行数

    public Database() {
    }

    public Database(String tableName, int countRow) {
        this.tableName = tableName;
        this.countRow = countRow;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getCountRow() {
        return countRow;
    }

    public void setCountRow(int countRow) {
        this.countRow = countRow;
    }

    @Override
    public String toString() {
        return "Database{" +
                "tableName='" + tableName + '\'' +
                ", countRow=" + countRow +
                '}';
    }
}
