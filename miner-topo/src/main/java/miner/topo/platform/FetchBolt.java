package miner.topo.platform;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import miner.spider.httpclient.Crawl4HttpClient;
import miner.spider.utils.MyLogger;


import java.util.Map;

/**
 * The Bolt for downloading page
 *
 * Created by cutoutsy on 8/5/15.
 */
public class FetchBolt extends BaseRichBolt {

    private static MyLogger logger = new MyLogger(FetchBolt.class);

    private OutputCollector _collector;

    public void execute(Tuple input) {
        String downloadUrl = "";
        try{
            String globalInfo = input.getString(0);
            downloadUrl = input.getString(1);
            System.out.println("downloadurl:"+downloadUrl);
            String resource = Crawl4HttpClient.downLoadPage(downloadUrl);
//            System.out.println("resource:"+resource);
            _collector.emit(new Values(globalInfo, resource));
            _collector.ack(input);
        } catch (Exception ex) {
            logger.error("Download page:" +downloadUrl+" error!"+ex);
        }

    }

    public void prepare(Map conf,TopologyContext context,OutputCollector collector){
        this._collector = collector;
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("f_globalinfo","f_resource"));
    }
}
