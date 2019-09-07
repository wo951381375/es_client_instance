package es.handler;

import es.result.ESResult;
import es.utils.JsonNoNullUtil;
import es.utils.JsonUtil;
import es.exception.GenericBusinessException;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.*;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BulkHandler<T> extends ESBaseHandler {

        private String DOCUMENTID;

        private Integer THREAD_COUNT = 1;

        private List<String> error = Collections.synchronizedList(new LinkedList<>());

        private Long TIMEOUT = 60L;

        private List params;

        /**
         * 输入空值,是否覆盖操作,默认 false
         * */
        private boolean coverYn;

        //单行执行bulk
        private static final Integer BULK_LIMIT = 5000;

        public BulkHandler(TransportClient client) {
                this.client = client;
        }

        public ESResult execute() throws GenericBusinessException {
                validate();
                doExecute();
                StringBuilder msg = new StringBuilder();
                if (params!= null){
                        msg.append("Params: ").append(params.size()).append(", ");
                }
                if (count!= null){
                        msg.append("Success: ").append(count.get()).append(", ");
                }
                if (error!= null){
                        msg.append("Error: ").append(error.size()).append(", ");
                }
                return new ESResult(retBool, msg.toString(),error,count.get());
        }

        private void doExecute() {
                if (params == null || params.isEmpty()){
                        return;
                }
                Set<Boolean> exception = Sets.newHashSet();
                List<ListenableFuture<Integer>> futureList = Lists.newArrayList();
                List<List<Object>> partition = Lists.partition(params, BULK_LIMIT);
                ListeningExecutorService service = null;
                try {
                        service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(THREAD_COUNT));
                        for (List list : partition) {
                                //limit/5000
                                BulkRequestBuilder bulk = this.getUpdateOrInsertResponse(list);
                                if (Objects.isNull(bulk)){
                                        log.error("Document Id Is Not Fount , Please Check Your Params, Params:{}", JsonNoNullUtil.transFormationJson(list));
                                        continue;
                                }
                                ListenableFuture<Integer> ask = service.submit(new Callable<Integer>() {
                                        @Override
                                        public Integer call() throws Exception {
                                                BulkResponse response = bulk.get();
                                                List<BulkItemResponse> bulkList = Arrays.asList(response.getItems());
                                                List<String> collect = bulkList.stream().filter(a -> a.isFailed()).map(a -> a.getId()).collect(Collectors.toList());
                                                error.addAll(collect);
                                                //返回成功个数
                                                int count = list.size() - collect.size();
                                                return count;
                                        }
                                });
                                if (ask!=null) futureList.add(ask);
                                //处理回调
                                Futures.addCallback(ask, new FutureCallback<Integer>() {
                                        @Override
                                        public void onFailure(Throwable arg0) {
                                                arg0.printStackTrace();
                                                List<String> collect = getDocumentId(list);
                                                error.addAll(collect);
                                        }

                                        @Override
                                        public void onSuccess(Integer upd) {
                                                count.getAndAdd(upd);
                                                exception.add(true);
                                        }
                                });
                        }
                        ListenableFuture<List<Integer>> listenableFuture = Futures.successfulAsList(futureList);
                        // 默认等待60秒，如果没取到数据则跳出, 且可以获取成功结果集
                        listenableFuture.get(TIMEOUT, TimeUnit.SECONDS);
                } catch (Exception e) {
                        log.error("error",e);
                        List<String> collect = getDocumentId(params);
                        error.addAll(collect);
                }finally {
                        // 一定要shutdown
                        if (service!=null) service.shutdownNow();
                }
                retBool = error.isEmpty();
        }

        private List<String> getDocumentId(List result){
                List<String> res = Lists.newLinkedList();
                for (Object obj : result){
                        Map map = JsonNoNullUtil.transFormationMapNoNullVal(obj,Map.class);
                        Object id = map.get(DOCUMENTID);
                        if (!Objects.isNull(id)){
                                res.add(id.toString());
                        }
                }
                return res;
        }



        private BulkRequestBuilder getUpdateOrInsertResponse(List<Object> list) {
                BulkRequestBuilder bulk = client.prepareBulk();
                for (Object obj : list) {
                        Map<String,Object> map = null;
                        // 是否空值覆盖
                        if (coverYn){
                            map = JsonUtil.transFormationMap(obj,Map.class);
                        }else {
                            map = JsonNoNullUtil.transFormationMapNoNullVal(obj,Map.class);
                        }
                        if (Objects.isNull(map.get(DOCUMENTID))) {
                                continue;
                        }
                        XContentBuilder builder = this.getXContentBuilder(map);
                        UpdateRequestBuilder upsert = client.prepareUpdate(INDEX, INDEX_TYPE, map.get(DOCUMENTID).toString()).setDoc(builder).setUpsert(builder);
                        bulk.add(upsert);
                }
                BulkRequest request = bulk.request();
                int size = request.requests().size();
                if (size == 0){
                        return null;
                }
                return bulk;
        }

        private static XContentBuilder getXContentBuilder(Map<String,Object> map) {
                XContentBuilder builder = null;
                try {
                        builder = XContentFactory.jsonBuilder().startObject();
                        Set<Map.Entry<String, Object>> entries = map.entrySet();
                        for (Map.Entry<String, Object> entry : entries){
                                builder.field(entry.getKey(),entry.getValue());
                        }
                        builder.endObject();
                } catch (Exception e) {
                }
                return builder;
        }


        public BulkHandler setINDEX(String INDEX) {
                this.INDEX = INDEX;
                return this;
        }

        public BulkHandler setINDEX_TYPE(String INDEX_TYPE) {
                this.INDEX_TYPE = INDEX_TYPE;
                return this;
        }


        public BulkHandler setDOCUMENTID(String DOCUMENTID) {
                this.DOCUMENTID = DOCUMENTID;
                return this;
        }

        public BulkHandler setTHREAD_COUNT(Integer THREAD_COUNT) {
                this.THREAD_COUNT = THREAD_COUNT;
                return this;
        }


        public BulkHandler setTIMEOUT(Long TIMEOUT) {
                this.TIMEOUT = TIMEOUT;
                return this;
        }

        public BulkHandler setParams(List params) {
                this.params = params;
                return this;
        }

        public BulkHandler setCoverYn(boolean coverYn) {
                this.coverYn = coverYn;
                return this;
        }

        public List<String> getError() {
                return error;
        }

        @Override
        public Boolean validate() throws GenericBusinessException {
                if (DOCUMENTID == null || DOCUMENTID.equals("")){
                        throw new GenericBusinessException("DOCUMENTID IS NOT NULL");
                }
                if (params == null || params.isEmpty()){
                        throw new GenericBusinessException("params IS NOT NULL");
                }
                return super.validate();
        }
}


