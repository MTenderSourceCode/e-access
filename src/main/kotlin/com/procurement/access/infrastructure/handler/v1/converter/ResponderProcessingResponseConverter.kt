package com.procurement.access.infrastructure.handler.v1.converter

import com.procurement.access.application.model.responder.processing.ResponderProcessing
import com.procurement.access.infrastructure.handler.v2.model.response.ResponderProcessingResult

fun ResponderProcessing.Params.Responder.toReference(): ResponderProcessingResult =
    ResponderProcessingResult( // FR-10.1.4.7
        name = this.name,  // FR-10.1.4.8
        id = this.id // FR-10.1.4.9
    )

