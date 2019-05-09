package es.invoke;

import es.client.BuilderClient;
import es.enums.QueryEnum;
import es.handler.QueryFieldHandler;
import es.perpare.PrepareQuery;
import es.result.ESResult;
import es.exception.GenericBusinessException;

public class QueryField<T> {

    public static<T> ESResult byField(String index, String indexType, Object search, Class<T> clazz) throws GenericBusinessException {
        return doQuery(index,indexType,search,clazz,null,null,null,0,100);
    }

    public static<T> ESResult byField(String index, String indexType, Object search, Class<T> clazz, QueryEnum queryEnum) throws GenericBusinessException {
        return doQuery(index,indexType,search,clazz,queryEnum,null,null,0,100);
    }

    public static<T> ESResult byField(String index, String indexType, Object search, Class<T> clazz, QueryEnum queryEnum, String sortField, int from, int size) throws GenericBusinessException {
        return doQuery(index,indexType,search,clazz,queryEnum,sortField,null,from,size);
    }

    public static<T> ESResult byField(String index, String indexType, Object search, Class<T> clazz, QueryEnum queryEnum, String sortField, PrepareQuery query, int from, int size) throws GenericBusinessException {
        return doQuery(index,indexType,search,clazz,queryEnum,sortField,query,from,size);
    }

    private static <T> ESResult doQuery(String index, String indexType, Object search, Class<T> clazz, QueryEnum queryEnum, String sortField, PrepareQuery query, int from, int size) throws GenericBusinessException {
        QueryFieldHandler queryFieldHandler = BuilderClient.getInstance().queryFieldHandler(index)
                .setINDEX_TYPE(indexType)
                .setSearch(search)
                .setClazz(clazz)
                .setFrom(from);
        if (sortField != null){
            queryFieldHandler.setSortField(sortField);
        }
        if (query != null){
            queryFieldHandler.setHandler(query);
        }
        if (queryEnum != null){
            queryFieldHandler.setQueryEnum(queryEnum);
        }
        if (size>0 && size <10000){
            queryFieldHandler.setSize(size);
        }
        return queryFieldHandler.execute();
    }
}
