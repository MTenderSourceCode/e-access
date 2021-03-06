package com.procurement.access.domain.model.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.access.domain.fail.error.DataErrors
import com.procurement.access.exception.EnumElementProviderException
import com.procurement.access.lib.functional.Result
import com.procurement.access.lib.functional.asFailure
import com.procurement.access.lib.functional.asSuccess

enum class ProcurementMethod(@JsonValue val key: String) {
    CD("selective"),
    CF("selective"),
    DA("limited"),
    DC("selective"),
    DCO("selective"),
    FA("limited"),
    GPA("selective"),
    IP("selective"),
    MC("selective"),
    MV("open"),
    NP("limited"),
    OF("selective"),
    OP("selective"),
    OT("open"),
    RFQ("selective"),
    RT("selective"),
    SV("open"),
    TEST_CD("selective"),
    TEST_CF("selective"),
    TEST_DA("limited"),
    TEST_DC("selective"),
    TEST_DCO("selective"),
    TEST_FA("limited"),
    TEST_GPA("selective"),
    TEST_IP("selective"),
    TEST_MC("selective"),
    TEST_MV("open"),
    TEST_NP("limited"),
    TEST_OF("selective"),
    TEST_OP("selective"),
    TEST_OT("open"),
    TEST_RFQ("selective"),
    TEST_RT("selective"),
    TEST_SV("open");

    override fun toString(): String = key

    companion object {

        private val allowedValues = values()

        fun creator(name: String) = try {
            valueOf(name)
        } catch (ignored: Exception) {
            throw EnumElementProviderException(
                enumType = this::class.java.canonicalName,
                value = name,
                values = allowedValues.joinToString { it.name }
            )
        }

        fun tryCreate(name: String): Result<ProcurementMethod, DataErrors> = try {
            valueOf(name)
                .asSuccess()
        } catch (ignored: Exception) {
            DataErrors.Validation.UnknownValue(
                name = "pmd",
                expectedValues = allowedValues.map { it.name },
                actualValue = name
            )
                .asFailure()
        }
    }
}
