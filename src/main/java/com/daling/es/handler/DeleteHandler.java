package com.daling.es.handler;

import com.daling.es.result.ESResult;
import es.exception.GenericBusinessException;
import com.google.common.collect.Lists;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DeleteHandler<T> extends ESBaseHandler {

        private List<String> documentIds;

        private List<String> error = Lists.newArrayList();

        public DeleteHandler(TransportClient client) {
                this.client = client;
        }

        public ESResult execute() throws GenericBusinessException {
                validate();
                doExecute();
                return new ESResult(retBool,error);
        }

        private void doExecute() {
                if (documentIds == null || documentIds.isEmpty()){
                        return;
                }
                BulkResponse response = this.getDeleteResponse();
                if (Objects.isNull(response)){
                        this.error = documentIds;
                        return;
                }
                BulkItemResponse[] items = response.getItems();
                List<BulkItemResponse> bulkList = Arrays.asList(items);
                List<String> collect = bulkList.stream().filter(a -> a.isFailed()).map(a -> a.getId()).collect(Collectors.toList());
                error.addAll(collect);
                this.retBool = true;
        }

        private BulkResponse getDeleteResponse() {
                BulkRequestBuilder bulkRequest = client.prepareBulk();
                for (String documentId : documentIds) {
                        DeleteRequestBuilder builder = client.prepareDelete(INDEX, INDEX_TYPE, documentId);
                        bulkRequest.add(builder);
                }
                BulkResponse response = bulkRequest.get();
                return response;
        }


        public DeleteHandler setINDEX(String INDEX) {
                this.INDEX = INDEX;
                return this;
        }

        public DeleteHandler setINDEX_TYPE(String INDEX_TYPE) {
                this.INDEX_TYPE = INDEX_TYPE;
                return this;
        }

        public DeleteHandler setDocumentIds(List<String> documentIds) {
                this.documentIds = documentIds;
                return this;
        }

        @Override
        public Boolean validate() throws GenericBusinessException {
                if (documentIds == null || documentIds.isEmpty()){
                        throw new GenericBusinessException("documentIds IS NULL");
                }
                return super.validate();
        }
}
