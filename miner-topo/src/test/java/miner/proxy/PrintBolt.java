package miner.proxy;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

/**
 * Created by white on 15/9/30.
 */
public class PrintBolt extends BaseBasicBolt {

    public void execute(Tuple tuple, BasicOutputCollector collector) {
        String rec = tuple.getString(0)+" "+tuple.getString(1)+" "+tuple.getString(2);
        System.out.println("String recieved: " + rec);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // do nothing
    }
}
