package com.procurement.access.infrastructure.handler.v1.converter

import com.procurement.access.application.service.ap.update.ApUpdateData
import com.procurement.access.domain.EnumElementProviderParser
import com.procurement.access.domain.model.enums.DocumentType
import com.procurement.access.exception.ErrorException
import com.procurement.access.exception.ErrorType
import com.procurement.access.infrastructure.handler.v1.model.request.ApUpdateRequest
import com.procurement.access.lib.extension.errorIfEmpty
import com.procurement.access.lib.takeIfNotEmpty

fun ApUpdateRequest.convert() = ApUpdateData(
    tender = this.tender
        .let { tender ->
            ApUpdateData.Tender(
                // VR.COM-1.26.8
                title = tender.title.takeIfNotEmpty {
                    ErrorException(
                        error = ErrorType.INCORRECT_VALUE_ATTRIBUTE,
                        message = "The attribute 'tender.title' is empty or blank."
                    )
                },
                // VR.COM-1.26.9
                description = tender.description
                    .takeIfNotEmpty {
                        throw ErrorException(
                            error = ErrorType.INCORRECT_VALUE_ATTRIBUTE,
                            message = "The attribute 'tender.description' is empty or blank."
                        )
                    },
                mainProcurementCategory = tender.mainProcurementCategory,
                procurementMethodRationale = tender.procurementMethodRationale
                    .takeIfNotEmpty {
                        ErrorException(
                            error = ErrorType.INCORRECT_VALUE_ATTRIBUTE,
                            message = "The attribute 'tender.procurementMethodRationale' is empty or blank."
                        )
                    },
                tenderPeriod = tender.tenderPeriod
                    .let { tenderPeriod ->
                        ApUpdateData.Tender.TenderPeriod(
                            startDate = tenderPeriod.startDate
                        )
                    },
                documents = tender.documents
                    .errorIfEmpty {
                        ErrorException(
                            error = ErrorType.IS_EMPTY,
                            message = "The tender contain empty list of the documents."
                        )
                    }
                    ?.map { document ->
                        ApUpdateData.Tender.Document(
                            documentType = parseTenderDocumentType(document.documentType),
                            id = document.id,
                            title = document.title
                                .takeIfNotEmpty {
                                    ErrorException(
                                        error = ErrorType.INCORRECT_VALUE_ATTRIBUTE,
                                        message = "The attribute 'document.title' is empty or blank."
                                    )
                                },
                            description = document.description
                                .takeIfNotEmpty {
                                    ErrorException(
                                        error = ErrorType.INCORRECT_VALUE_ATTRIBUTE,
                                        message = "The attribute 'document.description' is empty or blank."
                                    )
                                },
                            relatedLots = document.relatedLots
                                .errorIfEmpty {
                                    ErrorException(
                                        error = ErrorType.IS_EMPTY,
                                        message = "The tender contain empty list of the documents."
                                    )
                                }
                                .orEmpty()
                        )
                    }
                    .orEmpty(),
                classification = tender.classification
                    ?.let { classification ->
                        ApUpdateData.Tender.Classification(
                            scheme = classification.scheme,
                            id = classification.id,
                            description = classification.description
                                .takeIfNotEmpty {
                                    ErrorException(
                                        error = ErrorType.INCORRECT_VALUE_ATTRIBUTE,
                                        message = "The attribute 'tender.classification.description' is empty or blank."
                                    )
                                }
                        )
                    },
                value = tender.value?.let { value ->
                    ApUpdateData.Tender.Value(
                        tender.value.currency
                    )
                },
                lots = tender.lots
                    .errorIfEmpty {
                        ErrorException(
                            error = ErrorType.IS_EMPTY,
                            message = "The tender contain empty list of the lots."
                        )
                    }
                    ?.map { lot ->
                        ApUpdateData.Tender.Lot(
                            id = lot.id,
                            internalId = lot.internalId,
                            title = lot.title,
                            description = lot.description,
                            placeOfPerformance = lot.placeOfPerformance
                                ?.let { placeOfPerformance ->
                                    ApUpdateData.Tender.Lot.PlaceOfPerformance(
                                        address = placeOfPerformance.address
                                            .let { address ->
                                                ApUpdateData.Tender.Address(
                                                    streetAddress = address.streetAddress,
                                                    postalCode = address.postalCode,
                                                    addressDetails = address.addressDetails
                                                        .let { addressDetails ->
                                                            ApUpdateData.Tender.Address.AddressDetails(
                                                                country = addressDetails.country
                                                                    .let { country ->
                                                                        ApUpdateData.Tender.Address.AddressDetails.Country(
                                                                            scheme = country.scheme,
                                                                            id = country.id,
                                                                            description = country.description,
                                                                            uri = country.uri
                                                                        )
                                                                    },
                                                                region = addressDetails.region
                                                                    .let { region ->
                                                                        ApUpdateData.Tender.Address.AddressDetails.Region(
                                                                            scheme = region.scheme,
                                                                            id = region.id,
                                                                            description = region.description,
                                                                            uri = region.uri
                                                                        )
                                                                    },
                                                                locality = addressDetails.locality
                                                                    ?.let { locality ->
                                                                        ApUpdateData.Tender.Address.AddressDetails.Locality(
                                                                            scheme = locality.scheme,
                                                                            id = locality.id,
                                                                            description = locality.description,
                                                                            uri = locality.uri
                                                                        )
                                                                    }
                                                            )
                                                        }
                                                )
                                            }
                                    )
                                }
                        )
                    }
                    .orEmpty(),
                items = tender.items
                    .errorIfEmpty {
                        ErrorException(
                            error = ErrorType.IS_EMPTY,
                            message = "The tender contain empty list of the items."
                        )
                    }
                    ?.map { item ->
                        ApUpdateData.Tender.Item(
                            id = item.id,
                            internalId = item.internalId,
                            classification = item.classification
                                .let { classification ->
                                    ApUpdateData.Tender.Item.Classification(
                                        scheme = classification.scheme,
                                        id = classification.id,
                                        description = classification.description
                                    )
                                },
                            additionalClassifications = item.additionalClassifications
                                .errorIfEmpty {
                                    ErrorException(
                                        error = ErrorType.IS_EMPTY,
                                        message = "The tender contain empty list of the items.additionalClassification."
                                    )
                                }
                                ?.map { additionalClassification ->
                                    ApUpdateData.Tender.Item.AdditionalClassification(
                                        scheme = additionalClassification.scheme,
                                        id = additionalClassification.id,
                                        description = additionalClassification.description
                                    )
                                }
                                .orEmpty(),
                            quantity = item.quantity,
                            unit = item.unit
                                .let { unit ->
                                    ApUpdateData.Tender.Item.Unit(
                                        id = unit.id,
                                        name = unit.name
                                    )
                                },
                            description = item.description,
                            relatedLot = item.relatedLot,
                            deliveryAddress = item.deliveryAddress
                                ?.let { address ->
                                    ApUpdateData.Tender.Address(
                                        streetAddress = address.streetAddress,
                                        postalCode = address.postalCode,
                                        addressDetails = address.addressDetails
                                            .let { addressDetails ->
                                                ApUpdateData.Tender.Address.AddressDetails(
                                                    country = addressDetails.country
                                                        .let { country ->
                                                            ApUpdateData.Tender.Address.AddressDetails.Country(
                                                                scheme = country.scheme,
                                                                id = country.id,
                                                                description = country.description,
                                                                uri = country.uri
                                                            )
                                                        },
                                                    region = addressDetails.region
                                                        .let { region ->
                                                            ApUpdateData.Tender.Address.AddressDetails.Region(
                                                                scheme = region.scheme,
                                                                id = region.id,
                                                                description = region.description,
                                                                uri = region.uri
                                                            )
                                                        },
                                                    locality = addressDetails.locality
                                                        ?.let { locality ->
                                                            ApUpdateData.Tender.Address.AddressDetails.Locality(
                                                                scheme = locality.scheme,
                                                                id = locality.id,
                                                                description = locality.description,
                                                                uri = locality.uri
                                                            )
                                                        }
                                                )
                                            }
                                    )
                                }
                        )
                    }
                    .orEmpty()
            )
        }
)

