package es.result;

import java.util.List;

public class QueryResult<T> {

        private boolean retBool;
        private Long count;
        private List<T> data;
        private String errMsg;

        public QueryResult() {
        }

        public QueryResult(boolean retBool, Long count, List<T> data, String errMsg) {
                this.retBool = retBool;
                this.count = count;
                this.data = data;
                this.errMsg = errMsg;
        }

        public QueryResult(boolean retBool, Long count, List<T> data) {
                this.retBool = retBool;
                this.count = count;
                this.data = data;
        }

        public boolean isRetBool() {
                return retBool;
        }

        public Long getCount() {
                return count;
        }

        public List<T> getData() {
                return data;
        }

        public String getErrMsg() {
                return errMsg;
        }
}
