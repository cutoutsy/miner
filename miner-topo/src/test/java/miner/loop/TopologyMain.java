package miner.loop;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import miner.topo.EmitMessageSpout;
import miner.topo.ParseTestBolt;
import miner.topo.StoreTestBolt;

public class TopologyMain {

	public static void main(String[] args) {
		try{
			TopologyBuilder topologyBuilder = new TopologyBuilder();
			topologyBuilder.setSpout("spout-number", new ProduceRecordSpout(Type.NUMBER, new String[]{"111 222 333", "80966 31"}), 1);
			topologyBuilder.setSpout("spout-string", new ProduceRecordSpout(Type.STRING, new String[]{"abc ddd fasko", "hello the world"}), 1);
			topologyBuilder.setSpout("spout-sign", new ProduceRecordSpout(Type.SIGN, new String[]{"++ -*% *** @@", "{+-} ^#######"}), 1);

			topologyBuilder.setBolt("bolt-splitter", new SplitRecordBolt(), 2)
					.shuffleGrouping("spout-number")
					.shuffleGrouping("spout-string")
					.shuffleGrouping("spout-sign");

			topologyBuilder.setBolt("bolt-distributor", new DistributeWordByTypeBolt(), 1)
					.fieldsGrouping("bolt-splitter", new Fields("type"));

			topologyBuilder.setBolt("bolt-number-saver", new SaveDataBolt(Type.NUMBER), 1)
					.shuffleGrouping("bolt-distributor", "stream-number-saver");
			topologyBuilder.setBolt("bolt-string-saver", new SaveDataBolt(Type.STRING), 1)
					.shuffleGrouping("bolt-distributor", "stream-string-saver");
			topologyBuilder.setBolt("bolt-sign-saver", new SaveDataBolt(Type.SIGN), 1)
					.shuffleGrouping("bolt-distributor", "stream-sign-saver");

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
