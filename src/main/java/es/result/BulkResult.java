package es.result;

import java.util.List;

public class BulkResult{

        private boolean retBool;
        private List<String> error;
        private Long sucCount;
        private String errMsg;

        public BulkResult() {
        }

        public BulkResult(List<String> error, boolean retBool, Long sucCount, String errMsg) {
                this.error = error;
                this.retBool = retBool;
                this.sucCount = sucCount;
                this.errMsg = errMsg;
        }

        public boolean isRetBool() {
                return retBool;
        }

        public List<String> getError() {
                return error;
        }

        public Long getSucCount() {
                return sucCount;
        }

        public String getErrMsg() {
                return errMsg;
        }
}