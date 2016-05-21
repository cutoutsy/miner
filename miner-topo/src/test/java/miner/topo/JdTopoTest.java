package miner.topo;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import jd.Jdong;
import miner.bolt.StoreBoltTest;
import miner.topo.bolt.FetchBolt;
import miner.topo.bolt.ParseBolt;
import miner.topo.bolt.ProxyBolt;
import miner.topo.spout.BeginSpout;
import miner.topo.spout.LoopSpout;
import miner.utils.PlatformParas;

/**
 * 麦轮胎测试拓扑文件
 */
public class JdTopoTest {


    public static void main(String[] args){
        Jdong.runJdPrepare();
        try{
            TopologyBuilder topologyBuilder = new TopologyBuilder();

            topologyBuilder.setSpout("beginspout", new BeginSpout(), 1).setMaxSpoutPending(200);//1,500
            topologyBuilder.setSpout("loopspout", new LoopSpout(), 1).setMaxSpoutPending(200);

            topologyBuilder.setBolt("generateurl", new miner.topo.bolt.GenerateUrlBolt(), 1)//2
                    .shuffleGrouping("beginspout")
                    .shuffleGrouping("loopspout");
            topologyBuilder.setBolt("generateurl-loop-bolt", new miner.topo.bolt.GenerateUrlBolt(), 1)
                    .shuffleGrouping("parse", "generate-loop");

            topologyBuilder.setBolt("proxy", new ProxyBolt(), 1)
                    .shuffleGrouping("generateurl")
                    .shuffleGrouping("generateurl-loop-bolt");

            topologyBuilder.setBolt("fetch", new FetchBolt(), 2)
                    .shuffleGrouping("proxy");

            topologyBuilder.setBolt("parse", new ParseBolt(), 1)
                    .shuffleGrouping("fetch");

            topologyBuilder.setBolt("store", new StoreBoltTest(), 1)
                    .shuffleGrouping("parse", "store");

            Config config = new Config();
            config.setDebug(false);
            //default:30s
            config.setMessageTimeoutSecs(PlatformParas.message_timeout_secs);
            //config.setMaxSpoutPending(2000);

            //测试采用本地模式
            config.setMaxTaskParallelism(4);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("test", config, topologyBuilder.createTopology());

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
