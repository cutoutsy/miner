package miner.topo.platform;

/**
 * Workplace class
 *
 * Created by cutoutsy on 8/5/15.
 */
public class Workplace {

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
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

    private String wid;
    private String name;
    private String description;


}
