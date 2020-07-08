package com.procurement.access.domain.model.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.access.domain.fail.error.DataErrors
import com.procurement.access.domain.util.Result
import com.procurement.access.domain.util.asFailure
import com.procurement.access.domain.util.asSuccess
import com.procurement.access.exception.EnumElementProviderException

enum class ProcurementMethod(@JsonValue val key: String) {
    MV("open"),
    OT("open"),
    RT("selective"),
    SV("open"),
    DA("limited"),
    NP("limited"),
    FA("limited"),
    OP("selective"),
    GPA("selective"),
    TEST_OT("open"),
    TEST_SV("open"),
    TEST_RT("selective"),
    TEST_MV("open"),
    TEST_DA("limited"),
    TEST_NP("limited"),
    TEST_FA("limited"),
    TEST_OP("selective"),
    TEST_GPA("selective");

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
