package entity;

/**
 * ClusterTask class
 *
 * Created by cutoutsy on 8/5/15.
 */
public class ClusterTask {

    private String wid;
    private String pid;
    private String status;      //运行状态
    private String execuNum;    //运行次数

    public ClusterTask() {
    }

    public ClusterTask(String wid, String pid, String status, String execuNum) {
        this.wid = wid;
        this.pid = pid;
        this.status = status;
        this.execuNum = execuNum;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExecuNum() {
        return execuNum;
    }

    public void setExecuNum(String execuNum) {
        this.execuNum = execuNum;
    }

    @Override
    public String toString() {
        return "ClusterTask{" +
                "wid='" + wid + '\'' +
                ", pid='" + pid + '\'' +
                ", status='" + status + '\'' +
                ", execuNum='" + execuNum + '\'' +
                '}';
    }
}
