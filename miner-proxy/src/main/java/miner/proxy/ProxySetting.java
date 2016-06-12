package miner.proxy;

/**
 * @author white
 * @name TaskProxySetting
 * @info 对每个task的配置
 * @extra 这里写成一个类来封装一些配置，应对以后需要加一些东西的情况
 */
public class ProxySetting {
    private int delay_time = 1000*2;
    private int dead_time = 1000*30;
    private Long last_action_time;
    /* 上次更新redis的时间 */
    private Long last_update_time=0l;

    public void set_last_update_time(Long last_update_time){
        this.last_update_time=last_update_time;
    }

    public Long get_last_update_time(){
        return this.last_update_time;
    }

    public void set_last_action_time(Long last_action_time){this.last_action_time=last_action_time;}

    public Long get_last_action_time(){return this.last_action_time;}

    public void set_dead_time(int dead_time) {
        this.dead_time = dead_time;
    }

    public int get_dead_time() {
        return this.dead_time;
    }

    public void set_delay_time(int delay_time) {
        this.delay_time = delay_time;
    }

    public int get_delay_time() {
        return this.delay_time;
    }

    public ProxySetting(){
        this.last_action_time=System.currentTimeMillis();
    }

    public ProxySetting(int delay_time) {
        this();
        this.delay_time = delay_time;
    }

    public ProxySetting(int delay_time, int dead_time) {
        this(delay_time);
        this.dead_time = dead_time;
    }
    public ProxySetting(int delay_time,int dead_time,Long last_action_time){
        this(delay_time, dead_time);
        this.last_action_time=last_action_time;
    }
}
