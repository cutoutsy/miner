package entity;

/**
 * Workspace class
 *
 * Created by cutoutsy on 8/5/15.
 */
public class Project {

    private int id;
    private String wid;
    private String pid;
    private String name;
    private String description;
    private String datasource;  //项目的在redis中的数据源
    private String schedule;    //项目定时策略
    private String precondition;    //项目的前提条件

    public Project() {
    }

    public Project(int id, String wid, String pid, String name, String description, String datasource, String schedule, String precondition) {
        this.id = id;
        this.wid = wid;
        this.pid = pid;
        this.name = name;
        this.description = description;
        this.datasource = datasource;
        this.schedule = schedule;
        this.precondition = precondition;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getPrecondition() {
        return precondition;
    }

    public void setPrecondition(String precondition) {
        this.precondition = precondition;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", wid='" + wid + '\'' +
                ", pid='" + pid + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", datasource='" + datasource + '\'' +
                ", schedule='" + schedule + '\'' +
                ", precondition='" + precondition + '\'' +
                '}';
    }
}
