package com.procurement.access.infrastructure.handler.v1.converter

import com.procurement.access.application.service.lot.SetLotsStatusUnsuccessfulData
import com.procurement.access.exception.ErrorException
import com.procurement.access.exception.ErrorType
import com.procurement.access.infrastructure.handler.v1.model.request.SetLotsStatusUnsuccessfulRequest
import com.procurement.access.lib.extension.mapIfNotEmpty
import com.procurement.access.lib.extension.orThrow

fun SetLotsStatusUnsuccessfulRequest.convert() = SetLotsStatusUnsuccessfulData(
    lots = this.lots
        .mapIfNotEmpty { lot ->
            SetLotsStatusUnsuccessfulData.Lot(
                id = lot.id
            )
        }
        .orThrow {
            ErrorException(
                error = ErrorType.IS_EMPTY,
                message = "The list of lots is empty."
            )
        }
)
