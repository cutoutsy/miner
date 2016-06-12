package miner.loop;

import backtype.storm.generated.GlobalStreamId;
import backtype.storm.generated.Grouping;
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
public class SaveDataBolt extends BaseRichBolt{

    private static final long serivalVersionUID = 1L;
    private OutputCollector collector;
    private String type;

    public SaveDataBolt(String type){
        this.type = type;
    }

    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector){
        this.collector = collector;
    }

    public void execute(Tuple input){
        System.out.println("["+type+"]"+
        "SourceComponent="+input.getSourceComponent()+
        ", SourceStreamId="+input.getSourceStreamId()+
        ", type="+input.getString(0)+
        ", value="+input.getString(1));

        collector.ack(input);
    }


    public void declareOutputFields(OutputFieldsDeclarer declarer){
    }

}
