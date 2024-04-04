package com.viettelpost.core.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseListResponse2 {
    private boolean error;
    private int total;
    private int page;
    private Object data;
}
