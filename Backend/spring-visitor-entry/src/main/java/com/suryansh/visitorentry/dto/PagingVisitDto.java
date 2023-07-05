package com.suryansh.visitorentry.dto;

import java.util.List;

/**
 * This is DTO record class that send back data to a client.
 *
 * @param pageNo Current page number.
 * @param totalPages Total Pages available.
 * @param visits List of visits
 * @param pageSize Page size
 * @param totalData Total Record in DB for search.
 */
public record PagingVisitDto(
        int pageNo,
        int totalPages,
        List<VisitDto>visits,
        int pageSize,
        long totalData
) { }
