package miner.topo.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import miner.spider.httpclient.Crawl4HttpClient;
import miner.utils.MySysLogger;

import java.util.Map;

/**
 * The Bolt for downloading page
 */
public class FetchBolt extends BaseRichBolt {

    private static MySysLogger logger = new MySysLogger(FetchBolt.class);
    private OutputCollector _collector;

    public void execute(Tuple tuple) {
        long startTime=System.currentTimeMillis();

        String globalInfo = tuple.getString(0);
        String downloadUrl = tuple.getString(1);
        String proxy = tuple.getString(2);
        String resource = "";
        try{
            if(proxy.equals("none")){
                //不加代理请求
                resource = Crawl4HttpClient.downLoadPage(downloadUrl);
            }else {
                //加上代理请求
                resource = Crawl4HttpClient.downLoadPage(downloadUrl, proxy);
            }
            if (resource.equals("exception")) {
                logger.error("fetch exception:" + downloadUrl);
                _collector.fail(tuple);
            } else if(resource.equals("error")){
                logger.error("fetch error:" + downloadUrl);
                _collector.fail(tuple);
                //返回值一般不会为空
            }else if(resource.equals("") || resource == null){
                logger.warn(downloadUrl + "return null.");
                _collector.fail(tuple);
            } else {
                _collector.emit(tuple, new Values(globalInfo, resource));
                logger.info(downloadUrl + ":fetch succeed!" + resource);
                _collector.ack(tuple);
            }
        } catch (Exception ex) {
            logger.error("fetch error:" +downloadUrl+" error:"+MySysLogger.formatException(ex));
            _collector.fail(tuple);
        }

        long endTime=System.currentTimeMillis();
        logger.info(globalInfo+"在FetchBolt的处理时间:"+(endTime-startTime)/1000+"s.");

    }

    public void prepare(Map conf,TopologyContext context,OutputCollector collector){
        this._collector = collector;
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("f_globalinfo","f_resource"));
    }
}
