package com.viettelpost.core.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleModuleRequest {
    Long roleId;
    Long appId;
    int limit;
    int page;
    String txtSearch;
}
