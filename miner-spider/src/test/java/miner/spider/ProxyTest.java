package miner.spider;

import miner.spider.httpclient.Crawl4HttpClient;
import miner.spider.manager.HttpClientPojoManager;
import miner.spider.utils.StaticValue;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;

import org.apache.http.client.methods.CloseableHttpResponse;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

import java.net.URI;

/**
 * Created by cutoutsy on 8/25/15.
 */
public class ProxyTest {

    public static void main(String[] args){
        int success = 0;
        int fail = 0;
        try{
            String proxyUrl = "http://svip.kuaidaili.com/api/getproxy/?orderid=944046759126341&num=500&area=%E4%B8%AD%E5%9B%BD&browser=1&protocol=1&method=1&an_tr=1&an_an=1&an_ha=1&sp2=1&quality=2&sort=0&format=text&sep=1";
            String proxyList = Crawl4HttpClient.downLoadPage(proxyUrl);
            String[] proxys = proxyList.split("\\r\\n");
            for(int i = 0; i < proxys.length; i++){
//                System.out.println(proxys[i]+"---");
                Thread.sleep(1000);
                if(TestProxyByBaidu(proxys[i])){
                    System.out.println(proxys[i]+"---fetch success!");
                    success++;
                }else{
                    System.out.println(proxys[i]+"---fetch failed!");
                    fail++;
                }
            }
            System.out.println("total:"+proxys.length);
            System.out.println("success:"+success);
            System.out.println("fail:"+fail);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static boolean TestProxyByBaidu(String proxyString){
        String testString = "030173";
        boolean reflag = true;
        String ip = proxyString.split(":")[0];
        int port = Integer.valueOf(proxyString.split(":")[1]);
        try{
            String url = "http://www.baidu.com/";
            HttpClient httpClient = HttpClients.custom().build();
            //211.144.72.154:8080
            HttpHost proxy = new HttpHost(ip, port);
            //无需验证的
            DefaultProxyRoutePlanner routePlanner= new DefaultProxyRoutePlanner(proxy);
            httpClient = HttpClients.custom().setRoutePlanner(routePlanner).build();
            RequestConfig.Builder config_builder = RequestConfig.custom();
            config_builder.setProxy(proxy);
            config_builder.setSocketTimeout(StaticValue.http_connection_timeout);
            config_builder.setConnectTimeout(StaticValue.http_read_timeout);
            RequestConfig requestConfig = config_builder.build();

            RequestBuilder rb = null;
            rb = RequestBuilder.get().setUri(URI.create(url));

            HttpUriRequest requestAll = null;
            rb.setConfig(requestConfig);
            requestAll = rb.build();
            CloseableHttpResponse response = (CloseableHttpResponse)httpClient.execute(requestAll);
            String re = Crawl4HttpClient.parserResponse_v2(response);
//            System.out.println(Crawl4HttpClient.parserResponse_v2(response).substring(0,100));
            if(!re.contains(testString)){
                reflag = false;
                System.out.println(re);
            }

        }catch (Exception ex){
            ex.printStackTrace();
            reflag = false;
        }

        return reflag;
    }
}
