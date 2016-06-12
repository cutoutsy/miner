package miner.topo.platform;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import miner.topo.bolt.*;
import miner.topo.spout.BeginSpout;
import miner.topo.spout.LoopSpout;
import miner.utils.PlatformParas;

public class TopologyMain {

	public static void main(String[] args) {
		try{
			TopologyBuilder topologyBuilder = new TopologyBuilder();

			topologyBuilder.setSpout("beginspout", new BeginSpout(), PlatformParas.begin_spout_num).setMaxSpoutPending(200);//1,500
			topologyBuilder.setSpout("loopspout", new LoopSpout(), PlatformParas.loop_spout_num).setMaxSpoutPending(200);

			topologyBuilder.setBolt("generateurl", new GenerateUrlBolt(), PlatformParas.generateurl_bolt_num)//2
					.shuffleGrouping("beginspout")
					.shuffleGrouping("loopspout");
			topologyBuilder.setBolt("generateurl-loop-bolt", new GenerateUrlBolt(), PlatformParas.generateurl_bolt_num)
					.shuffleGrouping("parse", "generate-loop");

			topologyBuilder.setBolt("proxy", new ProxyBolt(), PlatformParas.proxy_bolt_num)
					.shuffleGrouping("generateurl")
					.shuffleGrouping("generateurl-loop-bolt");

			topologyBuilder.setBolt("fetch", new FetchBolt(), PlatformParas.fetch_bolt_num)
					.shuffleGrouping("proxy");

			topologyBuilder.setBolt("parse", new ParseBolt(), PlatformParas.parse_bolt_num)
					.shuffleGrouping("fetch");

			topologyBuilder.setBolt("store", new StoreBolt(), PlatformParas.store_bolt_num)
					.shuffleGrouping("parse", "store");
			
			Config config = new Config();
			config.setDebug(false);
			//default:30s
			config.setMessageTimeoutSecs(PlatformParas.message_timeout_secs);
			//config.setMaxSpoutPending(2000);
			
			if(args != null && args.length>0){
				config.setNumWorkers(PlatformParas.work_num);
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
