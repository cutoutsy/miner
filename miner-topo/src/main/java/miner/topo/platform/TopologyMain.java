package miner.topo.platform;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;

public class TopologyMain {

	public static void main(String[] args) {
		try{
			TopologyBuilder topologyBuilder = new TopologyBuilder();
			topologyBuilder.setSpout("BeginSpout", new BeginSpout(), 1);
			topologyBuilder.setBolt("GenerateUrl", new GenerateUrlBolt(), 1).shuffleGrouping("BeginSpout");
			topologyBuilder.setBolt("Fetch", new FetchBolt(), 2).shuffleGrouping("GenerateUrl");
			topologyBuilder.setBolt("Parse", new ParseBolt(), 1).shuffleGrouping("Fetch");
			topologyBuilder.setBolt("Store", new StoreBolt(), 1).shuffleGrouping("Parse");
			
			Config config = new Config();
			config.setDebug(false);
			
			if(args != null && args.length>0){
				config.setNumWorkers(4);
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
