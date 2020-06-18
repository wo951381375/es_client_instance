package com.daling.es.invoke;

import com.daling.es.client.BuilderClient;
import com.daling.es.enums.QueryEnum;
import com.daling.es.handler.QueryFieldHandler;
import com.daling.es.perpare.DefineSource;
import com.daling.es.perpare.PrepareQuery;
import com.daling.es.result.ESResult;
import com.daling.platform.exception.GenericBusinessException;

public class QueryField<T> {

    public static<T> ESResult byField(String index, String indexType, Object search, Class<T> clazz) throws GenericBusinessException {
        return doQuery(index,indexType,search,clazz,null,null,null,0,100, false, null, null);
    }

    public static<T> ESResult byField(String index, String indexType, Object search, Class<T> clazz, QueryEnum queryEnum) throws GenericBusinessException {
        return doQuery(index,indexType,search,clazz,queryEnum,null,null,0,100, false, null, null);
    }

    public static<T> ESResult byField(String index, String indexType, Object search, Class<T> clazz, QueryEnum queryEnum, String sortField, int from, int size) throws GenericBusinessException {
        return doQuery(index,indexType,search,clazz,queryEnum,sortField,null,from,size, false, null, null);
    }

    public static<T> ESResult byField(String index, String indexType, Object search, Class<T> clazz, QueryEnum queryEnum, String sortField, PrepareQuery query, int from, int size) throws GenericBusinessException {
        return doQuery(index,indexType,search,clazz,queryEnum,sortField,query,from,size, false, null, null);
    }

    public static<T> ESResult byField(String index, String indexType, Object search, Class<T> clazz, QueryEnum queryEnum, String sortField, PrepareQuery query, int from, int size, Object sourceObj, DefineSource source) throws GenericBusinessException {
        return doQuery(index,indexType,search,clazz,queryEnum,sortField,query,from,size, true, sourceObj, source);
    }

    private static <T> ESResult doQuery(String index, String indexType, Object search, Class<T> clazz, QueryEnum queryEnum, String sortField, PrepareQuery query, int from, int size, boolean defineSource, Object sourceObj, DefineSource source) throws GenericBusinessException {
        QueryFieldHandler queryFieldHandler = BuilderClient.getInstance().queryFieldHandler(index)
                .setINDEX_TYPE(indexType)
                .setSearch(search)
                .setClazz(clazz)
                .setFrom(from)
                .isDefineSource(defineSource);
        if (sortField != null){
            queryFieldHandler.setSortField(sortField);
        }
        if (query != null){
            queryFieldHandler.setHandler(query);
        }
        if (queryEnum != null){
            queryFieldHandler.setQueryEnum(queryEnum);
        }
        if (size>0 && size <=10000){
            queryFieldHandler.setSize(size);
        }
        if (sourceObj != null){
            queryFieldHandler.setDefineSource(sourceObj);
        }
        if (source != null){
            queryFieldHandler.setSource(source);
        }
        return queryFieldHandler.execute();
    }
}
