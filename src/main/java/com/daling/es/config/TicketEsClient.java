package com.daling.es.config;

import com.daling.es.utils.SysDataRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author daling-guoyingjie
 * @create 2019-12-03 18:14
 * @desc
 **/
public class TicketEsClient {

    static Logger log = LoggerFactory.getLogger(ESInstance.class);

    public static TransportClient buildEsClient() {
        return TicketEsClient.InstanceHolder.client;
    }


    private static class InstanceHolder{
        /**
         * 配置 sysconf.properties 对应集群节点信息
         *
         * */
        private static final String TICKET_CLUSTER_NAME = SysDataRequest.getInstance().getString("elasticsearch_ticket_cluster-name");

        private static final String TICKET_CLUSTER_NODES = SysDataRequest.getInstance().getString("elasticsearch_ticket_cluster-nodes");

        private static final Integer TICKET_CLUSTER_PORT = SysDataRequest.getInstance().getInt("elasticsearch_ticket_cluster-port");

        private static TransportClient client;

        static {
            client = initClient();
        }

        private static TransportClient initClient() {
            if (client != null){
                return client;
            }
            //指定ES集群
            Settings settings = Settings.builder().put("cluster.name", TICKET_CLUSTER_NAME).build();
            //创建访问es服务器的客户端
            TransportClient client = null;
            try {
                log.info("ES ticket ready ---------> "+TICKET_CLUSTER_NAME+"---"+TICKET_CLUSTER_NODES);
                String[] nodes = TICKET_CLUSTER_NODES.split("&");
                client = new PreBuiltTransportClient(settings);
                for (String node : nodes) {
                    client.addTransportAddress(new TransportAddress(InetAddress.getByName(node), TICKET_CLUSTER_PORT));
                }
                log.info("ES ticket start ---------> "+TICKET_CLUSTER_NAME+"---"+TICKET_CLUSTER_NODES);
            } catch (UnknownHostException e) {
                log.error("ticket error ={}",e);
            }
            return client;
        }
    }
}
