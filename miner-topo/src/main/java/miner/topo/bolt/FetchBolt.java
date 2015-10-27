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

        String globalInfo = tuple.getString(0);
        String downloadUrl = tuple.getString(1);
        String proxy = tuple.getString(2);

        try{
            String resource = Crawl4HttpClient.downLoadPage(downloadUrl, proxy);

            if(!resource.equals("")) {
                _collector.emit(tuple, new Values(globalInfo, resource));
                logger.info(downloadUrl+":fetch succeed!");
                _collector.ack(tuple);
            }else{
                logger.warn(downloadUrl + " return null");
                _collector.fail(tuple);
            }
        } catch (Exception ex) {
            logger.error("Download page:" +downloadUrl+" error!"+ex);
            _collector.fail(tuple);
        }
    }

    public void prepare(Map conf,TopologyContext context,OutputCollector collector){
        this._collector = collector;
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("f_globalinfo","f_resource"));
    }
}
