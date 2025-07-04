package ck4.nvb.rsmanagement.base.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serial;
import java.io.Serializable;

public class OffsetBasePageable implements Pageable, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int limit;
    private final long offset;
    private final Sort sort;

    public OffsetBasePageable(long offset, int limit, Sort sort) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset cannot be negative!");
        }

        if (limit < 1) {
            throw new IllegalArgumentException("Limit cannot be less than 1!");
        }

        this.offset = offset;
        this.limit = limit;
        this.sort = sort;
    }

    public OffsetBasePageable(long offset, int limit, Sort.Direction direction, String... properties) {
        this(offset, limit, Sort.by(direction, properties));
    }

    public OffsetBasePageable(long offset, int limit) {
        this(offset, limit, Sort.unsorted());
    }

    @Override
    public int getPageNumber() {
        return (int) (offset / (long) limit);
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetBasePageable(offset + getPageSize(), getPageSize(), getSort());
    }

    public OffsetBasePageable previous() {
        return hasPrevious() ? new OffsetBasePageable(offset - getPageSize(), getPageSize(), getSort()) : this;
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    @Override
    public Pageable first() {
        return new OffsetBasePageable(0, getPageSize(), getSort());
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new OffsetBasePageable((long) pageNumber * getPageSize(), getPageSize(), getSort());
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }
}
