package entity;

/**
 * Workspace class
 *
 * Created by cutoutsy on 8/5/15.
 */
public class Workspace {

    private int id;
    private String wid;
    private String name;
    private String description;

    public Workspace() {
    }

    public Workspace(int id, String wid, String name, String description) {
        this.id = id;
        this.wid = wid;
        this.name = name;
        this.description = description;
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


    @Override
    public String toString() {
        return "Workspace{" +
                "id=" + id +
                ", wid='" + wid + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
