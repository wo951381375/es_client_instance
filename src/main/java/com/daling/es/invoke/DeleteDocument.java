package com.daling.es.invoke;

import com.daling.es.client.BuilderClient;
import com.daling.es.handler.DeleteHandler;
import com.daling.es.result.ESResult;
import com.daling.platform.exception.GenericBusinessException;

import java.util.List;

public class DeleteDocument {

    public static ESResult byDocument(String index, String indexType, List<String> documentIds) throws GenericBusinessException {
        return doDelete(index,indexType,documentIds);
    }

    private static ESResult doDelete(String index, String indexType, List<String> documentIds) throws GenericBusinessException {
        DeleteHandler deleteHandler = BuilderClient.getInstance().deleteHandler(index)
                .setINDEX_TYPE(indexType)
                .setDocumentIds(documentIds);
        return deleteHandler.execute();
    }
}
