package com.procurement.access.application.service.fe.create

import java.time.LocalDateTime

data class CreateFEContext(
    val cpid: String,
    val prevStage: String,
    val stage: String,
    val owner: String,
    val startDate: LocalDateTime
)
