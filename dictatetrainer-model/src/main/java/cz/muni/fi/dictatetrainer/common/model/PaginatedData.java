package cz.muni.fi.dictatetrainer.common.model;

import java.util.List;

/**
 * Class that enables pagination feature - showing the subset of results on a page
 * It holds total number of entries and entities on the particular page
 */
public class PaginatedData<T> {
    private final int numberOfRows;
    //just the rows on the particular page
    private final List<T> rows;

    public PaginatedData(final int numberOfRows, final List<T> rows) {
        this.numberOfRows = numberOfRows;
        this.rows = rows;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public List<T> getRows() {
        return rows;
    }

    public T getRow(final int index) {
        if (index >= rows.size()) {
            return null;
        }
        return rows.get(index);
    }

    @Override
    public String toString() {
        return "PaginatedData [numberOfRows=" + numberOfRows + ", rows=" + rows + "]";
    }

}