package com.interactivehome.main_service.model.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupDto {
    @JsonProperty("_id")
    public Integer _id;
    public String name;
}
