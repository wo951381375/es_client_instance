package com.daling.es.invoke;

import com.daling.es.client.BuilderClient;
import com.daling.es.enums.QueryEnum;
import com.daling.es.handler.UpdateHandler;
import com.daling.es.perpare.PrepareQuery;
import com.daling.es.result.ESResult;
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
