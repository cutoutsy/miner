package miner.spider.httpclient;

import miner.spider.charset.EncodeUtil;
import miner.spider.enumeration.HttpRequestMethod;
import miner.spider.manager.HttpClientPojoManager;
import miner.spider.pojo.ContentPojo;
import miner.spider.pojo.HttpRequestPojo;
import miner.spider.utils.ObjectAndByteArrayConvertUtil;
import miner.utils.MySysLogger;
import miner.utils.StaticValue;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Httpclient Download Class
 */

public class Crawl4HttpClient {

    private static MySysLogger logger = new MySysLogger(Crawl4HttpClient.class);

    public static String crawlWebPage(HttpRequestPojo requestPojo){
        CloseableHttpResponse response = null;
        try {
            RequestBuilder rb = null;
            if(requestPojo.isGetMethod()){
                rb = RequestBuilder.get().setUri(URI.create(requestPojo.getUrl()));
            }else{
                rb = RequestBuilder.post().setUri(new URI(requestPojo.getUrl()));
            }
            Map<String,Object> map = null;

            //设置头部信息
            if((map = requestPojo.getHeaderMap()) != null){
                for(Map.Entry<String,Object> entry:map.entrySet()){
                    rb.addHeader(entry.getKey(),entry.getValue().toString());
                }
            }
            //设置参数
            if((map = requestPojo.getParasMap()) != null){
                for(Map.Entry<String, Object> entry : map.entrySet()){
                    rb.addParameter(entry.getKey(),entry.getValue().toString());
                }
            }

            //将form data编码完成后放入entity中
            if(requestPojo.getFormEntity() != null){
                rb.setEntity(requestPojo.getFormEntity());
            }

            //查看是否设置代理
            HttpUriRequest requestAll = null;
            HttpClientPojoManager.HttpClientPojo httpClientPojo = HttpClientPojoManager.getHttpClientPojo();
//            HttpClientPojoManager.HttpClientPojo httpClientPojo = new HttpClientPojoManager.HttpClientPojo();

            //执行请求
            if(StaticValue.proxy_open){
                rb.setConfig(httpClientPojo.getRequestConfig());
                requestAll = rb.build();
                System.err.println(httpClientPojo);
                response = httpClientPojo.getHttpClient().execute(requestAll);
            }else{
                rb.setConfig(HttpClientPojoManager.default_requestConfig);
                requestAll = rb.build();
                response = httpClientPojo.getHttpClient().execute(requestAll);
            }

            return parserResponse_v2(response);

        }catch(SocketTimeoutException timeOutException){
            timeOutException.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(response != null){
                try{
                    response.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public static EncodeUtil encodeUtil = new EncodeUtil();

    public static String parserResponse_v2(CloseableHttpResponse response){
//		System.out.println(response);
        try{
            HttpEntity entity = response.getEntity();
            byte[] byteArray = ObjectAndByteArrayConvertUtil.getByteArrayOutputStream(entity.getContent());
			ContentPojo contentPojo = encodeUtil.getWebPageCharset(byteArray,entity.getContentType().toString());
            if(contentPojo.getCharset() == null){
                //return json,can not find charset
                return new String(byteArray,"utf-8");
            }
			if(contentPojo != null){
				return new String(byteArray,contentPojo.getCharset());
			}
//            return new String(byteArray,"gb2312");
        }catch(Exception e){
            e.printStackTrace();
        }finally{
        }
        return null;
    }

    /**
     *
     * @param url   request url
     * @param proxyString   proxy ip:port
     * @return  return url page source
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static String downLoadPage(String url, String proxyString) throws IOException, ClientProtocolException {
        String reString = "";
        String ip = proxyString.split(":")[0];
        int port = Integer.valueOf(proxyString.split(":")[1]);

        HttpClient httpClient = HttpClients.custom().build();
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
        reString = re;

        return reString;
    }


    public static String downLoadPage(String url){

        HttpRequestPojo requestPojo = new HttpRequestPojo();
        requestPojo.setRequestMethod(HttpRequestMethod.GET);

        Map<String, Object> headerMap = new HashMap<String, Object>();
        Map<String, Object> parasMap = new HashMap<String, Object>();
        // Map<String, String> formNameValueMap = new HashMap<String, String>();

        requestPojo.setUrl(url);
        requestPojo.setHeaderMap(headerMap);
        requestPojo.setParasMap(parasMap);
        String source = crawlWebPage(requestPojo);
        return source;
    }

    public static CloseableHttpResponse getResponse(HttpRequestPojo requestPojo) {
        CloseableHttpResponse response = null;
        try {
            RequestBuilder rb = null;
            if (requestPojo.isGetMethod()) {
                rb = RequestBuilder.get().setUri(
                        URI.create(requestPojo.getUrl()));
            } else {
                rb = RequestBuilder.post()
                        .setUri(new URI(requestPojo.getUrl()));
            }
            Map<String, Object> map = null;
            if ((map = requestPojo.getHeaderMap()) != null) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    rb.addHeader(entry.getKey(), entry.getValue().toString());
                }
            }
            if ((map = requestPojo.getParasMap()) != null) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    rb
                            .addParameter(entry.getKey(), entry.getValue()
                                    .toString());
                }
            }
            // 将form data编码完成后放入entity中
            if (requestPojo.getFormEntity() != null) {
                rb.setEntity(requestPojo.getFormEntity());
            }

            // 查看是否设置代理
            HttpUriRequest requestAll = null;
            HttpClientPojoManager.HttpClientPojo httpClientPojo = HttpClientPojoManager
                    .getHttpClientPojo();
            // 执行请求
            if (StaticValue.proxy_open) {
                rb.setConfig(httpClientPojo.getRequestConfig());
                requestAll = rb.build();
                System.out.println(httpClientPojo.getProxyPojo());
                response = httpClientPojo.getHttpClient().execute(requestAll);
            } else {
                rb.setConfig(HttpClientPojoManager.default_requestConfig);
                requestAll = rb.build();
                response = httpClientPojo.getHttpClient().execute(requestAll);
            }

            return response;
        } catch (SocketTimeoutException timeOutException) {
            // 此种情况将会认为可能是代理异常失效，但暂不处理这种异常对代理替换策略的影响的!
            timeOutException.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {




//        System.out.println("done!");
//        String url = "http://hotel.elong.com/isajax/HotelFillOrder/GetOrderCountIn24Hours?hotelId=40101627";
////        String proxy = "115.159.5.247:8080";
//        String re = downLoadPage(url);
//        System.out.println(re);

        try {
            int k = 3/0;
        }catch (Exception e){
            logger.error("error"+e);
        }

    }

}
