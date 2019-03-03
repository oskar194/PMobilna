package com.admin.budgetrook.wrappers;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class SummaryRequestWrapper {
    public Date from;
    public Date to;
    public List<Long> categoryIds;

    public SummaryRequestWrapper(Date from, Date to, List<Long> categoryIds) {
        this.from = from;
        this.to = to;
        this.categoryIds = categoryIds;
    }
}
