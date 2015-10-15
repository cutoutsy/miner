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

    public void execute(Tuple input) {
        int i = 3;
        String downloadUrl = "";
        try{
            String globalInfo = input.getString(0);
            downloadUrl = input.getString(1);
            String proxy = input.getString(2);
//            logger.info("downloadurl:"+downloadUrl);
//            System.err.println(globalInfo+"=="+downloadUrl+"=="+proxy);
            String resource = "";
            while(i > 0) {
                resource = Crawl4HttpClient.downLoadPage(downloadUrl, proxy);
                if(!resource.equals("")){
                    logger.info(proxy+"--request succeed!");
                    break;
                }
                i--;
            }
            if(!resource.equals("")) {
                _collector.emit(new Values(globalInfo, resource));
                _collector.ack(input);
            }else{
                logger.warn(downloadUrl + " return null");
            }
        } catch (Exception ex) {
            logger.error("Download page:" +downloadUrl+" error!"+ex);
            _collector.fail(input);
        }
    }

    public void prepare(Map conf,TopologyContext context,OutputCollector collector){
        this._collector = collector;
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("f_globalinfo","f_resource"));
    }
}
