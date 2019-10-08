package com.daling.es.handler;

import com.daling.es.result.ESResult;
import com.daling.es.utils.JsonNoNullUtil;
import es.exception.GenericBusinessException;
import com.google.common.collect.Lists;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.transport.TransportClient;

import java.util.List;

public class QueryDocumentHandler<T> extends ESBaseHandler {

        private List<T> data;

        private Class<T> clazz;

        private List<String> documentIds;

        public QueryDocumentHandler(TransportClient client) {
                this.client = client;
        }

        public ESResult execute() throws GenericBusinessException {
                validate();
                doExecute();
                return new ESResult(retBool,count.get(),data);
        }

        private void doExecute() {
                if (documentIds == null || documentIds.isEmpty()){
                        return;
                }
                MultiGetResponse response = client.prepareMultiGet().add(INDEX, INDEX_TYPE, documentIds).get();
                // 获取返回值
                this.getResponseAsResult(response);
                this.retBool = true;
        }

        private void getResponseAsResult(MultiGetResponse response) {
                if (response == null || response.getResponses() == null || response.getResponses().length == 0){
                        return;
                }
                this.data = this.getMultiToResponse(response.getResponses());
        }

        private List<T> getMultiToResponse(MultiGetItemResponse[] responses) {
                List<T> data = Lists.newArrayList();
                for(MultiGetItemResponse res : responses){
                        if (!res.isFailed() && res.getResponse() != null && res.getResponse().getSourceAsString() != null){
                                T t = JsonNoNullUtil.transFormationMapNoNullVal(res.getResponse().getSourceAsString(), clazz);
                                data.add(t);
                                this.count.getAndAdd(1);
                        }
                }
                return data;
        }


        public QueryDocumentHandler setINDEX(String INDEX) {
                this.INDEX = INDEX;
                return this;
        }

        public QueryDocumentHandler setINDEX_TYPE(String INDEX_TYPE) {
                this.INDEX_TYPE = INDEX_TYPE;
                return this;
        }

        public QueryDocumentHandler setClazz(Class<T> clazz) {
                this.clazz = clazz;
                return this;
        }

        public QueryDocumentHandler setDocumentIds(List<String> documentIds) {
                this.documentIds = documentIds;
                return this;
        }

        public List<T> getData() {
                return data;
        }

        @Override
        public Boolean validate() throws GenericBusinessException {
                if (clazz == null){
                        throw new GenericBusinessException("clazz IS NULL");
                }
                if (documentIds == null || documentIds.isEmpty()){
                        throw new GenericBusinessException("documentIds IS NULL");
                }
                return super.validate();
        }
}
