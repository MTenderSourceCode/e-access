package com.procurement.access.model.entity

import java.util.*


class TenderProcessEntity(

        var cpId: String,

        var token: UUID,

        var owner: String,

        var stage: String,

        var createdDate: Date,

        var jsonData: String
)