package com.procurement.access.model.dto.lots

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GetLotsAuctionRs @JsonCreator constructor(

        val awardCriteria: String?,

        val tender: GetLotsAuctionTender
)

data class GetLotsAuctionTender @JsonCreator constructor(

        val id: String,

        val title: String?,

        val description: String?,

        val awardCriteria: String?,

        val lots: List<LotDto>
)