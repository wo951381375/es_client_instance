package com.daling.es.handler;

import com.daling.es.enums.QueryEnum;
import com.daling.es.perpare.PrepareQuery;
import com.daling.es.perpare.PrepareQueryImpl;
import com.daling.es.result.ESResult;
import com.daling.es.utils.JsonNoNullUtil;
import es.exception.GenericBusinessException;
import com.google.common.collect.Lists;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;
import java.util.Objects;

public class QueryFieldHandler<T> extends ESBaseHandler {

        private int from = 0;

        private int size = 100;

        private String sortField;

        private List<T> data;

        private Class<T> clazz;

        private PrepareQuery handler = new PrepareQueryImpl();

        private Object search;

        private QueryEnum queryEnum = QueryEnum.termsQuery;

        public QueryFieldHandler(TransportClient client) {
                this.client = client;
        }

        public ESResult execute() throws GenericBusinessException {
                validate();
                doExecute();
                return new ESResult(retBool,count.get(),data);
        }

        private void doExecute() {
                if (search == null){
                        return;
                }
                // 获取查询条件
                BoolQueryBuilder boolQuery = this.handler.getBoolQueryBuilder(search, queryEnum);
                // 查询
                SearchResponse response = this.prepareSearch(boolQuery);
                if (Objects.isNull(response)){
                        return;
                }
                // 获取总数量
                this.count.getAndAdd(response.getHits().getTotalHits());
                // 获取返回值
                this.getResponseAsResult(response);
                this.retBool = true;
        }


        private List<T> getHitsToResponse(SearchHits searchHits) {
                List<T> data = Lists.newArrayList();
                for (SearchHit hit : searchHits.getHits()) {
                        T o = JsonNoNullUtil.transFormationMapNoNullVal(hit.getSourceAsString(),clazz);
                        data.add(o);
                }
                return data;
        }


        //如果查询数量大于10000,使用游标查询
        private void getResponseAsResult(SearchResponse response) {
                SearchHits searchHits = response.getHits();
                if (searchHits.getTotalHits()>0) {
                        this.data = this.getHitsToResponse(searchHits);
                }
        }


        private SearchResponse prepareSearch(BoolQueryBuilder boolQuery) {
                /**
                 * 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询
                 * 2.SearchType.SCAN = 扫描查询,无序
                 * 3.SearchType.COUNT = 不设置的话	,分词查询
                 * */
                SearchRequestBuilder searchRequestBuilder = client.prepareSearch(INDEX)
                        .setTypes(INDEX_TYPE)
                        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                        .setQuery(boolQuery)
                        .setFrom(from)
                        .setSize(size);
                if (sortField != null && !sortField.equals("")){
                        searchRequestBuilder.addSort(sortField,SortOrder.DESC);
                }
                return searchRequestBuilder.get();
        }

        public QueryFieldHandler setINDEX(String INDEX) {
                this.INDEX = INDEX;
                return this;
        }

        public QueryFieldHandler setINDEX_TYPE(String INDEX_TYPE) {
                this.INDEX_TYPE = INDEX_TYPE;
                return this;
        }

        public QueryFieldHandler setFrom(int from) {
                this.from = from;
                return this;
        }

        public QueryFieldHandler setSize(int size) {
                this.size = size;
                return this;
        }

        public QueryFieldHandler setSortField(String sortField) {
                this.sortField = sortField;
                return this;
        }

        public QueryFieldHandler setClazz(Class<T> clazz) {
                this.clazz = clazz;
                return this;
        }

        public QueryFieldHandler setHandler(PrepareQuery handler) {
                this.handler = handler;
                return this;
        }

        public QueryFieldHandler setSearch(Object search) {
                this.search = search;
                return this;
        }

        public QueryFieldHandler setQueryEnum(QueryEnum queryEnum) {
                this.queryEnum = queryEnum;
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
                if (search == null){
                        throw new GenericBusinessException("search IS NULL");
                }
                return super.validate();
        }
}
