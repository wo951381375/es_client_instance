package com.daling.es.invoke;

import com.daling.es.client.BuilderClient;
import com.daling.es.enums.QueryEnum;
import com.daling.es.handler.QueryAllByFieldHandler;
import com.daling.es.perpare.DefineSource;
import com.daling.es.perpare.PrepareQuery;
import com.daling.es.result.ESResult;
import com.daling.platform.exception.GenericBusinessException;

public class QueryMatchAll<T> {

    public static <T> ESResult queryAll(String index, String indexType, Class<T> clazz) throws GenericBusinessException {
        return doQuery(index,indexType,null,null,clazz,null,null, false, null, null);
    }

    public static <T> ESResult queryAll(String index, String indexType, Class<T> clazz, String sortField) throws GenericBusinessException {
        return doQuery(index,indexType,null, null,clazz,null,sortField, false, null, null);
    }

    public static <T> ESResult queryAllByField(String index, String indexType, Object search, QueryEnum queryEnum, Class<T> clazz) throws GenericBusinessException {
        return doQuery(index,indexType, search, queryEnum, clazz,null,null, false, null, null);
    }

    public static <T> ESResult queryAllByField(String index, String indexType, Object search, QueryEnum queryEnum, Class<T> clazz, String sortField) throws GenericBusinessException {
        return doQuery(index,indexType, search, queryEnum, clazz,null,sortField, false, null, null);
    }

    public static <T> ESResult queryAllByField(String index, String indexType, Object search, QueryEnum queryEnum, Class<T> clazz, PrepareQuery query, String sortField) throws GenericBusinessException {
        return doQuery(index,indexType, search, queryEnum, clazz,query,sortField, false, null, null);
    }

    private static <T> ESResult doQuery(String index, String indexType, Object search, QueryEnum queryEnum, Class<T> clazz, PrepareQuery query, String sortField, boolean defineSource, Object sourceObj, DefineSource source) throws GenericBusinessException {
        QueryAllByFieldHandler queryMatchAllHandler = BuilderClient.getInstance().queryMatchAllHandler(index)
                .setINDEX_TYPE(indexType)
                .setClazz(clazz)
                .isDefineSource(defineSource);
        if (sortField != null){
            queryMatchAllHandler.setSortField(sortField);
        }
        if (queryEnum != null){
            queryMatchAllHandler.setQueryEnum(queryEnum);
        }
        if (search != null){
            queryMatchAllHandler.setSearch(search);
        }
        if (query != null){
            queryMatchAllHandler.setHandler(query);
        }
        if (sourceObj != null){
            queryMatchAllHandler.setDefineSource(sourceObj);
        }
        if (source != null){
            queryMatchAllHandler.setSource(source);
        }

        return queryMatchAllHandler.execute();
    }
}
