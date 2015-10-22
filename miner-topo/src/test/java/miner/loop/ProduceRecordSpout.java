package miner.loop;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by cutoutsy on 8/21/15.
 */
public class ProduceRecordSpout extends BaseRichSpout {

    private static final long serialVersionUID = 1L;
    private SpoutOutputCollector collector;
    private Random rand;
    private String[] recordLines;
    private String type;

    public ProduceRecordSpout(String type, String[] lines){
        this.type = type;
        recordLines = lines;
    }

    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector){
        this.collector = collector;
        rand = new Random();
    }

    public void nextTuple(){
        Utils.sleep(500);
        String record = recordLines[rand.nextInt(recordLines.length)];
        List<Object> values = new Values(type, record);
        collector.emit(values, values);
        System.out.println("Record emitted: type=" + type + ", record=" + record);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer){
        declarer.declare(new Fields("type", "record"));
    }
}
