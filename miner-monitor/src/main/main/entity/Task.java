package entity;

/**
 * Workspace class
 *
 * Created by cutoutsy on 8/5/15.
 */
public class Task {

    private int id;
    private String wid;
    private String pid;
    private String tid;
    private String name;
    private String description;
    private String urlpattern;
    private String urlgenerate;     //是否进行url生成
    private String isloop;  //是否循环
    private String proxy_open;  //是否开启代理

    public Task() {
    }

    public Task(int id, String wid, String pid, String tid, String name, String description, String urlpattern, String urlgenerate, String isloop, String proxy_open) {
        this.id = id;
        this.wid = wid;
        this.pid = pid;
        this.tid = tid;
        this.name = name;
        this.description = description;
        this.urlpattern = urlpattern;
        this.urlgenerate = urlgenerate;
        this.isloop = isloop;
        this.proxy_open = proxy_open;
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

    public String getUrlpattern() {
        return urlpattern;
    }

    public void setUrlpattern(String urlpattern) {
        this.urlpattern = urlpattern;
    }

    public String getUrlgenerate() {
        return urlgenerate;
    }

    public void setUrlgenerate(String urlgenerate) {
        this.urlgenerate = urlgenerate;
    }

    public String getIsloop() {
        return isloop;
    }

    public void setIsloop(String isloop) {
        this.isloop = isloop;
    }

    public String getProxy_open() {
        return proxy_open;
    }

    public void setProxy_open(String proxy_open) {
        this.proxy_open = proxy_open;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", wid='" + wid + '\'' +
                ", pid='" + pid + '\'' +
                ", tid='" + tid + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", urlpattern='" + urlpattern + '\'' +
                ", urlgenerate='" + urlgenerate + '\'' +
                ", isloop='" + isloop + '\'' +
                ", proxy_open='" + proxy_open + '\'' +
                '}';
    }
}
