package miner.proxy;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class RandomSpout extends BaseRichSpout {

    private SpoutOutputCollector collector;

    private Random rand;

    private static String[] global_info = new String[] {"1-1-1-1", "2-2-1-1", "3-2-1-1", "4-1-2-1", "1-2-1-2","3-3-1-3"};
    private static String[] download_url=new String[]{"baidu.com","qq.com","sohu.com","taobao.com","z.cn","Jd.com"};

    public void open(Map conf, TopologyContext context,
                     SpoutOutputCollector collector) {
        this.collector = collector;
        this.rand = new Random();
    }

    public void nextTuple() {

        int random_number = rand.nextInt(global_info.length);
        String value0 = global_info[random_number];
        String value1 = download_url[random_number];
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//                .format(new Date())+" Emit: "+ value0+" "+value1);
        this.collector.emit(new Values(value0,value1));
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("global_info","download_url"));
    }

}