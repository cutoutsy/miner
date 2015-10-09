package miner.proxy;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

/**
 * Created by white on 15/9/30.
 */
public class ExclaimBasicTopo {

    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("spout", new RandomSpout());
        builder.setBolt("exclaim", new ProxyBolt()).shuffleGrouping("spout");
        builder.setBolt("print", new PrintBolt()).shuffleGrouping("exclaim");

        Config conf = new Config();
        conf.setDebug(false);

        /* Config里封装了Redis的配置 */
        conf.put("ip","127.0.0.1");
        conf.put("port","6379");
        conf.put("password","password");

        if (args != null && args.length > 0) {
            conf.setNumWorkers(1);

            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        } else {

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("test", conf, builder.createTopology());
            Utils.sleep(10*1000);
            cluster.killTopology("test");
            cluster.shutdown();
        }
    }
}
