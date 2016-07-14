package miner.proxy;

import junit.framework.TestCase;
import miner.utils.PlatformParas;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 关于取得代理的测试
 */
public class ProxyFetchTest extends TestCase{

    public void testProxyFetech(){
        /* 订单的参数和配置 */
        Map<String, String> proxy_param_map = new HashMap<String, String>();
        Set<String> current_ip_pool;
		/* 订单号 */
        String orderid = PlatformParas.orderid;
		/* 提取数量 */
        String num = "1000";
		/* 质量2，SVIP非常稳定 */
        String quality = "2";
        /* 地区限制 */
        String area="中国";
		/* 代理协议 1表示http 2表示https*/
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
                    System.out.println(proxy_strs[i]);
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
}
