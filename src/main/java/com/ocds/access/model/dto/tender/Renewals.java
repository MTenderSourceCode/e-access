package com.ocds.access.model.dto.tender;

import lombok.Data;

@Data
public class Renewals {
    public Boolean hasRenewals;
    public Integer maxNumber;
    public String renewalConditions;
}