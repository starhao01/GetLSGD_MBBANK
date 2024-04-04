package com.viettelpost.core.controller.request;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel
public class ModuleRequest implements Serializable {
    Long id;
    String name;
    String code;
    Long type;
    Long parent_id;
    String url;
    Long position;
    String icon;
    String description;
}
