package entity;

/**
 * Sql Server实体类
 */
public class Servers {

    private String hostip;
    private String hostport;
    private String servername;
    private String serverpasswd;

    public Servers() {
    }

    public Servers(String hostip, String hostport, String servername, String serverpasswd) {
        this.hostip = hostip;
        this.hostport = hostport;
        this.servername = servername;
        this.serverpasswd = serverpasswd;
    }

    public String getHostip() {
        return hostip;
    }

    public void setHostip(String hostip) {
        this.hostip = hostip;
    }

    public String getHostport() {
        return hostport;
    }

    public void setHostport(String hostport) {
        this.hostport = hostport;
    }

    public String getServername() {
        return servername;
    }

    public void setServername(String servername) {
        this.servername = servername;
    }

    public String getServerpasswd() {
        return serverpasswd;
    }

    public void setServerpasswd(String serverpasswd) {
        this.serverpasswd = serverpasswd;
    }
}
