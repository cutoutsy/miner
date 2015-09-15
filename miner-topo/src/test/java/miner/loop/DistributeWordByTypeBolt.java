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
public class DistributeWordByTypeBolt extends BaseRichBolt{

    private static final long serivalVersionUID = 1L;
    private OutputCollector collector;

    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector){
        this.collector = collector;
        Map<GlobalStreamId, Grouping> sources = context.getThisSources();
        System.out.println("sources====>"+sources);
    }

    public void execute(Tuple input){
        String type = input.getString(0);
        String word = input.getString(1);
//        switch(type) {
//            case Type.NUMBER:
//                emit("stream-number-saver", type, input, word);
//                break;
//            case Type.STRING:
//                emit("stream-string-saver", type, input, word);
//                break;
//            case Type.SIGN:
//                emit("stream-sign-saver", type, input, word);
//                break;
//            default:
//                emit("stream-discarder", type, input, word);
//        }

        if(type.equals(Type.NUMBER)){
            emit("stream-number-saver", type, input, word);
        }else if(type.equals(Type.STRING)){
            emit("stream-string-saver", type, input, word);
        }else if(type.equals(Type.SIGN)){
            emit("stream-sign-saver", type, input, word);
        }else{
            emit("stream-discarder", type, input, word);
        }
        collector.ack(input);
    }

    private void emit(String streamId, String type, Tuple input, String word){
        collector.emit(streamId, input, new Values(type, word));
        System.out.println("Distribution, typed word emitted: type="+type+", word="+word);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer){
//        declarer.declare(new Fields("type", "word"));
        declarer.declareStream("stream-number-saver", new Fields("type", "word"));
        declarer.declareStream("stream-string-saver", new Fields("type", "word"));
        declarer.declareStream("stream-sign-saver", new Fields("type", "word"));
    }

}
