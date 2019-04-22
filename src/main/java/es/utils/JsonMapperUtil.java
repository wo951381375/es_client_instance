package es.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.BuilderClient;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.transport.TransportClient;

import java.io.IOException;

public class JsonMapperUtil {


        private static final ObjectMapper mapper = new ObjectMapper();

        static {
                // 空值或""不参加序列化
                mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
                // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }

        public static <T> T transFormationMapNoNullVal(Object obj, Class<T> cls){

                //获取json
                String json = transFormationJson(obj);
                //序列化对象
                if (StringUtils.isBlank(json) || "null".equals(json)){
                        return null;
                }
                try {
                        return mapper.readValue(json, cls);
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return null;
        }

        public static  <T> T transFormationMapNoNullVal(String json, Class<T> clazz){

                //序列化对象
                if (StringUtils.isBlank(json) || "null".equals(json)){
                        return null;
                }
                try {
                        return (T) mapper.readValue(json, clazz);
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return null;
        }

        public static String transFormationJson(Object obj){
                try {
                        return mapper.writeValueAsString(obj);
                } catch (JsonProcessingException e) {
                        e.printStackTrace();
                }
                return "";
        }

}
