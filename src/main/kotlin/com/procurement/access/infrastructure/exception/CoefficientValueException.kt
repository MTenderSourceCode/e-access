package com.procurement.access.infrastructure.exception

class CoefficientValueException(coefficientValue: String, description: String = "") :
    RuntimeException("Incorrect value of the coefficient: '$coefficientValue'. $description")
