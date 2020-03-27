package com.procurement.access.domain.fail.error

import com.procurement.access.application.service.Logger
import com.procurement.access.domain.fail.Fail

sealed class BadRequestErrors(
    numberError: String,
    override val description: String
) : Fail.Error("RQ-") {

    override val code: String = prefix + numberError

    class EntityNotFound(entityName: String, by: String) : BadRequestErrors(
        numberError = "01",
        description = "Entity '$entityName' not found $by"
    )

    class Parsing(message: String, val request: String, val exception: Exception? = null) : BadRequestErrors(
        numberError = "02",
        description = message
    ) {
        override fun logging(logger: Logger) {
            logger.error(message = "$message Invalid request body $request.", exception = exception)
        }
    }

    override fun logging(logger: Logger) {
        logger.error(message = message)
    }
}
