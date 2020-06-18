package com.daling.es.config;

import com.daling.inner.enums.IndexEnum;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author daling-guoyingjie
 * @create 2019-12-03 10:39
 * @desc
 **/
public class ClientFactory {

    private static class SingletonHolder {
        private static ClientFactory instance = new ClientFactory();
    }

    private ClientFactory() {
    }

    public static ClientFactory getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 新商品中心，旧商品中心，品牌信息存放在同一个索引集群
     *
     * 其余放在cec索引集群中
     * @param index
     * @return
     */
    public TransportClient getTransportClient(String index) {
        if (IndexEnum.Goods.getIndex().equals(index) || IndexEnum.OldGoods.getIndex().equals(index) || IndexEnum.Brand.getIndex().equals(index)) {
            return ESInstance.esClient();
        } else {
            return TicketEsClient.buildEsClient();
        }
    }


}
