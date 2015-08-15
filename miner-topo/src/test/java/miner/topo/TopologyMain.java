package miner.topo;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;

public class TopologyMain {

	public static void main(String[] args) {
		try{
			TopologyBuilder topologyBuilder = new TopologyBuilder();
			topologyBuilder.setSpout("Spout", new EmitMessageSpout(), 1);
			topologyBuilder.setBolt("Parse", new ParseBolt(), 1).shuffleGrouping("Spout");
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
