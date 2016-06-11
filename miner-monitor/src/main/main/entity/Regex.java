package entity;

/**
 * Regex class
 *
 * Created by cutoutsy on 8/5/15.
 */
public class Regex {

    private int id;
    private String wid;
    private String pid;
    private String tid;
    private String tagname;
    private String path;

    public Regex() {
    }

    public Regex(int id, String wid, String pid, String tid, String tagname, String path) {
        this.id = id;
        this.wid = wid;
        this.pid = pid;
        this.tid = tid;
        this.tagname = tagname;
        this.path = path;
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

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Regex{" +
                "id=" + id +
                ", wid='" + wid + '\'' +
                ", pid='" + pid + '\'' +
                ", tid='" + tid + '\'' +
                ", tagname='" + tagname + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
