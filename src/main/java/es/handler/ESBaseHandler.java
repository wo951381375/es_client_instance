package es.handler;

import es.BuilderClient;
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
}
