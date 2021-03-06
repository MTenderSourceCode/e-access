package com.procurement.access.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.access.domain.model.enums.LotStatus
import com.procurement.access.domain.model.enums.LotStatusDetails

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Lot @JsonCreator constructor(

        val id: String,

        val internalId: String?,

        var title: String?,

        var description: String?,

        var status: LotStatus?,

        var statusDetails: LotStatusDetails?,

        val value: Value,

        val options: List<Option>?,

        val recurrentProcurement: List<RecurrentProcurement>?,

        val renewals: List<Renewal>?,

        val variants: List<Variant>?,

        var contractPeriod: ContractPeriod?,

        var placeOfPerformance: PlaceOfPerformance?
)