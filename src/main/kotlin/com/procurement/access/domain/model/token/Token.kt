package com.procurement.access.domain.model.token

import com.procurement.access.lib.functional.Result
import java.util.*

typealias Token = UUID

fun String.tryCreateToken(): Result<Token, String> = try {
    Result.success(Token.fromString(this))
} catch (ignored: Exception) {
    Result.failure("uuid")
}
