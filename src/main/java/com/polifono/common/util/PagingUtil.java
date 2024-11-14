package com.polifono.common.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PagingUtil {

    public static int getMaxPageSize(int size) {
        return Math.min(size, 10);
    }

    public static Pageable createPageable(int page, int size, String orderBy) {
        return PageRequest.of(page, size, Sort.by(Sort.Order.desc(orderBy)));
    }
}
