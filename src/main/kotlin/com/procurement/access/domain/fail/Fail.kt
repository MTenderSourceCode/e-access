package com.procurement.access.domain.fail

import com.procurement.access.domain.EnumElementProvider
import com.procurement.access.domain.util.Result
import com.procurement.access.domain.util.ValidationResult

sealed class Fail {

    abstract class Error(val prefix: String) : Fail() {
        abstract val code: String
        abstract val description: String
        val message: String
            get() = "ERROR CODE: '$code', DESCRIPTION: '$description'."

        companion object {
            fun <T, E : Error> E.toResult(): Result<T, E> = Result.failure(this)

            fun <E : Error> E.toValidationResult(): ValidationResult<E> = ValidationResult.error(this)
        }
    }

    sealed class Incident(val level: Level, number: String, val description: String) : Fail() {
        val code: String = "INC-$number"

        class Database(val exception: Exception) : Incident(
            level = Level.ERROR,
            number = "01",
            description = "Database incident."
        )

        class Parsing(data: String) : Incident(
            level = Level.ERROR,
            number = "02",
            description = "Entity has invalid data = $data."
        )

        class DatabaseIncident : Incident(
            level = Level.ERROR,
            number = "03",
            description = "Could not process data from database."
        )

        enum class Level(override val key: String) : EnumElementProvider.Key {
            ERROR("error"),
            WARNING("warning"),
            INFO("info");

            companion object : EnumElementProvider<Level>(info = info())
        }
    }
}
