package miner.spider.pojo;

import miner.spider.enumeration.CharsetEnum;
import miner.spider.enumeration.HttpRequestMethod;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by cutoutsy on 7/24/15.
 */
public class HttpRequestPojo {
    //请求的方法
    private HttpRequestMethod requestMethod = HttpRequestMethod.GET;
    private String url;
    private Map<String,Object> headerMap;
    private Map<String,Object> parasMap;
    //代理
//	private ProxyPojo proxyPojo;
    //传递form表单参数
    private UrlEncodedFormEntity formEntity;


    // 仅添加一次,暂不支持添加多次map值对.
    // 解决post中传递为utf-8编码的中文value
    public void setFormNameValePairMap(Map<String, String> formNameValePairMap,CharsetEnum charsetEnum) {
        if (!formNameValePairMap.isEmpty()) {
            // formNameValePairMap
            List<NameValuePair> formNameValueParams = new ArrayList<NameValuePair>();
            Set<String> keySet = formNameValePairMap.keySet();
            for (String key : keySet) {
                formNameValueParams.add(new BasicNameValuePair(key,formNameValePairMap.get(key)));
            }
            try {
                this.formEntity = new UrlEncodedFormEntity(formNameValueParams,charsetEnum.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public HttpRequestPojo(){
        this.requestMethod = HttpRequestMethod.GET;
    }

    public HttpRequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(HttpRequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, Object> headerMap) {
        this.headerMap = headerMap;
    }

    public UrlEncodedFormEntity getFormEntity() {
        return formEntity;
    }

    public void setFormEntity(UrlEncodedFormEntity formEntity) {
        this.formEntity = formEntity;
    }

    public Map<String, Object> getParasMap() {
        return parasMap;
    }

    public void setParasMap(Map<String, Object> parasMap) {
        this.parasMap = parasMap;
    }

    public void addHeaderMap(String key,Object value){
        this.headerMap.put(key, value);
    }

    public void addParasMap(String key,Object value){
        this.parasMap.put(key, value);
    }

    public boolean isPostMethod(){
        return this.getRequestMethod() == HttpRequestMethod.POST ? true:false;
    }

    public boolean isGetMethod(){
        return this.getRequestMethod() == HttpRequestMethod.GET ? true:false;
    }
}
