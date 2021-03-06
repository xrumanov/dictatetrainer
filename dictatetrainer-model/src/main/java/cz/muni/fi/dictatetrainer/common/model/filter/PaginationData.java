package cz.muni.fi.dictatetrainer.common.model.filter;

/**
 * Class that holds settings for pagination
 *
 * firstResult: first showed result
 * maxResults: maximum amount of results that will be shown
 * orderField: field on which ordering will be performed
 * orderMode: ascending or descending
 */
public class PaginationData {
    private final int firstResult;
    private final int maxResults;
    private final String orderField;
    private final OrderMode orderMode;

    public enum OrderMode {
        ASCENDING, DESCENDING
    }

    public PaginationData(final int firstResult, final int maxResults, final String orderField,
                          final OrderMode orderMode) {
        this.firstResult = firstResult;
        this.maxResults = maxResults;
        this.orderField = orderField;
        this.orderMode = orderMode;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public String getOrderField() {
        return orderField;
    }

    public OrderMode getOrderMode() {
        return orderMode;
    }

    public boolean isAscending() {
        return OrderMode.ASCENDING.equals(orderMode);
    }

    @Override
    public String toString() {
        return "PaginationData [firstResult=" + firstResult + ", maxResults=" + maxResults + ", orderField="
                + orderField + ", orderMode=" + orderMode + "]";
    }

}