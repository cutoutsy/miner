package miner.spider.pojo;

/**
 * Proxy Object Class
 */

public class ProxyPojo {
    private String ip;
    private int port;
    //用此代理时，失败的请求次数
    private int fail_count;
    private String username;
    private String password;

    @Override
    public String toString() {
        return "ProxyPojo [authEnable=" + authEnable + ", ip=" + ip
                + ", port=" + port  + "]";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 是不是需要输入代理的用户名和密码的标志位
     */
    private boolean authEnable;

    public boolean isAuthEnable() {
        return authEnable;
    }

    public void setAuthEnable(boolean authEnable) {
        this.authEnable = authEnable;
    }

    public int getFail_count() {
        return fail_count;
    }

    public void setFail_count(int fail_count) {
        this.fail_count = fail_count;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    // 无认证,现在我们用的为无需认证
    public ProxyPojo(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

}
