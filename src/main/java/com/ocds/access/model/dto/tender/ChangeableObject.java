package com.ocds.access.model.dto.tender;

import lombok.Data;

@Data
public class ChangeableObject {
    public String property;
    public Object former_value;
}