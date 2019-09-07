package es.invoke;

import es.client.BuilderClient;
import es.enums.QueryEnum;
import es.handler.QueryAllByFieldHandler;
import es.perpare.PrepareQuery;
import es.result.ESResult;
import es.exception.GenericBusinessException;

public class QueryMatchAll<T> {

    public static <T> ESResult queryAll(String index, String indexType, Class<T> clazz) throws GenericBusinessException {
        return doQuery(index,indexType,null,null,clazz,null,null);
    }

    public static <T> ESResult queryAll(String index, String indexType, Class<T> clazz, String sortField) throws GenericBusinessException {
        return doQuery(index,indexType,null, null,clazz,null,sortField);
    }

    public static <T> ESResult queryAllByField(String index, String indexType, Object search, QueryEnum queryEnum, Class<T> clazz) throws GenericBusinessException {
        return doQuery(index,indexType, search, queryEnum, clazz,null,null);
    }

    public static <T> ESResult queryAllByField(String index, String indexType, Object search, QueryEnum queryEnum, Class<T> clazz, String sortField) throws GenericBusinessException {
        return doQuery(index,indexType, search, queryEnum, clazz,null,sortField);
    }

    public static <T> ESResult queryAllByField(String index, String indexType, Object search, QueryEnum queryEnum, Class<T> clazz, PrepareQuery query, String sortField) throws GenericBusinessException {
        return doQuery(index,indexType, search, queryEnum, clazz,query,sortField);
    }

    private static <T> ESResult doQuery(String index, String indexType, Object search, QueryEnum queryEnum, Class<T> clazz, PrepareQuery query, String sortField) throws GenericBusinessException {
        QueryAllByFieldHandler queryMatchAllHandler = BuilderClient.getInstance().queryMatchAllHandler(index)
                .setINDEX_TYPE(indexType)
                .setClazz(clazz);
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
        return queryMatchAllHandler.execute();
    }
}
