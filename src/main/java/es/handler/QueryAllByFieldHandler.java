package es.handler;

import es.enums.QueryEnum;
import es.perpare.PrepareQuery;
import es.perpare.PrepareQueryImpl;
import es.result.ESResult;
import es.utils.JsonNoNullUtil;
import es.exception.GenericBusinessException;
import com.google.common.collect.Lists;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;

public class QueryAllByFieldHandler<T> extends ESBaseHandler {

        private static final int size = 10000;

        private static final TimeValue timeValue = new TimeValue(30000);

        private String sortField;

        private List<T> data;

        private Object search;

        private PrepareQuery handler = new PrepareQueryImpl();

        private Class<T> clazz;

        private QueryEnum queryEnum = QueryEnum.termsQuery;

        public QueryAllByFieldHandler(TransportClient client) {
                this.client = client;
        }

        public ESResult execute() throws GenericBusinessException {
                validate();
                doExecute();
                return new ESResult(retBool,count.get(),data);
        }

        //游标查询
        private void doExecute() {
                //记录迭代使用
                String scrollId = null;
                // 获取查询条件
                BoolQueryBuilder boolQuery = null;
                if (search != null){
                        boolQuery = this.handler.getBoolQueryBuilder(search, queryEnum);
                }

                SearchResponse response = this.getScrollResponse(boolQuery);
                if (response == null || response.getHits() == null){
                        return;
                }
                this.getResponseAsResult(response);
                // 获取总数量
                this.count.getAndAdd(response.getHits().getTotalHits());
                this.retBool = true;
                if (response.getHits().totalHits < size || response.getScrollId() == null){
                        return;
                }
                scrollId = response.getScrollId();
                while (true){
                        response = client.prepareSearchScroll(scrollId)
                                .setScroll(timeValue)
                                .get();
                        if (response == null || response.getHits() == null){
                                break;
                        }
                        this.getResponseAsResult(response);
                        if (response.getHits().getHits().length < size || response.getScrollId() == null){
                                break;
                        }
                        scrollId = response.getScrollId();

                }
        }

        private SearchResponse getScrollResponse(BoolQueryBuilder boolQuery) {
                SearchRequestBuilder searchRequestBuilder = client.prepareSearch()
                        .setIndices(INDEX)
                        .setTypes(INDEX_TYPE)
                        .setSize(size)
                        .setScroll(timeValue)
                        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
                if (boolQuery != null){
                        searchRequestBuilder.setQuery(boolQuery);
                }
                if (sortField != null && !sortField.equals("")){
                        searchRequestBuilder.addSort(sortField,SortOrder.ASC);
                }
                return searchRequestBuilder.get();
        }


        private List<T> getHitsToResponse(SearchHits searchHits) {
                List<T> data = Lists.newArrayList();
                for (SearchHit hit : searchHits.getHits()) {
                        T o = JsonNoNullUtil.transFormationMapNoNullVal(hit.getSourceAsString(),clazz);
                        data.add(o);
                }
                return data;
        }

        private void getResponseAsResult(SearchResponse response) {
                SearchHits searchHits = response.getHits();
                if (searchHits.getTotalHits()>0) {
                        if (this.data == null){
                                this.data = Lists.newArrayList();
                        }
                        this.data.addAll(this.getHitsToResponse(searchHits));
                }
        }


        public QueryAllByFieldHandler setINDEX(String INDEX) {
                this.INDEX = INDEX;
                return this;
        }

        public QueryAllByFieldHandler setINDEX_TYPE(String INDEX_TYPE) {
                this.INDEX_TYPE = INDEX_TYPE;
                return this;
        }

        public QueryAllByFieldHandler setSortField(String sortField) {
                this.sortField = sortField;
                return this;
        }

        public QueryAllByFieldHandler setClazz(Class<T> clazz) {
                this.clazz = clazz;
                return this;
        }

        public QueryAllByFieldHandler setSearch(Object search) {
                this.search = search;
                return this;
        }

        public QueryAllByFieldHandler setQueryEnum(QueryEnum queryEnum) {
                this.queryEnum = queryEnum;
                return this;
        }

        public QueryAllByFieldHandler setHandler(PrepareQuery handler) {
                this.handler = handler;
                return this;
        }

        public List<T> getData() {
                return data;
        }

        @Override
        public Boolean validate() throws GenericBusinessException {
                if (clazz == null){
                        throw new GenericBusinessException("clazz IS NOT NULL");
                }
                return super.validate();
        }
}
