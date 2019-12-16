package cn.ganzhiqiang.ares.search.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zq_gan
 * @since 2019/12/15
 **/

public class ESSearchResult<T> {

    public static ESSearchResult EMPTY_RESULT = new ESSearchResult<>(0, null);

    public ESSearchResult(int total, List<T> results) {
        this.total = total;
        this.results = results;
    }

    private int total;
    private List<T> results;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
