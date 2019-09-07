package es.handler;

import es.enums.QueryEnum;
import es.perpare.PrepareQuery;
import es.perpare.PrepareQueryImpl;
import es.result.ESResult;
import es.exception.GenericBusinessException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;

import java.util.Map;

public class UpdateHandler extends ESBaseHandler{

    private Object search;

    private Map<String,Object> param;

    private PrepareQuery handler = new PrepareQueryImpl();

    private QueryEnum queryEnum = QueryEnum.termsQuery;

    public UpdateHandler(TransportClient client) {
        this.client = client;
    }

    public ESResult execute() throws GenericBusinessException {
        validate();
        doExecute();
        return new ESResult(retBool,count.get());
    }

    private void doExecute() {

        // 获取查询条件
        BoolQueryBuilder boolQuery = this.handler.getBoolQueryBuilder(search, queryEnum);
        this.getUpdateGoodsByFiledCount(param, boolQuery);
        this.retBool = true;
    }

    private void getUpdateGoodsByFiledCount(Map<String,Object> param, BoolQueryBuilder boolQuery) {
        String inlineScript = this.getInLineScript(param);
        Script script = new Script(ScriptType.INLINE,"painless",inlineScript , param);
        BulkByScrollResponse response = UpdateByQueryAction.INSTANCE.newRequestBuilder(client)
                .filter(boolQuery)
                .source(INDEX)
                .script(script)
                .abortOnVersionConflict(false) //版本冲突,不终止服务,其余继续执行
                .get();
        Long count = response.getUpdated();
        this.count.getAndAdd(count);
    }

    private String getInLineScript(Map<String, Object> params) {
        String inLineScript = "";
        for (String key : params.keySet()) {
            inLineScript += "ctx._source."+key+"= params."+key+";";
        }
        return inLineScript;
    }

    @Override
    public Boolean validate() throws GenericBusinessException {
        if (search == null){
            throw new GenericBusinessException("search IS NOT NULL");
        }
        if (param == null){
            throw new GenericBusinessException("param IS NOT NULL");
        }
        return super.validate();
    }

    public UpdateHandler setINDEX(String INDEX) {
        this.INDEX = INDEX;
        return this;
    }

    public UpdateHandler setINDEX_TYPE(String INDEX_TYPE) {
        this.INDEX_TYPE = INDEX_TYPE;
        return this;
    }

    public UpdateHandler setSearch(Object search) {
        this.search = search;
        return this;
    }

    public UpdateHandler setParam(Map<String,Object> param) {
        this.param = param;
        return this;
    }

    public UpdateHandler setHandler(PrepareQuery handler) {
        this.handler = handler;
        return this;
    }

    public UpdateHandler setQueryEnum(QueryEnum queryEnum) {
        this.queryEnum = queryEnum;
        return this;
    }
}