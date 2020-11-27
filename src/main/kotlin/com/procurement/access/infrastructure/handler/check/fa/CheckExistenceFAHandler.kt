package com.procurement.access.infrastructure.handler.check.fa

import com.procurement.access.application.service.Logger
import com.procurement.access.domain.fail.Fail
import com.procurement.access.infrastructure.api.v2.CommandTypeV2
import com.procurement.access.infrastructure.dto.converter.convert
import com.procurement.access.infrastructure.handler.AbstractValidationHandler
import com.procurement.access.infrastructure.handler.v2.CommandDescriptor
import com.procurement.access.lib.functional.ValidationResult
import com.procurement.access.lib.functional.asValidationFailure
import com.procurement.access.lib.functional.flatMap
import com.procurement.access.service.validation.ValidationService
import org.springframework.stereotype.Service

@Service
class CheckExistenceFAHandler(
    logger: Logger,
    private val validationService: ValidationService
) : AbstractValidationHandler(logger = logger) {

    override val action: CommandTypeV2
        get() = CommandTypeV2.CHECK_EXISTENCE_FA

    override fun execute(descriptor: CommandDescriptor): ValidationResult<Fail> {
        val params = descriptor.body.asJsonNode
            .params<CheckExistenceFARequest>()
            .flatMap { it.convert() }
            .onFailure { return it.reason.asValidationFailure() }

        return validationService.checkExistenceFA(params = params)
    }
}
