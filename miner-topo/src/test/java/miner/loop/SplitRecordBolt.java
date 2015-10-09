package miner.loop;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * Created by cutoutsy on 8/21/15.
 */
public class SplitRecordBolt extends BaseRichBolt{

    private static final long serivalVersionUID = 1L;
    private OutputCollector collector;

    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector){
        this.collector = collector;
    }

    public void execute(Tuple input){
        String type = input.getString(0);
        String line = input.getString(1);
        if(line != null && !line.trim().isEmpty()){
            for(String word:line.split("\\s+")){
                collector.emit(input, new Values(type, word));
                System.out.println("Word emitted: type="+type+",word="+word);
                collector.ack(input);
            }
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer){
        declarer.declare(new Fields("type", "word"));
    }

}
