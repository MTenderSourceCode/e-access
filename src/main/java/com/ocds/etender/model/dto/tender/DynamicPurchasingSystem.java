package com.ocds.etender.model.dto.tender;

import lombok.Data;

@Data
public class DynamicPurchasingSystem {
    public Boolean hasDynamicPurchasingSystem;
    public Boolean hasOutsideBuyerAccess;
    public Boolean noFurtherContracts;
}
