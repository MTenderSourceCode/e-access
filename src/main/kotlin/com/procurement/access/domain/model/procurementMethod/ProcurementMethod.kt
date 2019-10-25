package com.procurement.access.domain.model.procurementMethod

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.access.exception.EnumException

enum class ProcurementMethod(@JsonValue val value: String) {
    MV("open"),
    OT("open"),
    RT("selective"),
    SV("open"),
    DA("limited"),
    NP("limited"),
    FA("limited"),
    OP("selective"),
    TEST_OT("open"),
    TEST_SV("open"),
    TEST_RT("selective"),
    TEST_MV("open"),
    TEST_DA("limited"),
    TEST_NP("limited"),
    TEST_FA("limited"),
    TEST_OP("selective");

    override fun toString(): String {
        return this.value
    }

    companion object {
        private val elements: Map<String, ProcurementMethod> = values().associateBy { it.value.toUpperCase() }

        fun <T : Exception> valueOrException(name: String, block: (Exception) -> T): ProcurementMethod = try {
            valueOf(name)
        } catch (exception: Exception) {
            throw block(exception)
        }

        fun fromString(value: String): ProcurementMethod =
            elements[value.toUpperCase()] ?: throw EnumException(
                enumType = ProcurementMethod::class.java.canonicalName,
                value = value,
                values = values().joinToString { it.value })
    }
}
