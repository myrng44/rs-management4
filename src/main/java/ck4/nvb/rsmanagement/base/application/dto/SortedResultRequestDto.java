package ck4.nvb.rsmanagement.base.application.dto;

public class SortedResultRequestDto extends Dto {

    /**
     * Sorting information
     * Should include sorting field and optionally a direction (ASC or DESC)
     * Can contain more than one field separated by comma (,)
     * Examples:
     * "Name"
     * "Name DESC"
     * "Name ASC, Age DESC"
     */
    private String sort;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
