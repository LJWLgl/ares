package cn.ganzhiqiang.ares.common.warpper;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import cn.ganzhiqiang.ares.common.enums.NapiRespStatus;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NapiRespWrapper<T> {

    private int status;
    private String message;
    private T data;

    public NapiRespWrapper(T data) {
        this.status = NapiRespStatus.SUCCESS.apiCode;
        this.message = NapiRespStatus.SUCCESS.commonMsg;
        this.data = data;
    }

    public NapiRespWrapper(NapiRespStatus status) {
        this.status = status.apiCode;
        this.message = status.commonMsg;
    }

    public NapiRespWrapper(NapiRespStatus status, String msg) {
        this.status = status.apiCode;
        this.message = msg;
    }

    @Data
    @NoArgsConstructor
    public static class ScrollingData<E> {
        private int more;
        @JsonProperty("next_start")
        private int nextStart;
        @JsonProperty("object_list")
        private List<E> objectList = Collections.emptyList();

        public ScrollingData(boolean more, int nextStart, List<E> objectList) {
            this.more = more ? 1 : 0;
            this.nextStart = nextStart;
            this.objectList = objectList;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaginationData<E> extends ScrollingData<E> {
        private long total;
        private int limit;

        public PaginationData(long total, int limit, boolean more, int nextStart, List<E> objectList) {
            super(more, nextStart, objectList);
            this.total = total;
            this.limit = limit;
        }
    }

}
