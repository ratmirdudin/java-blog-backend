package com.ratmirdudin.jblog_server.payloads.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageMetaData {
    private int currentPage;
    private int lastPage;
    private long totalElements;
}
