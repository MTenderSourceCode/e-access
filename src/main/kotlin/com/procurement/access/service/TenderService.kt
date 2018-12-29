package com.procurement.access.service

import com.procurement.access.dao.TenderProcessDao
import com.procurement.access.exception.ErrorException
import com.procurement.access.exception.ErrorType.*
import com.procurement.access.model.bpe.CommandMessage
import com.procurement.access.model.bpe.ResponseDto
import com.procurement.access.model.dto.lots.CancellationRs
import com.procurement.access.model.dto.lots.LotCancellation
import com.procurement.access.model.dto.lots.UpdateLotsRs
import com.procurement.access.model.dto.ocds.*
import com.procurement.access.model.dto.tender.*
import com.procurement.access.model.entity.TenderProcessEntity
import com.procurement.access.utils.localNowUTC
import com.procurement.access.utils.toDate
import com.procurement.access.utils.toJson
import com.procurement.access.utils.toObject
import org.springframework.stereotype.Service

@Service
class TenderService(private val tenderProcessDao: TenderProcessDao) {

    fun setSuspended(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(CONTEXT)

        val entity = tenderProcessDao.getByCpIdAndStage(cpId, stage) ?: throw ErrorException(DATA_NOT_FOUND)
        val process = toObject(TenderProcess::class.java, entity.jsonData)
        process.tender.statusDetails = TenderStatusDetails.SUSPENDED
        tenderProcessDao.save(getEntity(process, entity))
        return ResponseDto(data = UpdateTenderStatusRs(
                process.tender.status.value,
                process.tender.statusDetails.value))
    }

    fun setUnsuspended(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(CONTEXT)
        val phase = cm.context.phase ?: throw ErrorException(CONTEXT)

        val entity = tenderProcessDao.getByCpIdAndStage(cpId, stage) ?: throw ErrorException(DATA_NOT_FOUND)
        val process = toObject(TenderProcess::class.java, entity.jsonData)
        if (process.tender.statusDetails == TenderStatusDetails.SUSPENDED) {
            process.tender.statusDetails = TenderStatusDetails.fromValue(phase)
        } else {
            throw ErrorException(IS_NOT_SUSPENDED)
        }
        tenderProcessDao.save(getEntity(process, entity))
        return ResponseDto(data = UnsuspendedTenderRs(UnsuspendedTender(
                process.tender.status.value,
                process.tender.statusDetails.value,
                process.tender.procurementMethodModalities,
                process.tender.electronicAuctions)))
    }

    fun setUnsuccessful(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(CONTEXT)

        val entity = tenderProcessDao.getByCpIdAndStage(cpId, stage) ?: throw ErrorException(DATA_NOT_FOUND)
        val process = toObject(TenderProcess::class.java, entity.jsonData)
        process.tender.apply {
            status = TenderStatus.UNSUCCESSFUL
            statusDetails = TenderStatusDetails.EMPTY
            lots.forEach { lot ->
                lot.status = LotStatus.UNSUCCESSFUL
                lot.statusDetails = LotStatusDetails.EMPTY

            }
        }
        tenderProcessDao.save(getEntity(process, entity))
        return ResponseDto(data = UpdateLotsRs(
                process.tender.status,
                process.tender.statusDetails,
                process.tender.lots, null))
    }

    fun setPreCancellation(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(CONTEXT)
        val owner = cm.context.owner ?: throw ErrorException(CONTEXT)
        val token = cm.context.token ?: throw ErrorException(CONTEXT)
        val operationType = cm.context.operationType ?: throw ErrorException(CONTEXT)

        val entity = tenderProcessDao.getByCpIdAndStage(cpId, stage) ?: throw ErrorException(DATA_NOT_FOUND)
        if (entity.owner != owner) throw ErrorException(INVALID_OWNER)
        if (entity.token.toString() != token) throw ErrorException(INVALID_TOKEN)
        val process = toObject(TenderProcess::class.java, entity.jsonData)
        validateTenderStatusForPrepareCancellation(process, operationType)
        val lotStatusPredicate = getLotStatusPredicateForPrepareCancellation(operationType)
        val lotsResponseDto = mutableListOf<LotCancellation>()
        process.tender.apply {
            statusDetails = TenderStatusDetails.CANCELLATION
            lots.asSequence()
                    .filter(lotStatusPredicate)
                    .forEach { lot ->
                        lot.statusDetails = LotStatusDetails.CANCELLED
                        addLotToLotsResponseDto(lotsResponseDto, lot)
                    }
        }
        tenderProcessDao.save(getEntity(process, entity))
        return ResponseDto(data = CancellationRs(lots = lotsResponseDto))
    }


    fun setCancellation(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(CONTEXT)
        val owner = cm.context.owner ?: throw ErrorException(CONTEXT)
        val token = cm.context.token ?: throw ErrorException(CONTEXT)
        val operationType = cm.context.operationType ?: throw ErrorException(CONTEXT)

        val entity = tenderProcessDao.getByCpIdAndStage(cpId, stage) ?: throw ErrorException(DATA_NOT_FOUND)
        if (entity.owner != owner) throw ErrorException(INVALID_OWNER)
        if (entity.token.toString() != token) throw ErrorException(INVALID_TOKEN)
        val process = toObject(TenderProcess::class.java, entity.jsonData)
        validateTenderStatusForCancellation(process, operationType)
        val lotStatusPredicate = getLotStatusPredicateForCancellation(operationType)
        val lotsResponseDto = mutableListOf<LotCancellation>()
        process.tender.apply {
            status = TenderStatus.CANCELLED
            statusDetails = TenderStatusDetails.EMPTY
            lots.asSequence()
                    .filter(lotStatusPredicate)
                    .forEach { lot ->
                        lot.status = LotStatus.CANCELLED
                        lot.statusDetails = LotStatusDetails.EMPTY
                        addLotToLotsResponseDto(lotsResponseDto, lot)
                    }
        }
        tenderProcessDao.save(getEntity(process, entity))
        return ResponseDto(data = CancellationRs(lots = lotsResponseDto))
    }

