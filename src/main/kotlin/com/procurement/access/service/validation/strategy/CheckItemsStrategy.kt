package com.procurement.access.service.validation.strategy

import com.procurement.access.dao.TenderProcessDao
import com.procurement.access.domain.model.CPVCode
import com.procurement.access.domain.model.CPVCodePattern
import com.procurement.access.domain.model.patternBySymbols
import com.procurement.access.domain.model.patternOfGroups
import com.procurement.access.domain.model.startsWithPattern
import com.procurement.access.domain.model.toCPVCode
import com.procurement.access.exception.ErrorException
import com.procurement.access.exception.ErrorType
import com.procurement.access.infrastructure.dto.CheckItemsRequest
import com.procurement.access.infrastructure.dto.CheckItemsResponse
import com.procurement.access.model.dto.bpe.CommandMessage
import com.procurement.access.model.dto.ocds.Operation
import com.procurement.access.model.dto.ocds.TenderProcess
import com.procurement.access.utils.toObject

class CheckItemsStrategy(private val tenderProcessDao: TenderProcessDao) {

    /**
     * VR-3.14.1
     *
     * eAccess provides next steps:
     * eAccess checks the value of "operation type":
     * a. IF "operation type" == createCNonPN || createPINonPN || createNegotiationCnOnPn,
     *      eAccess checks the availability of Items object in saved version of tender (PN):
     *      IF there are NO Items object in saved version of tender, eAccess performs the next steps:
     *      1. Validates Item.Classification.ID received in Request by rule VR-3.14.2;
     *      2. Calculates tender.Classification.ID by rule BR-3.14.1;
     *      3. Compares tender.Classification.ID (got on step 1.a.i.2) with tender.Classification.ID
     *         from saved version of tender by rule VR-3.14.3;
     *      4. IF checking on step (1.a.i.3) is successful,
     *         eAccess returns "mdmValidation" == TRUE && calculated tender.Classification.ID && "itemsAdd" == TRUE;
     *    ELSE eAccess returns "mdmValidation" ==  FALSE & "itemsAdd" == TRUE;
     *
     * b. IF "operation type" == createPN || createPIN || createCN} eAccess performs the next steps:
     *   1. Validates Item.Classification.ID received in Request by rule VR-3.14.2;
     *   2. Calculates tender.Classification.ID by rule BR-3.14.1;
     *   3. eAccess returns "mdmValidation" == TRUE && calculated tender.Classification.ID && "itemsAdd" == TRUE;
     *
     * c. IF "operation type" == updatePN}, eAccess checks the availability of Items object in saved version of tender (PN):
     *      IF there are NO Items object in saved version of tender, eAccess performs the next steps:
     *      1. Validates Item.Classification.ID received in Request by rule VR-3.14.2;
     *      2. Calculates tender.Classification.ID by rule BR-3.14.1;
     *      3. Compares tender.Classification.ID (got on step 1.c.i.2) with tender.Classification.ID
     *         from saved version of tender by rule VR-3.14.3;
     *      4. IF checking on step (1.c.i.3) is successful,
     *         eAccess returns "mdmValidation" == TRUE && "itemsAdd" == TRUE && calculated tender.Classification.ID;
     *    ELSE Items object in saved version of tender is presented, eAccess returns "mdmValidation" == TRUE && "itemsAdd" == FALSE;
     */
    fun check(cm: CommandMessage): CheckItemsResponse {
        val contextRequest: CheckItemsContextRequest = getCheckItemsContext(cm)
        val request: CheckItemsRequest = toObject(CheckItemsRequest::class.java, cm.data)
        return when (contextRequest.operationType) {
            Operation.CREATE_CN_ON_PN,
            Operation.CREATE_PIN_ON_PN,
            Operation.CREATE_NEGOTIATION_CN_ON_PN -> {
                val process: TenderProcess = loadTenderProcess(contextRequest.cpid, contextRequest.prevStage)
                if (process.tender.items.isEmpty()) {
                    val cpvCodes = getCPVCodes(request)
                        .also {
                            checkItemsCPVCodes(it)
                        }

                    val calculatedCPVCode = calculateCPVCode(cpvCodes)
                        .also {
                            checkCalculatedCPVCode(
                                calculatedCPVCode = it,
                                tenderCPVCode = process.tender.classification.id
                            )
                        }

                    CheckItemsResponse(
                        mdmValidation = true,
                        itemsAdd = true,
                        tender = CheckItemsResponse.Tender(
                            classification = CheckItemsResponse.Tender.Classification(
                                id = calculatedCPVCode
                            )
                        )
                    )
                } else {
                    CheckItemsResponse(mdmValidation = false, itemsAdd = true)
                }
            }

            Operation.CREATE_CN,
            Operation.CREATE_PN,
            Operation.CREATE_PIN -> {
                val cpvCodes = getCPVCodes(request)
                    .also {
                        checkItemsCPVCodes(it)
                    }

                val calculatedTenderCPVCode = calculateCPVCode(cpvCodes)
                CheckItemsResponse(
                    mdmValidation = true,
                    itemsAdd = true,
                    tender = CheckItemsResponse.Tender(
                        classification = CheckItemsResponse.Tender.Classification(
                            id = calculatedTenderCPVCode
                        )
                    )
                )
            }

            Operation.UPDATE_PN -> {
                val process: TenderProcess = loadTenderProcess(contextRequest.cpid, contextRequest.stage)
                if (process.tender.items.isEmpty()) {
                    val cpvCodes = getCPVCodes(request)
                        .also {
                            checkItemsCPVCodes(it)
                        }

                    val calculatedCPVCode = calculateCPVCode(cpvCodes)
                        .also {
                            checkCalculatedCPVCode(
                                calculatedCPVCode = it,
                                tenderCPVCode = process.tender.classification.id
                            )
                        }

                    CheckItemsResponse(
                        mdmValidation = true,
                        itemsAdd = true,
                        tender = CheckItemsResponse.Tender(
                            classification = CheckItemsResponse.Tender.Classification(
                                id = calculatedCPVCode
                            )
                        )
                    )
                } else {
                    CheckItemsResponse(mdmValidation = true, itemsAdd = false)
                }
            }

            Operation.UPDATE_CN,
            Operation.CREATE_CN_ON_PIN -> CheckItemsResponse.resultUndefined()
        }
    }

