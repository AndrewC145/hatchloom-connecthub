package com.hatchloom.connecthub.connecthub_service.service;

import com.hatchloom.connecthub.connecthub_service.dto.CursorResponse;
import com.hatchloom.connecthub.connecthub_service.utils.CursorPayload;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
public class CursorPaginationService {

    public<T, P extends CursorPayload> CursorResponse<T> paginate(
            String after,
            Integer limit,
            Function<Pageable, List<T>> firstPageFetcher,
            BiFunction<P, Pageable, List<T>> cursorPageFetcher,
            Function<String, P> cursorDecoder,
            Function<P, String> cursorEncoder,
            Function<T, P> payloadMapper
            )
    {
        int pageSize;
        if (limit == null || limit <= 0) {
            pageSize = 25;
        } else {
            pageSize = limit;
        }

        Pageable pageable = Pageable.ofSize(pageSize + 1);
        List<T> items;

        if (after == null || after.isBlank()) {
            items = firstPageFetcher.apply(pageable);
        } else {
            P payload = cursorDecoder.apply(after);
            items = cursorPageFetcher.apply(payload, pageable);
        }

        boolean hasNext = items.size() > pageSize;
        items = hasNext ? items.subList(0, pageSize) : items;
        String nextCursor = null;

        if (hasNext && !items.isEmpty()) {
            T lastItem = items.getLast();
            nextCursor = cursorEncoder.apply(payloadMapper.apply(lastItem));
        }

        return new CursorResponse<>(items, nextCursor, hasNext);
    }
}
