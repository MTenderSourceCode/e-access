package com.procurement.access.service.validation.strategy

import com.procurement.access.application.repository.TenderProcessRepository
import com.procurement.access.application.service.tender.strategy.check.CheckAccessToTenderParams
import com.procurement.access.dao.TenderProcessDao
import com.procurement.access.domain.fail.Fail
import com.procurement.access.domain.fail.error.ValidationErrors
import com.procurement.access.domain.model.Cpid
import com.procurement.access.domain.model.Ocid
import com.procurement.access.exception.ErrorException
import com.procurement.access.exception.ErrorType
import com.procurement.access.infrastructure.api.v1.CommandMessage
import com.procurement.access.infrastructure.api.v1.cpid
import com.procurement.access.infrastructure.api.v1.owner
import com.procurement.access.infrastructure.api.v1.token
import com.procurement.access.lib.functional.Result
import com.procurement.access.lib.functional.ValidationResult
import com.procurement.access.lib.functional.asValidationFailure
import com.procurement.access.model.entity.TenderProcessEntity

class CheckOwnerAndTokenStrategy(
    private val tenderProcessDao: TenderProcessDao,
    private val tenderProcessRepository: TenderProcessRepository
) {

    fun checkOwnerAndToken(cm: CommandMessage) {
        val cpid = cm.cpid
        val token = cm.token
        val owner = cm.owner

        val auths = tenderProcessDao.findAuthByCpid(cpid = cpid)
            .takeIf { it.isNotEmpty() }
            ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)


        auths.forEach { auth ->
            if (auth.owner != owner)
                throw ErrorException(error = ErrorType.INVALID_OWNER)

            if (auth.token != token)
                throw ErrorException(error = ErrorType.INVALID_TOKEN)
        }
    }

    fun checkOwnerAndToken(params: CheckAccessToTenderParams): ValidationResult<Fail> {

        val tenderProcessEntity = getTenderProcessEntityByCpIdAndOcid(cpid = params.cpid, ocid = params.ocid)
            .onFailure { return it.reason.asValidationFailure() }

        if (tenderProcessEntity.owner != params.owner.toString())
            return ValidationResult.error(ValidationErrors.InvalidOwner(owner = params.owner, cpid = params.cpid))

        if (tenderProcessEntity.token != params.token)
            return ValidationResult.error(ValidationErrors.InvalidToken(token = params.token, cpid = params.cpid))

        return ValidationResult.ok()
    }

    private fun getTenderProcessEntityByCpIdAndOcid(cpid: Cpid, ocid: Ocid): Result<TenderProcessEntity, Fail> {
        val entity = tenderProcessRepository.getByCpIdAndStage(cpid = cpid, stage = ocid.stage)
            .onFailure { return it }
            ?: return Result.failure(ValidationErrors.TenderNotFoundCheckAccessToTender(cpid = cpid, ocid = ocid))

        return Result.success(entity)
    }
}
