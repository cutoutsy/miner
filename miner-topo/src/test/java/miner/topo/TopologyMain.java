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

			topologyBuilder.setBolt("generate", new GenerateUrlBolt(), 1)
					.shuffleGrouping("Spout");
			topologyBuilder.setBolt("generate_loop", new GenerateUrlBolt(), 1)
					.shuffleGrouping("Parse", "loop");

//			topologyBuilder.setBolt("Parse", new ParseTestBolt(), 1).shuffleGrouping("Spout");
			topologyBuilder.setBolt("Parse", new ParseLoopBolt(), 1)
					.shuffleGrouping("generate")
					.shuffleGrouping("generate_loop");

			topologyBuilder.setBolt("Store", new StoreTestBolt(), 1)
					.shuffleGrouping("Parse", "store");
			
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
