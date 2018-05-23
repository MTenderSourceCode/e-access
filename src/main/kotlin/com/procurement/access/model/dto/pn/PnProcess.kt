package com.procurement.access.model.dto.pn

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.access.model.dto.ocds.Planning
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class PnProcess(

        @JsonProperty("token")
        var token: String?,

        @JsonProperty("ocid")
        var ocId: String?,

        @JsonProperty("planning") @Valid @NotNull
        val planning: Planning,

        @JsonProperty("tender") @Valid @NotNull
        val tender: PnTender)