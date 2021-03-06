package com.procurement.access.infrastructure.handler.v1.model.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.access.domain.model.enums.MainProcurementCategory
import com.procurement.access.domain.model.enums.ProcurementMethod
import com.procurement.access.model.dto.ocds.Classification
import com.procurement.access.model.dto.ocds.Item
import com.procurement.access.model.dto.ocds.Lot

data class GetDataForAcRq @JsonCreator constructor(

        val awards: Set<Award>
)

data class Award @JsonCreator constructor(

        val relatedLots: List<String>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GetDataForAcRs @JsonCreator constructor(

        val contractedTender: GetDataForAcTender
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GetDataForAcTender @JsonCreator constructor(

    val id: String?,

    var classification: Classification,

    val procurementMethod: ProcurementMethod,

    val procurementMethodDetails: String,

    val mainProcurementCategory: MainProcurementCategory,

    var lots: List<Lot>,

    var items: List<Item>
)
