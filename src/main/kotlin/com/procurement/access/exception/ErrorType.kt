package com.procurement.access.exception

enum class ErrorType constructor(val code: String, val message: String) {
    INVALID_JSON_TYPE("00.00", "Invalid type: "),
    DATA_NOT_FOUND("00.01", "Data not found."),
    INVALID_OWNER("00.02", "Invalid owner."),
    OCID_NOT_NULL("00.03", "Ocid must be empty."),
    TOKEN_NOT_NULL("00.04", "Token id must be empty."),
    TENDER_ID_NOT_NULL("00.05", "Tender id must be empty."),
    TENDER_STATUS_NOT_NULL("00.06", "Tender status must be empty."),
    TENDER_STATUS_DETAILS_NOT_NULL("00.07", "Tender status details must be empty."),
    LOT_STATUS_NOT_NULL("00.08", "Lot status must be empty."),
    LOT_STATUS_DETAILS_NOT_NULL("00.09", "Lot status details must be empty."),
    PERIOD_NOT_NULL("00.10", "Tender period must be empty."),
    IDENTIFIER_IS_NULL("00.11", "Identifier of organization must be not empty."),
    NOT_ACTIVE("10.01", "The tender procedure is not in active state."),
    NOT_INTERMEDIATE("10.02", "The tender procedure is not in any of the intermediate states."),
    NO_ACTIVE_LOTS("10.03", "There is no lot in the active state."),
    INVALID_TOKEN("10.04", "Invalid token."),
    INVALID_CPID_FROM_DTO("10.05", "Invalid tender id."),
    INVALID_DOCS_RELATED_LOTS("10.06", "Invalid documents related lots."),
    EL_CRITERIA_IS_NULL("10.07", "EligibilityCriteria must be presented!"),
    INVALID_START_DATE("10.08", "Invalid tender period start date."),
    INVALID_CURRENCY("10.09", "Invalid currency."),
    NO_AWARDED_LOT("10.10", "Awarding by lot is not completed."),
    INVALID_ITEMS("10.11", "Invalid items id."),
    INVALID_LOT_CONTRACT_PERIOD("10.12", "Invalid contract period of lot."),
    INVALID_PMD("10.13", "Invalid pmd."),
    INVALID_LOT_AMOUNT("10.14", "Invalid lot amount."),
    INVALID_LOT_CURRENCY("10.15", "Invalid lot currency."),
    INVALID_LOT_ID("10.16", "Invalid lot id."),
    INVALID_ITEMS_RELATED_LOTS("10.17", "Invalid items related lots."),
    BID_VALUE_MORE_THAN_SUM_LOT("10.18", "Bid amount is more than amount of lot!"),
    INVALID_LOT_STATUS("10.19", "Related lot must be in status active and status details empty."),
    LOT_NOT_FOUND("10.20", "Lot not found"),
    TENDER_IN_UNSUCCESSFUL_STATUS("10.21", "Invalid tender status for cancellation."),
    INVALID_TENDER_STATUS("10.22", "Invalid Tender status."),
    INVALID_OPERATION_TYPE("10.23", "Invalid operation type."),
    INVALID_CPV_CODE("10.24", "Invalid cpv code."),
    INVALID_DOCS_ID("10.25", "Invalid documents ids."),
    IS_SUSPENDED("10.26", "Tender is suspended."),
    IS_NOT_SUSPENDED("10.27", "Invalid tender status details. Please retry saving the answer."),
    EMPTY_ITEMS("10.28", "Items must not be empty."),
    EMPTY_LOTS("10.29", "Lots must not be empty."),
    EMPTY_DOCS("10.30", "Documents must not be empty."),
    INVALID_AUCTION_RELATED_LOTS("10.31", "Invalid auctions related lots."),
    INVALID_AUCTION_IS_EMPTY("10.32", "Electronic auctions must not be empty."),
    INVALID_PMM("10.33", "Procurement method modalities must not be empty."),
    INVALID_AUCTION_MINIMUM("10.34", "Invalid auction minimum."),
    INVALID_AUCTION_CURRENCY("10.35", "Invalid auction currency."),
    RULES_NOT_FOUND("10.36", "Rules not found."),
    INVALID_AUCTION_ID("10.37", "Invalid auctions ids."),
    INVALID_BS("10.38", "Invalid budget sources."),
    EMPTY_BREAKDOWN("10.39", "Budget breakdowns must not be empty."),
    INVALID_AUCTION_IS_NON_EMPTY("10.40", "Electronic auctions must be empty."),
    INVALID_LOTS_STATUS("10.41", "Invalid lots status."),
    CONTEXT("20.01", "Context parameter not found."),
    TENDER_CONTAIN_DUPLICATE_LOT_ID("10.42", "The tender contain a duplicate lot id."),
    LOT_ID_NOT_MATCH_TO_RELATED_LOT_IN_AUCTIONS("10.44", "Lot id in the tender do not match related lot in the auctions."),
    NUMBER_AUCTIONS_NOT_MATCH_TO_LOTS("10.45", "The number of auctions does not match the number of lots."),
    LOT_ID_NOT_MATCH_TO_RELATED_LOT_IN_ITEMS("10.46", "Lot id in the tender do not match related lot in the items."),
    LOT_ID_DUPLICATED("10.47", "Lot id duplicated."),
    AUCTION_ID_DUPLICATED("10.48", "Auction id duplicated."),
    AUCTIONS_CONTAIN_DUPLICATE_RELATED_LOTS("10.49", "Auctions contain a duplicate related lots."),
    ITEM_ID_IS_DUPLICATED("10.50", "Item id duplicated."),
    INVALID_ITEMS_QUANTITY("10.51", "Invalid quantity value in item."),
    INVALID_TENDER_AMOUNT("10.52", "Invalid amount of the tender."),
    ITEMS_CPV_CODES_NOT_CONSISTENT("10.53", "CPV codes of all items must have minimum 3 the same starting symbols."),
    CALCULATED_CPV_CODE_NO_MATCH_TENDER_CPV_CODE("10.54", "The calculated CPV code does not match the CPV code in the tender."),
    AWARD_RELATED_TO_UNKNOWN_LOT("10.55", "The award related to an unknown lot."),
    AWARD_ON_LOT_IN_INVALID_STATUS("10.56", "The lot related to an award has invalid status."),
    AWARD_HAS_INVALID_AMOUNT_VALUE("10.57", "The award has an invalid amount value."),
    AWARD_HAS_INVALID_CURRENCY_VALUE("10.58", "The award has an invalid currency value."),
    AUCTION_LOT_HAS_INVALID_AMOUNT_VALUE("10.59", "The lot in auction has an invalid amount value."),
    AUCTION_LOT_HAS_INVALID_CURRENCY_VALUE("10.60", "The lot in auction has an invalid currency value.");
}
