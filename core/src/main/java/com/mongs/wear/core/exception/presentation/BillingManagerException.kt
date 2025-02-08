package com.mongs.wear.core.exception.presentation

import com.mongs.wear.core.errors.PresentationErrorCode
import com.mongs.wear.core.exception.global.PresentationException

class BillingConnectException : PresentationException(
    code = PresentationErrorCode.PRESENTATION_USER_BILLING_CONNECT,
)

class BillingNotSupportException : PresentationException(
    code = PresentationErrorCode.PRESENTATION_USER_BILLING_NOT_SUPPORT,
)

class GetProductException : PresentationException(
    code = PresentationErrorCode.PRESENTATION_USER_GET_PRODUCTS,
)