package com.procurement.access.model.dto.bpe

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.JsonNode
import com.procurement.access.domain.model.enums.OperationType
import com.procurement.access.domain.model.enums.ProcurementMethod
import com.procurement.access.domain.model.lot.LotId
import com.procurement.access.exception.EnumElementProviderException
import com.procurement.access.exception.ErrorException
import com.procurement.access.exception.ErrorType
import com.procurement.access.utils.toLocal
import java.time.LocalDateTime
import java.util.*

data class CommandMessage @JsonCreator constructor(

    val id: String,
    val command: CommandType,
    val context: Context,
    val data: JsonNode,
    val version: ApiVersion
)

val CommandMessage.cpid: String
    get() = this.context.cpid
        ?: throw ErrorException(error = ErrorType.CONTEXT, message = "Missing the 'cpid' attribute in context.")

val CommandMessage.token: UUID
    get() = this.context
        .token
        ?.let { token ->
            try {
                UUID.fromString(token)
            } catch (exception: Exception) {
                throw ErrorException(error = ErrorType.INVALID_FORMAT_TOKEN, message = "Expected token format is UUID, actual token=$token.")
            }
        }
        ?: throw ErrorException(error = ErrorType.CONTEXT, message = "Missing the 'token' attribute in context.")

val CommandMessage.owner: String
    get() = this.context.owner
        ?: throw ErrorException(error = ErrorType.CONTEXT, message = "Missing the 'owner' attribute in context.")

val CommandMessage.stage: String
    get() = this.context.stage
        ?: throw ErrorException(error = ErrorType.CONTEXT, message = "Missing the 'stage' attribute in context.")

val CommandMessage.prevStage: String
    get() = this.context.prevStage
        ?: throw ErrorException(error = ErrorType.CONTEXT, message = "Missing the 'prevStage' attribute in context.")

val CommandMessage.country: String
    get() = this.context.country
        ?: throw ErrorException(error = ErrorType.CONTEXT, message = "Missing the 'country' attribute in context.")

val CommandMessage.phase: String
    get() = this.context.phase
        ?: throw ErrorException(error = ErrorType.CONTEXT, message = "Missing the 'phase' attribute in context.")

val CommandMessage.pmd: ProcurementMethod
    get() = this.context.pmd?.let {
        ProcurementMethod.creator(it)
    } ?: throw ErrorException(error = ErrorType.CONTEXT, message = "Missing the 'pmd' attribute in context.")

val CommandMessage.operationType: OperationType
    get() = this.context.operationType?.let { OperationType.creator(it) }
        ?: throw ErrorException(
            error = ErrorType.CONTEXT,
            message = "Missing the 'operationType' attribute in context."
        )

val CommandMessage.startDate: LocalDateTime
    get() = this.context.startDate?.toLocal()
        ?: throw ErrorException(error = ErrorType.CONTEXT, message = "Missing the 'startDate' attribute in context.")

val CommandMessage.testMode: Boolean
    get() = this.context.testMode?.let { it } ?: false

val CommandMessage.isAuction: Boolean
    get() = this.context.isAuction?.let { it } ?: false

val CommandMessage.lotId: LotId
    get() = this.context.id?.let { id ->
        try {
            LotId.fromString(id)
        } catch (exception: Exception) {
            throw ErrorException(error = ErrorType.INVALID_FORMAT_LOT_ID)
        }
    } ?: throw ErrorException(error = ErrorType.CONTEXT, message = "Missing the 'id' attribute in context.")

data class Context @JsonCreator constructor(
    val operationId: String?,
    val requestId: String?,
    val cpid: String?,
    val ocid: String?,
    val stage: String?,
    val prevStage: String?,
    val processType: String?,
    val operationType: String?,
    val phase: String?,
    val owner: String?,
    val country: String?,
    val language: String?,
    val pmd: String?,
    val token: String?,
    val startDate: String?,
    val endDate: String?,
    @get:JsonProperty("isAuction") @param:JsonProperty("isAuction") val isAuction: Boolean?,
    val id: String?,
    val testMode: Boolean?
)

enum class CommandType(private val value: String) {

