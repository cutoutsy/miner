package miner.topo.platform;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import miner.topo.bolt.*;
import miner.topo.spout.BeginSpout;

public class TopologyMain {

	public static void main(String[] args) {
		try{
			TopologyBuilder topologyBuilder = new TopologyBuilder();

			topologyBuilder.setSpout("beginspout", new BeginSpout(), 1).setMaxSpoutPending(500);//1,500

			topologyBuilder.setBolt("generateurl", new GenerateUrlBolt(), 1)//2
					.shuffleGrouping("beginspout");
			topologyBuilder.setBolt("generateurl-loop-bolt", new GenerateUrlBolt(), 1)//2
					.shuffleGrouping("parse", "generate-loop");

			topologyBuilder.setBolt("proxy", new ProxyBolt(), 1)//2
					.shuffleGrouping("generateurl")
					.shuffleGrouping("generateurl-loop-bolt");

			topologyBuilder.setBolt("fetch", new FetchBolt(), 1)//10
					.shuffleGrouping("proxy");

			topologyBuilder.setBolt("parse", new ParseBolt(), 1)//10
					.shuffleGrouping("fetch");


			topologyBuilder.setBolt("store", new StoreBolt(), 1)//5
					.shuffleGrouping("parse", "store");
			
			Config config = new Config();
			config.setDebug(false);
			//config.setMaxSpoutPending(2000);
			
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
