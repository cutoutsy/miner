package miner.proxy;

/**
 * @author white
 * @name TaskProxySetting
 * @info 对每个task的配置
 * @extra 这里写成一个类来封装一些配置，应对以后需要加一些东西的情况
 */
public class TaskProxySetting {
	private int delay_time;
	private int dead_time;
    private Long last_action_time;

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

	public TaskProxySetting(int delay_time) {
		this.delay_time = delay_time;
	}

	public TaskProxySetting(int delay_time, int dead_time) {
		this(delay_time);
		this.dead_time = dead_time;
	}
    public TaskProxySetting(int delay_time,int dead_time,Long last_action_time){
        this(delay_time, dead_time);
        this.last_action_time=last_action_time;
    }
}
