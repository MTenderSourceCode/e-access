package com.procurement.access.infrastructure.handler.processing.responder

import com.procurement.access.application.service.Logger
import com.procurement.access.application.service.Transform
import com.procurement.access.dao.HistoryDao
import com.procurement.access.domain.fail.Fail
import com.procurement.access.infrastructure.api.v2.CommandTypeV2
import com.procurement.access.infrastructure.dto.converter.convert
import com.procurement.access.infrastructure.handler.AbstractHistoricalHandler
import com.procurement.access.infrastructure.handler.v2.CommandDescriptor
import com.procurement.access.lib.functional.Result
import com.procurement.access.lib.functional.flatMap
import com.procurement.access.service.ResponderService
import org.springframework.stereotype.Service

@Service
class ResponderProcessingHandler(
    private val responderService: ResponderService,
    transform: Transform,
    historyDao: HistoryDao,
    logger: Logger
) : AbstractHistoricalHandler<ResponderProcessingResult>(transform, historyDao, logger) {

    override val action: CommandTypeV2
        get() = CommandTypeV2.RESPONDER_PROCESSING

    override fun execute(descriptor: CommandDescriptor): Result<ResponderProcessingResult, Fail> {
        val params = descriptor.body.asJsonNode
            .params<ResponderProcessingRequest.Params>()
            .flatMap { it.convert() }
            .onFailure { return it }
        return responderService.responderProcessing(params = params)
    }
}
