package com.hatchloom.connecthub.connecthub_service.dto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CursorResponse<T> {
    private List<T> data;
    private String nextCursor;
    private boolean hasNext;

    public CursorResponse(List<T> data, String nextCursor, boolean hasNext) {
        this.data = data;
        this.nextCursor = nextCursor;
        this.hasNext = hasNext;
    }
}
