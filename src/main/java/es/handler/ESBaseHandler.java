package es.handler;

import es.client.BuilderClient;
import es.exception.GenericBusinessException;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

public class ESBaseHandler {

        static Logger log = LoggerFactory.getLogger(BuilderClient.class);

        protected TransportClient client;

        protected String INDEX;

        protected String INDEX_TYPE;

        protected AtomicLong count = new AtomicLong();

        protected boolean retBool;

        public AtomicLong getCount() {
                return count;
        }

        public boolean isRetBool() {
                return retBool;
        }

        public Boolean validate() throws GenericBusinessException {
                if (INDEX == null || INDEX.equals("")){
                        throw new GenericBusinessException("INDEX IS NOT NULL");
                }
                if (INDEX_TYPE == null || INDEX_TYPE.equals("")){
                        throw new GenericBusinessException("INDEX_TYPE IS NOT NULL");
                }
                return true;
        }
}
