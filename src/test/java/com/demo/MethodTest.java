package java.com.demo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * 使用java API操作elasticSearch
 * 
 * @author 231
 *
 */
public class MethodTest {
	private TransportClient client = null;
	
	@Before
	public void initClient() {
		//指定ES集群
		Settings settings = Settings.builder().put("cluster.name", "daling-b").build();
		//创建访问es服务器的客户端
		try {
			client = new PreBuiltTransportClient(settings)
									.addTransportAddresses(new TransportAddress(InetAddress.getByName("10.14.113.67"),9300));

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@After
	public void closeClient() {
//		client.close();
	}
	
	@Test
	public void put() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", "2");
		map.put("title", "书包");
		map.put("content", "背包小浣熊正版");
		Set<String> keySet = map.keySet();
		XContentBuilder builder = null;
		try {
			builder = XContentFactory.jsonBuilder().startObject();
			for (String key : keySet) {
				builder.field(key, map.get(key));
			}
			builder.endObject();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			IndexResponse response = client.prepareIndex("center", "shop", "4")
										.setSource(builder).get();
			System.out.println(response.status());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	


}