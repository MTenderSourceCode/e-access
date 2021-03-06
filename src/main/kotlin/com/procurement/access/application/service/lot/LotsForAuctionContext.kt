package com.procurement.access.application.service.lot

import com.procurement.access.domain.model.enums.OperationType

data class LotsForAuctionContext(
    val cpid: String,
    val stage: String,
    val prevStage: String,
    val operationType: OperationType
)
