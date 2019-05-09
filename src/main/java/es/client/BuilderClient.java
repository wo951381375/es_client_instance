package es.client;

import es.config.ESInstance;
import es.handler.*;
import org.elasticsearch.client.transport.TransportClient;

public class BuilderClient {

        /**
         * 获取client
         * */
        private static TransportClient client = ESInstance.esClient();

        private BuilderClient() {
        }

        /**
         * 实用注意:
         * 1. 自定义查询方式, 只针对中文类型使用
         * 2. 前缀为 un_ , 查询条件为非查询, field 取 un_ 之后
         * 3. 自定义排序方式, 默认使用 id 字段排序
         * 4. 查询下标注意: size 最大不超过 10000, 超过则使用游标查询方式 --> 未实现
         * 5. 查询实体类增加类注解:@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class), 统一驼峰命名规范
         * 6. 对于特殊要求字段增加字段注解: @JsonProperty("")       package com.fasterxml.jackson.annotation.JsonProperty;, 注意: 入参json与返回值 json 会相应变化
         * 7. 默认查询方式不满足, 可重写 查询条件 PrepareQuery.getBoolQueryBuilder(obj,querEnum)
         * Param:{
         *         client : 客户端实例       必填
         *         INDEX : 真实索引索引       必填
         *         INDEXTYPE : 索引类型       必填
         *         clazz : 返回值类型       必填
         *         search : 查询实体类       必填
         *         queryEnum : 自定义中文字段查询方式
         *         ProcessorServiceImpl : 自定义查询方式 查询条件
         *         sortField : 自定义排序字段
         *         from : 开始下标
         *         size : 展示数量
         * }
         *
         * */
        public QueryFieldHandler queryFieldHandler(String index){
                return new QueryFieldHandler(client).setINDEX(index);
        }


        /**
         * 使用注意:
         * Params:{
         *         INDEX : 真实索引索引       必填
         *         INDEXTYPE : 索引类型      必填
         *         clazz: 返回值类型         必填
         *         documentIds : 索引文档Id         必填
         * }
         *
         * */
        public QueryDocumentHandler queryDocumentHandler(String index){
                return new QueryDocumentHandler(client).setINDEX(index);
        }

        /**
         * 使用注意:
         * 1.实体类使用驼峰命名, 存储 ES 结构中会自动转换为 下划线
         * 2.实体类增加注解: @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class), 对应操作 1 使用
         * 3.外部注入 client
         * 4.存储索引不可使用别名, 使用真实索引
         * 5.documentId 为唯一条件, 重复会被覆盖
         * 6.此操作为批量操作, 单词执行最大为 5000, 如果数据量过大, 可自定义增加线程, 默认为 1
         * 7. 存储特殊字段时,序列化使用使用 @JsonProperty("")       package com.fasterxml.jackson.annotation.JsonProperty;, 注意: 入参json与返回值 json 会相应变化
         *
         * Params:{
         *         INDEX : 真实索引索引       必填
         *         INDEXTYPE : 索引类型       必填
         *         DOCUMENTID : 存储数据的唯一值       必填
         *         param : 存储数据       必填
         *         THREAD_COUNT : 存储数据量大于 5000 时, 可适当调整, 默认 1
         *         timeout : 响应超时时间, 单位(秒), 默认 60s
         *         coverYn : 是否空值覆盖     默认 false
         * }
         *
         * */
        public BulkHandler bulkHandler(String index){
                return new BulkHandler(client).setINDEX(index);
        }

        /**
         * 使用注意:
         * Params:{
         *         INDEX : 真实索引索引       必填
         *         INDEXTYPE : 索引类型      必填
         *         documentIds : 索引文档Id         必填
         * }
         *
         * */
        public DeleteHandler deleteHandler(String index){
                return new DeleteHandler(client).setINDEX(index);
        }

        /**
         * 谨慎使用, 清空索引内所有数据
         * */
        public EmptyHandler emptyHandler(String index){
                return new EmptyHandler(client).setINDEX(index);
        }

        /**
         *
         * */
        public UpdateHandler updateHandler(String index){
                return new UpdateHandler(client).setINDEX(index);
        }

        public TransportClient getClient() {
                return client;
        }

        /**
         * 私有化 唯一入口
         * */
        public static BuilderClient getInstance() {
                return BuilderClientHolder.builderClient;
        }

        private static class BuilderClientHolder{
                private static BuilderClient builderClient = new BuilderClient();

                public BuilderClientHolder() {
                }
        }

}

