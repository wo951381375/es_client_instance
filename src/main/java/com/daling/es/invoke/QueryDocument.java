package com.daling.es.invoke;

import com.daling.es.client.BuilderClient;
import com.daling.es.handler.QueryDocumentHandler;
import com.daling.es.result.ESResult;
import es.exception.GenericBusinessException;

import java.util.List;

public class QueryDocument<T> {

    public static<T> ESResult byDocument(String index, String indexType, Class<T> clazz, List<String> documentIds) throws GenericBusinessException {
        return doQuery(index,indexType,clazz,documentIds);
    }

    private static <T> ESResult doQuery(String index, String indexType, Class<T> clazz, List<String> documentIds) throws GenericBusinessException {
        QueryDocumentHandler queryDocumentHandler = BuilderClient.getInstance().queryDocumentHandler(index)
                .setINDEX_TYPE(indexType)
                .setClazz(clazz)
                .setDocumentIds(documentIds);
        return queryDocumentHandler.execute();
    }
}
