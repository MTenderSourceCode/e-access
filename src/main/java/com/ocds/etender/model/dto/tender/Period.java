package com.ocds.etender.model.dto.tender;

import lombok.Data;

@Data
public class Period {
    public String startDate;
    public String endDate;
    public String maxExtentDate;
    public Integer durationInDays;
}