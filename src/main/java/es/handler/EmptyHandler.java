package es.handler;

import es.result.ESResult;
import es.exception.GenericBusinessException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;

public class EmptyHandler extends ESBaseHandler{

    public EmptyHandler(TransportClient client) {
        this.client = client;
    }

    public ESResult execute() throws GenericBusinessException {
        validate();
        doExecute();
        return new ESResult(retBool,count.get());
    }

    private void doExecute() {
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                .filter(matchAllQueryBuilder)
                .source(INDEX)
                .get();
        if (response == null){
            return;
        }
        count.getAndAdd(response.getDeleted());
        this.retBool = true;
    }

    @Override
    public Boolean validate() throws GenericBusinessException {
        return super.validate();
    }

    public EmptyHandler setINDEX(String INDEX) {
        this.INDEX = INDEX;
        return this;
    }

    public EmptyHandler setINDEX_TYPE(String INDEX_TYPE) {
        this.INDEX_TYPE = INDEX_TYPE;
        return this;
    }

}
