package com.procurement.access.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.access.domain.model.enums.Scheme

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Classification @JsonCreator constructor(

        val scheme: Scheme,

        val id: String,

        val description: String,

        val uri: String?
)