    private fun loadTenderProcess(cpid: String, stage: String): TenderProcess {
        val entity = tenderProcessDao.getByCpIdAndStage(cpid, stage)
            ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
        return toObject(TenderProcess::class.java, entity.jsonData)
    }

    private fun getCPVCodes(request: CheckItemsRequest): List<CPVCode> = request.items.map { it.classification.id }

    /**
     * VR-3.14.2 CPV code (item)
     *
     * eAccess проверяет, что "CPV code ID"  (Item.Classification.id) каждого Item from Request
     * относятся к одному классу кодов:
     * - "CPV code ID"  (Item.Classification.id) каждого Item совпадают по первым 3-м цифрам в коде: XXXNNNNN-Y.
     * ELSE eAccess cancels the update || create process and returns error code.
     */
    private fun checkItemsCPVCodes(codes: List<CPVCode>) {
        if (codes.isEmpty())
            throw ErrorException(error = ErrorType.EMPTY_ITEMS, message = "Items must not be empty.")

        if (codes.size > 1) {
            val pattern: CPVCodePattern = codes.first().patternOfGroups
            if (!codes.startsWithPattern(pattern))
                throw ErrorException(error = ErrorType.ITEMS_CPV_CODES_NOT_CONSISTENT)
        }
    }

    /**
     * VR-3.14.3 CPV code (tender)
     *
     * eAccess checks that calculated tender.Classification.ID (by rule BR-3.14.1)
     * coincides with tender.Classification.ID from saved version of tender by FIRST 3 FIGURES: : XXXNNNNN.
     * ELSE eAccess cancels the update process and returns error code.
     */
    private fun checkCalculatedCPVCode(calculatedCPVCode: CPVCode, tenderCPVCode: CPVCode) {
        if (calculatedCPVCode.patternOfGroups != tenderCPVCode.patternOfGroups)
            throw ErrorException(error = ErrorType.CALCULATED_CPV_CODE_NO_MATCH_TENDER_CPV_CODE)
    }

    /**
     * BR-3.14.1 CPV code (tender)
     *
     * eAccess определяет "CPV code" (tender.Classification) по значениям,
     * переданным в "Item Classification" (item.Classification) из запроса:
     * 1. eAccess определяет совпадающую часть (от 3 до 7 знаков) путем сравнения кодов
     *    во всех item (item.Classification.id), поступивших в запросе.
     * 2. eAccess записывает полученное на шаге 1 значение в "ID" (tender.Classification.ID),
     *    остальные цифры в коде заполняются нулями (помимо контрольного числа).
     *
     *    Пример: 12340000, где "1234" - совпадающая часть, "0000" - подставленные нули.
     */
    private fun calculateCPVCode(codes: List<CPVCode>): CPVCode =
        generalPattern(codes, 3, 7).toCPVCode()

    private fun generalPattern(codes: List<CPVCode>, countFrom: Int, countTo: Int): CPVCodePattern {
        if (codes.size == 1)
            return codes.first().take(countTo)

        val firstCode: CPVCode = codes.first()
        for (countSymbols in countTo downTo countFrom) {
            val pattern: CPVCodePattern = firstCode.patternBySymbols(countSymbols)
            if (codes.startsWithPattern(pattern))
                return pattern
        }

        throw ErrorException(
            error = ErrorType.ITEMS_CPV_CODES_NOT_CONSISTENT,
            message = "CPV codes of all items must have minimum 3 the same starting symbols."
        )
    }

    private fun getCheckItemsContext(cm: CommandMessage): CheckItemsContextRequest {
        val cpid: String = cm.context.cpid
            ?: throw ErrorException(
                error = ErrorType.CONTEXT,
                message = "Missing the 'cpid' attribute in context."
            )
        val stage = cm.context.stage
            ?: throw ErrorException(
                error = ErrorType.CONTEXT,
                message = "Missing the 'prevStage' attribute in context."
            )
        val prevStage = cm.context.prevStage
            ?: throw ErrorException(
                error = ErrorType.CONTEXT,
                message = "Missing the 'prevStage' attribute in context."
            )
        val operationType = cm.context.operationType?.let { Operation.fromValue(it) }
            ?: throw ErrorException(
                error = ErrorType.CONTEXT,
                message = "Missing the 'operationType' attribute in context."
            )

        return CheckItemsContextRequest(
            cpid = cpid,
            stage = stage,
            prevStage = prevStage,
            operationType = operationType
        )
    }

    data class CheckItemsContextRequest(
        val cpid: String,
        val stage: String,
        val prevStage: String,
        val operationType: Operation
    )
}