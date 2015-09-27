package miner.topo.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

import java.util.Map;

/**
 * Created by white on 15/9/24.
 */
public class ProxyBolt extends BaseRichBolt {
    private OutputCollector _collector;

    public void execute(Tuple input) {
        try {
            String globalInfo = input.getString(0);
            String download_url = input.getString(1);
        }catch(Exception e){
            e.printStackTrace();

        }
    }

    public void prepare(Map conf,TopologyContext context,OutputCollector collector){
        this._collector = collector;
    }
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("f_globalinfo","f_resource"));
    }
}
