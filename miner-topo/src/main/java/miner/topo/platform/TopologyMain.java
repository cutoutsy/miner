package miner.topo.platform;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import miner.topo.bolt.FetchBolt;
import miner.topo.bolt.GenerateUrlBolt;
import miner.topo.bolt.ParseBolt;
import miner.topo.bolt.StoreBolt;
import miner.topo.spout.BeginSpout;

public class TopologyMain {

	public static void main(String[] args) {
		try{
			TopologyBuilder topologyBuilder = new TopologyBuilder();

			topologyBuilder.setSpout("beginspout", new BeginSpout(), 2);

			topologyBuilder.setBolt("generateurl", new GenerateUrlBolt(), 2)
					.shuffleGrouping("beginspout");
			topologyBuilder.setBolt("generateurl-loop-bolt", new GenerateUrlBolt(), 2)
					.shuffleGrouping("parse", "generate-loop");

			topologyBuilder.setBolt("fetch", new FetchBolt(), 10)
					.shuffleGrouping("generateurl")
					.shuffleGrouping("generateurl-loop-bolt");

			topologyBuilder.setBolt("parse", new ParseBolt(), 10)
					.shuffleGrouping("fetch");

			topologyBuilder.setBolt("store", new StoreBolt(), 5)
					.shuffleGrouping("parse", "store");
			
			Config config = new Config();
			config.setDebug(false);
			
			if(args != null && args.length>0){
				config.setNumWorkers(20);
				StormSubmitter.submitTopology(args[0], config, topologyBuilder.createTopology());
			}else{
				config.setMaxTaskParallelism(2);
				LocalCluster cluster = new LocalCluster();
				cluster.submitTopology("test", config, topologyBuilder.createTopology());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
