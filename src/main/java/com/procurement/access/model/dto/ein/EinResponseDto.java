package com.procurement.access.model.dto.ein;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.access.model.dto.databinding.LocalDateTimeDeserializer;
import com.procurement.access.model.dto.databinding.LocalDateTimeSerializer;
import com.procurement.access.model.dto.ocds.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({
        "token",
        "ocid",
        "planning",
        "tender",
        "parties",
        "buyer"
})
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class EinResponseDto {
    @JsonProperty("token")
    private final String token;
    @JsonProperty("ocid")
    private String ocId;
    @JsonProperty("planning")
    private final EinPlanningDto planning;
    @JsonProperty("tender")
    private final Tender tender;
    @JsonProperty("parties")
    private final List<Organization> parties;
    @JsonProperty("buyer")
    private final OrganizationReference buyer;

    @JsonCreator
    public EinResponseDto(@JsonProperty("token") final String token,
                          @JsonProperty("ocid") final String ocId,
                          @JsonProperty("planning") final EinPlanningDto planning,
                          @JsonProperty("tender") final Tender tender,
                          @JsonProperty("parties") final List<Organization> parties,
                          @JsonProperty("buyer") final OrganizationReference buyer) {
        this.token = token;
        this.ocId = ocId;
        this.planning = planning;
        this.tender = tender;
        this.parties = parties;
        this.buyer = buyer;
    }
}
