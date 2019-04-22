package es.handler;

import com.google.common.collect.Lists;
import es.enums.QueryEnum;
import es.perpare.PrepareQuery;
import es.perpare.PrepareQueryImpl;
import es.result.QueryResult;
import es.utils.JsonMapperUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;
import java.util.Objects;

public class QueryHandler<T> extends ESBaseHandler {

        private int from = 0;

        private int size = 100;

        private String sortField = "id";

        private List<T> data;

        private Class<T> clazz;

        private PrepareQuery handler = new PrepareQueryImpl();

        private Object search;

        private QueryEnum queryEnum = QueryEnum.termsQuery;

        public QueryHandler(TransportClient client) {
                this.client = client;
        }

        public QueryResult execute(){
                doExecute();
                return new QueryResult(retBool,count.get(),data);
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
                        T o = JsonMapperUtil.transFormationMapNoNullVal(hit.getSourceAsString(),clazz);
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
                SearchResponse response = client.prepareSearch(INDEX)
                        .setTypes(INDEX_TYPE)
                        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                        .setQuery(boolQuery)
                        .addSort(sortField, SortOrder.ASC)
                        .setFrom(from)
                        .setSize(size)
                        .get();
                //如果查询数量大于10000,使用游标查询方式
                return response;
        }

        public QueryHandler setINDEX(String INDEX) {
                this.INDEX = INDEX;
                return this;
        }

        public QueryHandler setINDEX_TYPE(String INDEX_TYPE) {
                this.INDEX_TYPE = INDEX_TYPE;
                return this;
        }

        public QueryHandler setFrom(int from) {
                this.from = from;
                return this;
        }

        public QueryHandler setSize(int size) {
                this.size = size;
                return this;
        }

        public QueryHandler setSortField(String sortField) {
                this.sortField = sortField;
                return this;
        }

        public QueryHandler setClazz(Class<T> clazz) {
                this.clazz = clazz;
                return this;
        }

        public QueryHandler setHandler(PrepareQuery handler) {
                this.handler = handler;
                return this;
        }

        public QueryHandler setSearch(Object search) {
                this.search = search;
                return this;
        }

        public QueryHandler setQueryEnum(QueryEnum queryEnum) {
                this.queryEnum = queryEnum;
                return this;
        }

        public List<T> getData() {
                return data;
        }
}
