package com.procurement.access.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.util.*


enum class ProcurementMethodModalities(@JsonValue val value: String) {
    ELECTRONIC_AUCTION("electronicAuction");

    override fun toString(): String {
        return this.value
    }
}

enum class AwardCriteria(@JsonValue val value: String) {
    PRICE_ONLY("priceOnly"),
    COST_ONLY("costOnly"),
    QUALITY_ONLY("qualityOnly"),
    RATED_CRITERIA("ratedCriteria");

    override fun toString(): String {
        return this.value
    }
}

enum class ExtendedProcurementCategory(@JsonValue val value: String) {
    GOODS("goods"),
    WORKS("works"),
    SERVICES("services"),
    CONSULTING_SERVICES("consultingServices");

    override fun toString(): String {
        return this.value
    }
}

enum class LegalBasis(@JsonValue val value: String) {
    DIRECTIVE_2014_23_EU("DIRECTIVE_2014_23_EU"),
    DIRECTIVE_2014_24_EU("DIRECTIVE_2014_24_EU"),
    DIRECTIVE_2014_25_EU("DIRECTIVE_2014_25_EU"),
    DIRECTIVE_2009_81_EC("DIRECTIVE_2009_81_EC"),
    REGULATION_966_2012("REGULATION_966_2012"),
    NATIONAL_PROCUREMENT_LAW("NATIONAL_PROCUREMENT_LAW"),
    NULL("NULL");

    override fun toString(): String {
        return this.value
    }
}

enum class MainProcurementCategory(@JsonValue val value: String) {
    GOODS("goods"),
    WORKS("works"),
    SERVICES("services");

    override fun toString(): String {
        return this.value
    }
}

enum class ProcurementMethod(@JsonValue val value: String) {
    MV("open"),
    OT("open"),
    RT("selective"),
    SV("open"),
    DA("direct"),
    NP("selective"),
    FA("limited"),
    TEST_OT("open"),
    TEST_SV("open"),
    TEST_RT("selective"),
    TEST_MV("open");

    override fun toString(): String {
        return this.value
    }
}

enum class Scheme(@JsonValue val value: String) {
    CPV("CPV"),
    CPVS("CPVS"),
    GSIN("GSIN"),
    UNSPSC("UNSPSC"),
    CPC("CPC"),
    OKDP("OKDP"),
    OKPD("OKPD");

    override fun toString(): String {
        return this.value
    }
}

enum class SubmissionLanguage(@JsonValue val value: String) {
    BG("bg"),
    ES("es"),
    CS("cs"),
    DA("da"),
    DE("de"),
    ET("et"),
    EL("el"),
    EN("en"),
    FR("fr"),
    GA("ga"),
    HR("hr"),
    IT("it"),
    LV("lv"),
    LT("lt"),
    HU("hu"),
    MT("mt"),
    NL("nl"),
    PL("pl"),
    PT("pt"),
    RO("ro"),
    SK("sk"),
    SL("sl"),
    FI("fi"),
    SV("sv");

    override fun toString(): String {
        return this.value
    }
}

enum class SubmissionMethod(@JsonValue val value: String) {
    ELECTRONIC_SUBMISSION("electronicSubmission"),
    ELECTRONIC_AUCTION("electronicAuction"),
    WRITTEN("written"),
    IN_PERSON("inPerson");

    override fun toString(): String {
        return this.value
    }
}

enum class SubmissionMethodRationale(@JsonValue val value: String) {
    TOOLS_DEVICES_FILE_FORMATS_UNAVAILABLE("TOOLS_DEVICES_FILE_FORMATS_UNAVAILABLE"),
    IPR_ISSUES("IPR_ISSUES"),
    REQUIRES_SPECIALISED_EQUIPMENT("REQUIRES_SPECIALISED_EQUIPMENT"),
    PHYSICAL_MODEL("PHYSICAL_MODEL"),
    SENSITIVE_INFORMATION("SENSITIVE_INFORMATION");

    override fun toString(): String {
        return this.value
    }
}

