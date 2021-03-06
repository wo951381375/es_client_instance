package com.daling.es.invoke;

import com.daling.es.client.BuilderClient;
import com.daling.es.handler.BulkHandler;
import com.daling.es.result.ESResult;
import com.daling.platform.exception.GenericBusinessException;

import java.util.List;

public class UpdateOrInsertDocument {


    public static ESResult byDocument(String index, String indexType, String documentName, List params) throws GenericBusinessException {
        return doBulk(index,indexType,documentName, false,params,null,null);
    }

    public static ESResult byDocument(String index, String indexType, String documentName, boolean coverYn, List params, Integer threadCount, Long timeoutSecond) throws GenericBusinessException {
        return doBulk(index,indexType,documentName, coverYn,params,threadCount,timeoutSecond);
    }

    private static ESResult doBulk(String index, String indexType, String documentName, boolean coverYn, List params, Integer threadCount, Long timeoutSecond) throws GenericBusinessException {
        BulkHandler bulkHandler = BuilderClient.getInstance().bulkHandler(index)
                .setINDEX_TYPE(indexType)
                .setDOCUMENTID(documentName)
                .setCoverYn(coverYn)
                .setParams(params);
        if (threadCount != null){
            bulkHandler.setTHREAD_COUNT(threadCount);
        }
        if (timeoutSecond != null){
            bulkHandler.setTIMEOUT(timeoutSecond);
        }
        return bulkHandler.execute();
    }
}
