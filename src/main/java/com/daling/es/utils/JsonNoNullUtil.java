package com.daling.es.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class JsonNoNullUtil {

        public static final Logger log = LoggerFactory.getLogger(JsonNoNullUtil.class);

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
                        log.error("error",e);
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
                        log.error("error",e);
                }
                return null;
        }

        public static String transFormationJson(Object obj){
                if (obj == null){
                        return null;
                }
                try {
                        return mapper.writeValueAsString(obj);
                } catch (JsonProcessingException e) {
                        log.error("error",e);
                }
                return null;
        }

        public static <T extends Object> List<T> jsonToList(String json, Class<T> bean){
                if (StringUtils.isBlank(json)) {
                        return null;
                }
                try {
                        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, bean);
                        return mapper.readValue(json, javaType);
                } catch (IOException e) {
                        log.error("error",e);
                }
                return null;
        }

}
