package com.procurement.access.infrastructure.handler.find.auction

import com.fasterxml.jackson.annotation.JsonProperty

data class FindAuctionsRequest(
    @field:JsonProperty("cpid") @param:JsonProperty("cpid") val cpid: String,
    @field:JsonProperty("ocid") @param:JsonProperty("ocid") val ocid: String
)