    fun setStatusDetails(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(CONTEXT)
        val phase = cm.context.phase ?: throw ErrorException(CONTEXT)

        val entity = tenderProcessDao.getByCpIdAndStage(cpId, stage) ?: throw ErrorException(DATA_NOT_FOUND)
        val process = toObject(TenderProcess::class.java, entity.jsonData)
        process.tender.statusDetails = TenderStatusDetails.fromValue(phase)
        tenderProcessDao.save(getEntity(process, entity))
        return ResponseDto(data = UpdateTenderStatusRs(process.tender.status.value, process.tender.statusDetails.value))
    }

    fun getTenderOwner(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(CONTEXT)

        val entity = tenderProcessDao.getByCpIdAndStage(cpId, stage) ?: throw ErrorException(DATA_NOT_FOUND)
        return ResponseDto(data = GetTenderOwnerRs(entity.owner))
    }

    fun getDataForAc(cm: CommandMessage): ResponseDto {

        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(CONTEXT)
        val dto = toObject(GetDataForAcRq::class.java, cm.data)
        val lotsIdsSet = dto.awards.asSequence().map { it.relatedLots[0] }.toSet()

        val entity = tenderProcessDao.getByCpIdAndStage(cpId, stage) ?: throw ErrorException(DATA_NOT_FOUND)
        val process = toObject(TenderProcess::class.java, entity.jsonData)
        val lots = process.tender.lots.asSequence().filter { lotsIdsSet.contains(it.id) }.toList()
        if (lots.asSequence().any { it.status == LotStatus.CANCELLED || it.status == LotStatus.UNSUCCESSFUL }) {
            throw ErrorException(INVALID_LOTS_STATUS)
        }
        val items = process.tender.items.asSequence().filter { lotsIdsSet.contains(it.relatedLot) }.toList()
        val contractedTender = GetDataForAcTender(
                id = process.tender.id,
                classification = process.tender.classification,
                procurementMethod = process.tender.procurementMethod,
                procurementMethodDetails = process.tender.procurementMethodDetails,
                mainProcurementCategory = process.tender.mainProcurementCategory,
                lots = lots,
                items = items)
        return ResponseDto(data = GetDataForAcRs(contractedTender))
    }

    private fun getLotStatusPredicateForPrepareCancellation(operationType: String): (Lot) -> Boolean {
        return when (operationType) {
            "cancelTender", "cancellationStandstillPeriod" -> { lot: Lot ->
                (lot.status == LotStatus.ACTIVE)
                        && (lot.statusDetails == LotStatusDetails.EMPTY || lot.statusDetails == LotStatusDetails.AWARDED)
            }
            "cancelPlan" -> { lot: Lot ->
                (lot.status == LotStatus.PLANNING || lot.status == LotStatus.PLANNED)
                        && (lot.statusDetails == LotStatusDetails.EMPTY)
            }
            else -> {
                throw ErrorException(INVALID_OPERATION_TYPE)
            }
        }
    }

    private fun getLotStatusPredicateForCancellation(operationType: String): (Lot) -> Boolean {
        return when (operationType) {
            "cancelTender", "cancelTenderEv" -> { lot: Lot ->
                (lot.status == LotStatus.ACTIVE)
                        && (lot.statusDetails == LotStatusDetails.CANCELLED)
            }
            "cancelPlan" -> { lot: Lot ->
                (lot.status == LotStatus.PLANNING || lot.status == LotStatus.PLANNED)
                        && (lot.statusDetails == LotStatusDetails.EMPTY)
            }
            else -> {
                throw ErrorException(INVALID_OPERATION_TYPE)
            }
        }
    }

    private fun validateTenderStatusForPrepareCancellation(process: TenderProcess, operationType: String) {
        when (operationType) {
            "cancelTender", "cancellationStandstillPeriod" -> {
                if (process.tender.status != TenderStatus.ACTIVE)
                    throw ErrorException(TENDER_IN_UNSUCCESSFUL_STATUS)
            }
        }
    }

    private fun validateTenderStatusForCancellation(process: TenderProcess, operationType: String) {
        when (operationType) {
            "cancelTender", "cancelTenderEv" -> {
                if (process.tender.status != TenderStatus.ACTIVE)
                    throw ErrorException(TENDER_IN_UNSUCCESSFUL_STATUS)
                if (process.tender.statusDetails != TenderStatusDetails.CANCELLATION)
                    throw ErrorException(TENDER_IN_UNSUCCESSFUL_STATUS)
            }
            "cancelPlan" -> {
                if (process.tender.status != TenderStatus.PLANNING && process.tender.status != TenderStatus.PLANNED)
                    throw ErrorException(TENDER_IN_UNSUCCESSFUL_STATUS)
            }
        }
    }

    private fun addLotToLotsResponseDto(lotsResponseDto: MutableList<LotCancellation>, lot: Lot) {
        lotsResponseDto.add(LotCancellation(
                id = lot.id,
                status = lot.status,
                statusDetails = lot.statusDetails))
    }

    private fun getEntity(process: TenderProcess,
                          entity: TenderProcessEntity): TenderProcessEntity {

        return TenderProcessEntity(
                cpId = entity.cpId,
                token = entity.token,
                stage = entity.stage,
                owner = entity.owner,
                createdDate = localNowUTC().toDate(),
                jsonData = toJson(process)
        )
    }
}