    CHECK_AWARD("checkAward"),
    CHECK_BID("checkBid"),
    CHECK_BUDGET_SOURCES("checkBudgetSources"),
    CHECK_CN_ON_PN("checkCnOnPn"),
    CHECK_EXISTANCE_ITEMS_AND_LOTS("checkExistanceItemsAndLots"),
    CHECK_FE_DATA("checkFEData"),
    CHECK_ITEMS("checkItems"),
    CHECK_LOTS_STATUS("checkLotsStatus"),
    CHECK_LOT_ACTIVE("checkLotActive"),
    CHECK_LOT_AWARDED("checkLotAwarded"),
    CHECK_LOT_STATUS("checkLotStatus"),
    CHECK_RESPONSES("checkResponses"),
    CHECK_TOKEN("checkToken"),
    COMPLETE_LOTS("completeLots"),
    CREATE_AP("createAp"),
    CREATE_CN("createCn"),
    CREATE_CN_ON_PIN("createCnOnPin"),
    CREATE_CN_ON_PN("createCnOnPn"),
    CREATE_PIN("createPin"),
    CREATE_PIN_ON_PN("createPinOnPn"),
    CREATE_PN("createPn"),
    CREATE_REQUESTS_FOR_EV_PANELS("createRequestsForEvPanels"),
    GET_ACTIVE_LOTS("getActiveLots"),
    GET_AWARD_CRITERIA("getAwardCriteria"),
    GET_AWARD_CRITERIA_AND_CONVERSIONS("getAwardCriteriaAndConversions"),
    GET_DATA_FOR_AC("getDataForAc"),
    GET_ITEMS_BY_LOT("getItemsByLot"),
    GET_LOT("getLot"),
    GET_LOTS_AUCTION("getLotsAuction"),
    GET_LOTS_FOR_AUCTION("getLotsForAuction"),
    GET_TENDER_OWNER("getTenderOwner"),
    SET_FINAL_STATUSES("setFinalStatuses"),
    SET_LOTS_INITIAL_STATUS("setLotInitialStatus"),
    SET_LOTS_SD_AWARDED("setLotsStatusDetailsAwarded"),
    SET_LOTS_SD_UNSUCCESSFUL("setLotsStatusDetailsUnsuccessful"),
    SET_LOTS_UNSUCCESSFUL("setLotsStatusUnsuccessful"),
    SET_TENDER_CANCELLATION("setTenderCancellation"),
    SET_TENDER_PRECANCELLATION("setTenderPreCancellation"),
    SET_TENDER_STATUS_DETAILS("setTenderStatusDetails"),
    SET_TENDER_SUSPENDED("setTenderSuspended"),
    SET_TENDER_UNSUCCESSFUL("setTenderUnsuccessful"),
    SET_TENDER_UNSUSPENDED("setTenderUnsuspended"),
    START_NEW_STAGE("startNewStage"),
    UPDATE_AP("updateAp"),
    UPDATE_CN("updateCn"),
    UPDATE_PN("updatePn"),
    VALIDATE_OWNER_AND_TOKEN("validateOwnerAndToken");

    @JsonValue
    fun value(): String {
        return this.value
    }

    override fun toString(): String {
        return this.value
    }
}

enum class ApiVersion(private val value: String) {
    V_0_0_1("0.0.1");

    @JsonValue
    fun value(): String {
        return this.value
    }

    override fun toString(): String {
        return this.value
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseDto(

    val errors: List<ResponseErrorDto>? = null,

    val data: Any? = null,

    val id: String? = null
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseErrorDto(

    val code: String,

    val description: String?
)

fun getExceptionResponseDto(exception: Exception): ResponseDto {
    return ResponseDto(
        errors = listOf(
            ResponseErrorDto(
                code = "400.03.00",
                description = exception.message ?: exception.toString()
            )
        )
    )
}

fun getErrorExceptionResponseDto(exception: ErrorException, id: String? = null): ResponseDto {
    return ResponseDto(
        errors = listOf(
            ResponseErrorDto(
                code = "400.03." + exception.error.code,
                description = exception.message
            )
        ),
        id = id
    )
}

fun getEnumExceptionResponseDto(error: EnumElementProviderException, id: String? = null): ResponseDto {
    return ResponseDto(
        errors = listOf(
            ResponseErrorDto(
                code = "400.03." + error.code,
                description = error.message
            )
        ),
        id = id
    )
}
