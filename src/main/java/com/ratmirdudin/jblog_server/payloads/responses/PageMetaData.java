package com.ratmirdudin.jblog_server.payloads.responses;

import lombok.Data;

@Data
public class PageMetaData {
    private int currentPage;
    private int lastPage;
    private long totalElements;

    public PageMetaData(int currentPage, int lastPage, long totalElements) {
        this.currentPage = currentPage;
        this.lastPage = lastPage;
        this.totalElements = totalElements;
    }
}
