package com.ocds.etender.model.dto.tender;

import lombok.Data;

@Data
public class Classification {
    public String scheme;
    public String id;
    public String description;
    public String uri;
}