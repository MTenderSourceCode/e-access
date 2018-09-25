package com.procurement.access.service

import com.procurement.access.dao.TenderProcessDao
import com.procurement.access.exception.ErrorException
import com.procurement.access.exception.ErrorType.*
import com.procurement.access.model.bpe.CommandMessage
import com.procurement.access.model.bpe.ResponseDto
import com.procurement.access.model.dto.lots.*
import com.procurement.access.model.dto.ocds.*
import com.procurement.access.utils.toJson
import com.procurement.access.utils.toObject
import org.springframework.stereotype.Service
import java.util.*

interface LotsService {

    fun getLots(cm: CommandMessage): ResponseDto

    fun setStatusDetailsUnsuccessful(cm: CommandMessage): ResponseDto

    fun setStatusDetailsAwarded(cm: CommandMessage): ResponseDto

    fun setStatusUnsuccessful(cm: CommandMessage): ResponseDto

    fun setStatusUnsuccessfulEv(cm: CommandMessage): ResponseDto

}

@Service
class LotsServiceImpl(private val tenderProcessDao: TenderProcessDao) : LotsService {

    override fun getLots(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(CONTEXT)

        val entity = tenderProcessDao.getByCpIdAndStage(cpId, stage) ?: throw ErrorException(DATA_NOT_FOUND)
        val process = toObject(TenderProcess::class.java, entity.jsonData)
        return ResponseDto(data = GetLotsRs(
                awardCriteria = process.tender.awardCriteria.value(),
                lots = getLotsDtoByStatus(process.tender.lots, LotStatus.ACTIVE))
        )
    }

    override fun setStatusDetailsUnsuccessful(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(CONTEXT)
        val lotsDto = toObject(UpdateLotsRq::class.java, cm.data)

        val entity = tenderProcessDao.getByCpIdAndStage(cpId, stage) ?: throw ErrorException(DATA_NOT_FOUND)
        val process = toObject(TenderProcess::class.java, entity.jsonData)
        process.tender.apply {
            setLotsStatusDetails(lots, lotsDto, LotStatusDetails.UNSUCCESSFUL)
        }
        entity.jsonData = toJson(process)
        tenderProcessDao.save(entity)
        return ResponseDto(data = UpdateLotsRs(
                tenderStatus = process.tender.status,
                tenderStatusDetails = process.tender.statusDetails,
                lots = process.tender.lots,
                items = null))
    }

    override fun setStatusDetailsAwarded(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(CONTEXT)
        val dto = toObject(UpdateLotByBidRq::class.java, cm.data)

        val entity = tenderProcessDao.getByCpIdAndStage(cpId, stage) ?: throw ErrorException(DATA_NOT_FOUND)
        val process = toObject(TenderProcess::class.java, entity.jsonData)
        var statusDetails = if (dto.lotAwarded) {
            LotStatusDetails.AWARDED
        } else {
            LotStatusDetails.EMPTY
        }
        val updatedLot = setLotsStatusDetails(process.tender.lots, dto.lotId, statusDetails)
        entity.jsonData = toJson(process)
        tenderProcessDao.save(entity)
        return ResponseDto(data = UpdateLotByBidRs(updatedLot))
    }

    override fun setStatusUnsuccessful(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(CONTEXT)
        val phase = cm.context.phase ?: throw ErrorException(CONTEXT)
        val lotsDto = toObject(UpdateLotsRq::class.java, cm.data)

        val entity = tenderProcessDao.getByCpIdAndStage(cpId, stage) ?: throw ErrorException(DATA_NOT_FOUND)
        val process = toObject(TenderProcess::class.java, entity.jsonData)
        process.tender.apply {
            setLotsStatus(lots, lotsDto)
            if (!isAnyActiveLots(lots)) {
                status = TenderStatus.UNSUCCESSFUL
                statusDetails = TenderStatusDetails.EMPTY
            } else {
                statusDetails = TenderStatusDetails.fromValue(phase)
            }
        }
        entity.jsonData = toJson(process)
        tenderProcessDao.save(entity)
        return ResponseDto(data = UpdateLotsRs(process.tender.status, process.tender.statusDetails, process.tender.lots, null))
    }