private val allowedTenderDocumentTypes = DocumentType.allowedElements
    .filter {
        when (it) {
            DocumentType.TENDER_NOTICE,
            DocumentType.BIDDING_DOCUMENTS,
            DocumentType.TECHNICAL_SPECIFICATIONS,
            DocumentType.EVALUATION_CRITERIA,
            DocumentType.CLARIFICATIONS,
            DocumentType.ELIGIBILITY_CRITERIA,
            DocumentType.RISK_PROVISIONS,
            DocumentType.BILL_OF_QUANTITY,
            DocumentType.CONFLICT_OF_INTEREST,
            DocumentType.PROCUREMENT_PLAN,
            DocumentType.CONTRACT_DRAFT,
            DocumentType.COMPLAINTS,
            DocumentType.ILLUSTRATION,
            DocumentType.CANCELLATION_DETAILS,
            DocumentType.EVALUATION_REPORTS,
            DocumentType.SHORTLISTED_FIRMS,
            DocumentType.CONTRACT_ARRANGEMENTS,
            DocumentType.CONTRACT_GUARANTEES -> true

            DocumentType.ASSET_AND_LIABILITY_ASSESSMENT,
            DocumentType.ENVIRONMENTAL_IMPACT,
            DocumentType.FEASIBILITY_STUDY,
            DocumentType.HEARING_NOTICE,
            DocumentType.MARKET_STUDIES,
            DocumentType.NEEDS_ASSESSMENT,
            DocumentType.PROJECT_PLAN -> false
        }
    }.toSet()

private fun parseTenderDocumentType(documentType: String) =
    EnumElementProviderParser.checkAndParseEnum(documentType, allowedTenderDocumentTypes, DocumentType)