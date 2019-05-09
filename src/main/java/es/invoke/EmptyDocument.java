package es.invoke;

import es.client.BuilderClient;
import es.handler.EmptyHandler;
import es.result.ESResult;
import es.exception.GenericBusinessException;

public class EmptyDocument {

    public static ESResult empty(String index, String indexType) throws GenericBusinessException {
        return doEmpty(index,indexType);
    }

    private static ESResult doEmpty(String index, String indexType) throws GenericBusinessException {
        EmptyHandler emptyHandler = BuilderClient.getInstance().emptyHandler(index)
                .setINDEX_TYPE(indexType);
        return emptyHandler.execute();
    }
}
