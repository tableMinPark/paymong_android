package com.mongs.wear.core.exception.global

import com.mongs.wear.core.errors.ErrorCode
import java.util.Collections

open class PresentationException(
    open val code: ErrorCode,
    open val result: Map<String, Any> = Collections.emptyMap(),
    override val message: String = code.getMessage(),
) : RuntimeException(message)