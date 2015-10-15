package miner.spider.utils;


import miner.spider.pojo.ProxyPojo;
import miner.utils.RedisUtil;
import miner.utils.StaticValue;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by cutoutsy on 7/24/15.
 */
public class ProxyUtil {
//    public static MyLogger logger = new MyLogger(ProxyUtil.class);
    private static RedisUtil ru;
    public static Jedis redisdb0;

    // 读取代理列表，并加入到proxy中
    public static List<ProxyPojo> proxyList = new LinkedList<ProxyPojo>();

    static {
        try {
            if (StaticValue.proxy_open) {
                ru = new RedisUtil();
                redisdb0 = ru.getJedisInstance();
//                logger.info("proxy server has been used!");
                Set<String> ip_ports = redisdb0.hkeys("ip_port");
                String temp_proxy_paras[] = null;
                ProxyPojo proxyPojo = null;
                if (ip_ports.size() > 0) {
                    Iterator it = ip_ports.iterator();
                    while(it.hasNext()){
                        temp_proxy_paras = it.next().toString().split(":");
                        proxyPojo = new ProxyPojo(temp_proxy_paras[0],
                                Integer.parseInt(temp_proxy_paras[1]));
                        proxyPojo.setAuthEnable(false);// 无需用户名和密码
                        proxyList.add(proxyPojo);
                    }
//					else {
                    //需要用户名，密码验证的暂时不考虑
//							proxyPojo = new ProxyPojo(temp_proxy_paras[0],
//									Integer.parseInt(temp_proxy_paras[1]),
//									temp_proxy_paras[2], temp_proxy_paras[3]);
//							proxyPojo.setAuthEnable(true);// 需要用户名和密码
//							proxyList.add(proxyPojo);
//						}

                }
            } else {
//                logger.info("proxy server is forbidden!");
            }
        } catch (Exception e) {
//            logger.info("读取代理服务器列表参数时抛出异常，请检查!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        for (ProxyPojo proxyPojo : ProxyUtil.proxyList) {
//			httpClientPojoList.add(new HttpClientPojo(proxyPojo));
//			System.out.println(proxyPojo.getIp()+":"+proxyPojo.getPort());
            if(proxyPojo != null){
                System.out.println("===");
            }
        }
    }

}
