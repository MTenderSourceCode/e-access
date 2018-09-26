package com.procurement.access.model.dto.cn

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.access.databinding.QuantityDeserializer
import com.procurement.access.exception.ErrorException
import com.procurement.access.exception.ErrorType
import com.procurement.access.model.dto.ocds.*
import com.procurement.access.model.dto.ocds.Unit
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

data class CnUpdate @JsonCreator constructor(

        val planning: PlanningCnUpdate,

        val tender: TenderCnUpdate
)

data class PlanningCnUpdate @JsonCreator constructor(

        val budget: BudgetCnUpdate,

        val rationale: String?
)

data class BudgetCnUpdate @JsonCreator constructor(

        val description: String?
)

data class TenderCnUpdate @JsonCreator constructor(

        val title: String,

        val description: String,

        val procurementMethodRationale: String?,

        val procurementMethodAdditionalInfo: String?,

        val classification: Classification?,

        val tenderPeriod: PeriodCnUpdate,

        val lots: List<LotCnUpdate>,

        val items: List<ItemCnUpdate>,

        val documents: List<Document>
)

data class LotCnUpdate @JsonCreator constructor(

        var id: String,

        val title: String,

        val description: String,

        val value: Value,

        val contractPeriod: ContractPeriod,

        val placeOfPerformance: PlaceOfPerformance
)

data class ItemCnUpdate @JsonCreator constructor(

        var id: String,

        val description: String?,

        val classification: Classification,

        val additionalClassifications: HashSet<Classification>?,

        @field:JsonDeserialize(using = QuantityDeserializer::class)
        val quantity: BigDecimal,

        val unit: Unit,

        var relatedLot: String
)

data class PeriodCnUpdate @JsonCreator constructor(

        val endDate: LocalDateTime
)

fun CnUpdate.validate(): CnUpdate {
    if (this.tender.items.isEmpty()) throw ErrorException(ErrorType.EMPTY_ITEMS)
    if (this.tender.lots.isEmpty()) throw ErrorException(ErrorType.EMPTY_LOTS)
    if (this.tender.documents.isEmpty()) throw ErrorException(ErrorType.EMPTY_DOCS)
    return this
}