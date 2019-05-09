package es.invoke;

import es.client.BuilderClient;
import es.enums.QueryEnum;
import es.handler.UpdateHandler;
import es.perpare.PrepareQuery;
import es.result.ESResult;
import es.exception.GenericBusinessException;

import java.util.Map;

public class UpdateField {


    public static ESResult byFIeld(String index, String indexType, Object search, Map<String,Object> param, QueryEnum queryEnum, PrepareQuery query) throws GenericBusinessException {
        return doUpdate(index,indexType,search,param,queryEnum,query);
    }

    public static ESResult byFIeld(String index, String indexType, Object search, Map<String,Object> param, QueryEnum queryEnum) throws GenericBusinessException {
        return doUpdate(index,indexType,search,param,queryEnum,null);
    }

    public static ESResult byFIeld(String index, String indexType, Object search, Map<String,Object> param) throws GenericBusinessException {
        return doUpdate(index,indexType,search,param,null,null);
    }

    private static ESResult doUpdate(String index, String indexType, Object search, Map<String,Object> param, QueryEnum queryEnum, PrepareQuery query) throws GenericBusinessException {
        UpdateHandler updateHandler = BuilderClient.getInstance().updateHandler(index)
                .setINDEX_TYPE(indexType)
                .setSearch(search)
                .setParam(param);
        if (queryEnum != null){
            updateHandler.setQueryEnum(queryEnum);
        }
        if (query != null){
            updateHandler.setHandler(query);
        }
        return updateHandler.execute();
    }
}
