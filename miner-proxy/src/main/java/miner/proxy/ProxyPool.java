package miner.proxy;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import miner.utils.RedisUtil;
import miner.utils.StaticValue;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import redis.clients.jedis.Jedis;

/**
 * @name ProxyPool
 * @author white
 * @info JUST Manage the proxy pool
 * */
public class ProxyPool extends Thread {
	private Set<String> current_ip_pool;
	/* 刷新总IP池的时间 */
	private int refresh_hour;
	private int refresh_min;
	private int refresh_sec;
	/* 快代理请求参数 */
	private Map<String, String> proxy_param_map;
	/* 用于访问数据库的 */
	private RedisUtil ru;

	private Jedis jedis;

	public RedisUtil get_redis_util() {
		return this.ru;
	}

	public ProxyPool(int refresh_hour, int refresh_min, int refresh_sec,
			Map<String, String> proxy_param_map) {
		this.ru = new RedisUtil();
		this.jedis = ru.getJedisInstance();
		this.refresh_hour = refresh_hour;
		this.refresh_min = refresh_min;
		this.refresh_sec = refresh_sec;
		this.proxy_param_map = proxy_param_map;
	}

	/* 更新current_proxy_pool */
	public void refresh_proxy_set() {
		/* 得到代理 */
		CloseableHttpClient http_client = HttpClients.createDefault();
		try {
			URIBuilder uri = new URIBuilder().setScheme("http")
					.setHost("svip.kuaidaili.com").setPath("/api/getproxy/");
			for (Map.Entry<String, String> e : proxy_param_map.entrySet()) {
				uri.setParameter(e.getKey(), e.getValue());
			}
			URI u = uri.build();
			HttpGet http_get = new HttpGet(u);
			CloseableHttpResponse response = http_client.execute(http_get);
			String proxy_str = EntityUtils.toString(response.getEntity());
			String[] proxy_strs = proxy_str.split("\n");
			current_ip_pool = null;
			current_ip_pool = new HashSet<String>();
			for (int i = 0; i < proxy_strs.length; i++) {
				if (!current_ip_pool.contains(proxy_strs[i])) {
					current_ip_pool.add(proxy_strs[i]);
				}
			}
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date())
					+ " Proxy num:"
					+ current_ip_pool.size());
			response.close();
			http_get.abort();
			http_client.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void refresh_time(int refresh_hour, int refresh_min, int refresh_sec) {
		this.refresh_hour = refresh_hour;
		this.refresh_min = refresh_min;
		this.refresh_sec = refresh_sec;
	}

	/* 更新Redis中的proxy_pool */
	public void refresh_proxy_pool() {
		ru.clean_set(jedis, "proxy_pool");
		ru.save_set(jedis, "proxy_pool", current_ip_pool);
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date()) + " Save proxy pool...");
	}

	public void run() {
		while (true) {
			try {
				/* 每隔一段时间在从快代理fetch代理 */
				sleep(1000 * 60 * 60 * refresh_hour + 1000 * 60 * refresh_min
						+ 1000 * refresh_sec);
				this.refresh_proxy_set();
				this.refresh_proxy_pool();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (refresh_hour < 0 | refresh_min < 0 | refresh_sec < 0) {
				/* 退出机制 */
				break;
			}
		}
	}

	/* 主函数定义 */
	public static void main(String[] args) {
		/* 订单的参数和配置 */
		Map<String, String> proxy_param_map = new HashMap<String, String>();
		/* 订单号 */
		String orderid = "904228416555060";
		/* 提取数量 */
		String num = "1000";
		/* 质量2，SVIP非常稳定 */
		String quality = "2";
        /* 地区限制 */
        String area="中国";
		/* 代理协议 */
		String protocol = "1";
		/* 按照支持GET/POST筛选，1为支持GET */
		String method = "1";
		/* 支持PC的User-Agent为1 */
		String browser = "1";
		/* 高匿名代理 */
		String an_ha = "1";
		/* 极速代理 */
		String sp1 = "1";
		/* 快速代理 */
		String sp2 = "1";
		/* 按照响应速度排序 */
		String sort = "1";
		/* 接口返回内容的格式 */
		String format = "text";
		/* 提取结果列表中每个结果的分隔符\n */
		String sep = "2";
		proxy_param_map.put("orderid", orderid);
		proxy_param_map.put("num", num);
		proxy_param_map.put("quality", quality);
		proxy_param_map.put("protocol", protocol);
		proxy_param_map.put("method", method);
		proxy_param_map.put("browser", browser);
		proxy_param_map.put("an_na", an_ha);
		proxy_param_map.put("sp1", sp1);
		proxy_param_map.put("sp2", sp2);
		proxy_param_map.put("sort", sort);
		proxy_param_map.put("format", format);
		proxy_param_map.put("sep", sep);
		/* 刷新时间设置成5分钟 */
//		ProxyPool pp = new ProxyPool(0, 0, 30, "127.0.0.1", 6379, "xidian123",
//				proxy_param_map);
		ProxyPool pp = new ProxyPool(0, 0, 10, proxy_param_map);
		pp.start();

		// /* 从配置文件读入的task参数 */
		// Map<String, TaskProxySetting> task_proxy_setting_map = new
		// HashMap<String, TaskProxySetting>();
		// task_proxy_setting_map.put("task0", new TaskProxySetting(2000));
		// task_proxy_setting_map.put("task1", new TaskProxySetting(1000));
		// task_proxy_setting_map.put("task2", new TaskProxySetting(1500));
		/* 下面控制task代理池的行为 */
		// ExecutorService pool = Executors.newCachedThreadPool();
		// pool.execute(new TaskProxyPool(new TaskProxySetting(2000), pp
		// .get_redis_util(), "task0"));
		// pool.execute(new TaskProxyPool(new TaskProxySetting(2000, 35000), pp
		// .get_redis_util(), "task1"));
		// pool.execute(new TaskProxyPool(new TaskProxySetting(2000), pp
		// .get_redis_util(), "task2"));

	}
}