enum class TenderStatus(@JsonValue val value: String) {
    PLANNING("planning"),
    PLANNED("planned"),
    ACTIVE("active"),
    CANCELLED("cancelled"),
    UNSUCCESSFUL("unsuccessful"),
    COMPLETE("complete");

    override fun toString(): String {
        return this.value
    }
}

enum class TenderStatusDetails(@JsonValue val value: String) {
    //    UNSUCCESSFUL("unsuccessful"),
//    AWARDED("awarded"),
    PLANNING("planning"),
    PLANNED("planned"),
    CLARIFICATION("clarification"),
    TENDERING("tendering"),
    CANCELLATION("cancellation"),
    SUSPENDED("suspended"),
    AWARDING("awarding"),
    AUCTION("auction"),
    AWARDED_STANDSTILL("awardedStandStill"),
    AWARDED_SUSPENDED("awardedSuspended"),
    AWARDED_CONTRACT_PREPARATION("awardedContractPreparation"),
    COMPLETE("complete"),
    EMPTY("empty");

    override fun toString(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, TenderStatusDetails>()

        init {
            for (c in TenderStatusDetails.values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): TenderStatusDetails {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }

}

enum class LotStatus(@JsonValue val value: String) {
    PLANNING("planning"),
    PLANNED("planned"),
    ACTIVE("active"),
    CANCELLED("cancelled"),
    UNSUCCESSFUL("unsuccessful"),
    COMPLETE("complete");

    override fun toString(): String {
        return this.value
    }
}

enum class LotStatusDetails(@JsonValue val value: String) {
    UNSUCCESSFUL("unsuccessful"),
    AWARDED("awarded"),
    CANCELLED("cancelled"),
    EMPTY("empty");

    override fun toString(): String {
        return this.value
    }
}

enum class DocumentType(@JsonValue val value: String) {

    EVALUATION_CRITERIA("evaluationCriteria"),
    ELIGIBILITY_CRITERIA("eligibilityCriteria"),
    BILL_OF_QUANTITY("billOfQuantity"),
    ILLUSTRATION("illustration"),
    MARKET_STUDIES("marketStudies"),
    TENDER_NOTICE("tenderNotice"),
    BIDDING_DOCUMENTS("biddingDocuments"),
    PROCUREMENT_PLAN("procurementPlan"),
    TECHNICAL_SPECIFICATIONS("technicalSpecifications"),
    CONTRACT_DRAFT("contractDraft"),
    HEARING_NOTICE("hearingNotice"),
    CLARIFICATIONS("clarifications"),
    ENVIRONMENTAL_IMPACT("environmentalImpact"),
    ASSET_AND_LIABILITY_ASSESSMENT("assetAndLiabilityAssessment"),
    RISK_PROVISIONS("riskProvisions"),
    COMPLAINTS("complaints"),
    NEEDS_ASSESSMENT("needsAssessment"),
    FEASIBILITY_STUDY("feasibilityStudy"),
    PROJECT_PLAN("projectPlan"),
    CONFLICT_OF_INTEREST("conflictOfInterest"),
    CANCELLATION_DETAILS("cancellationDetails"),
    SHORTLISTED_FIRMS("shortlistedFirms"),
    EVALUATION_REPORTS("evaluationReports"),
    CONTRACT_ARRANGEMENTS("contractArrangements"),
    CONTRACT_GUARANTEES("contractGuarantees");

    override fun toString(): String {
        return this.value
    }
}

enum class Operation(val value: String) {
    CREATE_CN("createCN"),
    CREATE_PN("createPN"),
    CREATE_PIN("createPIN"),
    UPDATE_CN("updateCN"),
    UPDATE_PN("updatePN"),
    CREATE_CN_ON_PN("createCNonPN"),
    CREATE_CN_ON_PIN("createCNonPIN"),
    CREATE_PIN_ON_PN("createPINonPN");

    companion object {
        private val CONSTANTS = HashMap<String, Operation>()

        init {
            for (c in Operation.values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): Operation {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}