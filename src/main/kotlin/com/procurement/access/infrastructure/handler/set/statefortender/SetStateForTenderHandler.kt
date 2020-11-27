package com.procurement.access.infrastructure.handler.set.statefortender

import com.procurement.access.application.service.Logger
import com.procurement.access.application.service.Transform
import com.procurement.access.application.service.tender.ExtendTenderService
import com.procurement.access.dao.HistoryDao
import com.procurement.access.domain.fail.Fail
import com.procurement.access.infrastructure.api.v2.CommandTypeV2
import com.procurement.access.infrastructure.dto.converter.convert
import com.procurement.access.infrastructure.handler.AbstractHistoricalHandler
import com.procurement.access.infrastructure.handler.v2.CommandDescriptor
import com.procurement.access.lib.functional.Result
import com.procurement.access.lib.functional.flatMap
import org.springframework.stereotype.Service

@Service
class SetStateForTenderHandler(
    private val tenderService: ExtendTenderService,
    transform: Transform,
    historyDao: HistoryDao,
    logger: Logger
) : AbstractHistoricalHandler<SetStateForTenderResult>(transform, historyDao, logger) {

    override val action: CommandTypeV2
        get() = CommandTypeV2.SET_STATE_FOR_TENDER

    override fun execute(descriptor: CommandDescriptor): Result<SetStateForTenderResult, Fail> {
        val params = descriptor.body.asJsonNode
            .params<SetStateForTenderRequest>()
            .flatMap { it.convert() }
            .onFailure { return it }
        return tenderService.setStateForTender(params = params)
    }
}
