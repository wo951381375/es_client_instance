package es.config;

import es.utils.SysDataRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ESInstance {

        static Logger log = LoggerFactory.getLogger(ESInstance.class);

        public static TransportClient esClient() {
                return InstanceHolder.client;
        }



        private static class InstanceHolder{
                /**
                 * 配置 sysconf.properties 对应集群节点信息
                 *
                 * */
                private static String CLUSTER_NAME = SysDataRequest.getInstance().getString("elasticsearch_cluster-name");

                private static String CLUSTER_NODES = SysDataRequest.getInstance().getString("elasticsearch_cluster-nodes");

                private static Integer CLUSTER_PORT = SysDataRequest.getInstance().getInt("elasticsearch_cluster-port");

                private static TransportClient client;

                static {
                        client = initClient();
                }

                private static TransportClient initClient() {
                        if (client != null){
                                return client;
                        }
                        //指定ES集群
                        Settings settings = Settings.builder().put("cluster.name", CLUSTER_NAME).build();
                        //创建访问es服务器的客户端
                        TransportClient client = null;
                        try {
                                log.info("ES ready ---------> "+CLUSTER_NAME+"---"+CLUSTER_NODES);
                                String[] nodes = CLUSTER_NODES.split(",");
                                client = new PreBuiltTransportClient(settings);
                                for (String node : nodes) {
                                        client.addTransportAddress(new TransportAddress(InetAddress.getByName(node), CLUSTER_PORT));
                                }
                                log.info("ES start ---------> "+CLUSTER_NAME+"---"+CLUSTER_NODES);
                        } catch (UnknownHostException e) {
                                e.printStackTrace();
                        }
                        return client;
                }
        }


}
