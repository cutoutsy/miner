package miner.proxy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import miner.utils.RedisUtil;
import redis.clients.jedis.Jedis;

/**
 * @name TaskProxyPool.java
 * @author white
 * @info 控制task代理池的类，从bolt启动
 * @extra 线程自结束机制
 * */
public class TaskProxyPool implements Runnable {
	private int delay_time;
	private RedisUtil ru;
	private Jedis jedis;
	private final String task_handle_name;
	private Long last_action_time;
	private int dead_time = 1000 * 30;

	public String get_task_handle_name() {
		return this.task_handle_name;
	}

	public TaskProxyPool(ProxySetting setting, RedisUtil ru,
			String task_handle_name) {
		this.task_handle_name = task_handle_name;
		this.delay_time = setting.get_delay_time();
		this.dead_time = setting.get_dead_time();
		this.ru = ru;
		this.jedis = ru.getJedisInstance();
		this.init_set();
	}

	/* 当总的代理池更新的时候，需要对white和black名单进行一个更新 */
	public void init_set() {
		/* 这是更新后的proxy_pool */
		Set<String> new_proxy_set = null;
		while (new_proxy_set == null || new_proxy_set.size() == 0) {
			new_proxy_set = jedis.smembers("proxy_pool");
		}
		ru.clean_set(jedis, task_handle_name + "_white_set");
		ru.clean_set(jedis, task_handle_name + "_black_set");
		ru.save_set(jedis, task_handle_name + "_white_set", new_proxy_set);
		last_action_time = System.currentTimeMillis();
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date())
				+ " Init "
				+ task_handle_name
				+ " proxy pool");
	}

	public void refresh_white_and_black_set() {
		/* 得到时间戳 */
		long current_time = System.currentTimeMillis();
		Set<String> black_set = jedis.smembers(task_handle_name + "_black_set");
		if (black_set != null) {
			Iterator<String> it = black_set.iterator();
			while (it.hasNext()) {
				String tmp_black = it.next();
				String[] black_ip_time = tmp_black.split("_");
				if (current_time - Long.parseLong(black_ip_time[1]) > delay_time) {
					jedis.srem(task_handle_name + "_black_set", tmp_black);
					jedis.sadd(task_handle_name + "_white_set",
							black_ip_time[0]);
					last_action_time = System.currentTimeMillis();
				}
			}
		}
	}

	public void run() {
		while (true) {
			refresh_white_and_black_set();
			Long current_time = System.currentTimeMillis();
			if (current_time - last_action_time > dead_time) {
				break;
			}
		}
		this.destory_task_pool();
	}

	/* 清理task的方法 */
	public void destory_task_pool() {
		ru.clean_set(jedis, task_handle_name + "_white_set");
		ru.clean_set(jedis, task_handle_name + "_black_set");
		/* 释放jedis连接 */
		ru.release_jedis(jedis);
	}

}
