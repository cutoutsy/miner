package miner.topo;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import miner.spider.httpclient.Crawl4HttpClient;

import java.util.Map;
import java.util.Random;

public class EmitMessageSpout extends BaseRichSpout {
    SpoutOutputCollector _collector;
    Random _rand;


    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _collector = collector;
        _rand = new Random();
    }

    public void nextTuple() {
        Utils.sleep(1000);
        for(int i = 22; i<= 30; i++) {
            Utils.sleep(1000);
            String url = "https://account.wandoujia.com/v4/api/simple/profile?uid="+String.valueOf(i);
            String message = Crawl4HttpClient.downLoadPage(url);
//        System.out.println(message);
            _collector.emit(new Values(message));
        }
    }

    public void ack(Object id) {
    }

    public void fail(Object id) {
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }

}