    override fun setStatusUnsuccessfulEv(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val stage = cm.context.stage ?: throw ErrorException(CONTEXT)
        val lotsDto = toObject(UpdateLotsRq::class.java, cm.data)

        val entity = tenderProcessDao.getByCpIdAndStage(cpId, stage) ?: throw ErrorException(DATA_NOT_FOUND)
        val process = toObject(TenderProcess::class.java, entity.jsonData)
        var itemsForCompiledLots: List<Item>? = null
        process.tender.apply {
            setLotsStatusEv(lots, lotsDto)
            if (isAnyCompleteLots(lots)) {
                itemsForCompiledLots = getItemsForCompiledLots(items, lots)
            } else {
                status = TenderStatus.UNSUCCESSFUL
                statusDetails = TenderStatusDetails.EMPTY
            }
        }
        entity.jsonData = toJson(process)
        tenderProcessDao.save(entity)
        return ResponseDto(data = UpdateLotsRs(
                tenderStatus = process.tender.status,
                tenderStatusDetails = process.tender.statusDetails,
                lots = process.tender.lots,
                items = itemsForCompiledLots))
    }

    private fun getLotsDtoByStatus(lots: List<Lot>, status: LotStatus): List<LotDto> {
        if (lots.isEmpty()) throw ErrorException(NO_ACTIVE_LOTS)
        val lotsByStatus = lots.asSequence()
                .filter { it.status == status }
                .map { LotDto(it.id) }.toList()
        if (lotsByStatus.isEmpty()) throw ErrorException(NO_ACTIVE_LOTS)
        return lotsByStatus
    }

    private fun setLotsStatus(lots: List<Lot>, updateLotsDto: UpdateLotsRq) {
        if (lots.isEmpty()) throw ErrorException(NO_ACTIVE_LOTS)
        val lotsIds = updateLotsDto.unsuccessfulLots?.asSequence()?.map { it.id }?.toHashSet() ?: HashSet()
        lots.forEach { lot ->
            if (lot.id in lotsIds) lot.status = LotStatus.UNSUCCESSFUL
            if (lot.statusDetails == LotStatusDetails.UNSUCCESSFUL) lot.statusDetails = LotStatusDetails.EMPTY
        }
    }

    private fun setLotsStatusDetails(lots: List<Lot>, updateLotsDto: UpdateLotsRq, statusDetails: LotStatusDetails) {
        if (lots.isEmpty()) throw ErrorException(NO_ACTIVE_LOTS)
        val lotsIds = updateLotsDto.unsuccessfulLots?.asSequence()?.map { it.id }?.toHashSet() ?: HashSet()
        lots.forEach { lot ->
            if (lot.id in lotsIds) lot.statusDetails = statusDetails
        }
    }

    private fun isAnyActiveLots(lots: List<Lot>?): Boolean {
        return lots?.asSequence()
                ?.any { it.status == LotStatus.ACTIVE && it.statusDetails == LotStatusDetails.EMPTY }
                ?: false
    }

    private fun setLotsStatusDetails(lots: List<Lot>?, lotId: String, lotStatusDetails: LotStatusDetails): Lot {
        return lots?.asSequence()
                ?.filter { it.id == lotId }
                ?.first()
                ?.apply { statusDetails = lotStatusDetails }
                ?: throw ErrorException(DATA_NOT_FOUND)
    }

    private fun checkLotStatusDetails(lots: List<Lot>) {
        val predicate = { lot: Lot -> lot.status == LotStatus.ACTIVE && lot.statusDetails != LotStatusDetails.AWARDED }
        if (lots.asSequence().any(predicate)) throw ErrorException(NOT_ALL_LOTS_AWARDED)
    }

    private fun getItemsForCompiledLots(items: List<Item>?, lots: List<Lot>): List<Item>? {
        if (items != null) {
            val lotsIds = lots.asSequence()
                    .filter { it.status == LotStatus.COMPLETE && it.statusDetails == LotStatusDetails.EMPTY }
                    .map { it.id }.toHashSet()
            return items.asSequence().filter { lotsIds.contains(it.relatedLot) }.toList()
        }
        return null
    }

    private fun setLotsStatusEv(lots: List<Lot>, updateLotsDto: UpdateLotsRq) {
        if (lots.isEmpty()) throw ErrorException(NO_ACTIVE_LOTS)
        val lotsIds = updateLotsDto.unsuccessfulLots?.asSequence()?.map { it.id }?.toHashSet() ?: HashSet()
        lots.forEach { lot ->
            if (lot.id in lotsIds) {
                lot.status = LotStatus.UNSUCCESSFUL
                lot.statusDetails = LotStatusDetails.EMPTY
            }
        }
        lots.forEach { lot ->
            if (lot.status == LotStatus.ACTIVE && lot.statusDetails == LotStatusDetails.AWARDED) {
                lot.status = LotStatus.COMPLETE
                lot.statusDetails = LotStatusDetails.EMPTY
            }
        }
    }

    private fun isAnyCompleteLots(lots: List<Lot>?): Boolean {
        return if (lots != null && !lots.isEmpty()) {
            lots.asSequence()
                    .any { it.status == LotStatus.COMPLETE && it.statusDetails == LotStatusDetails.EMPTY }
        } else false
    }
}
