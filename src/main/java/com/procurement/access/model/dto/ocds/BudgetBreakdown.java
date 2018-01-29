package com.procurement.access.model.dto.ocds;

import com.fasterxml.jackson.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "description",
        "amount",
        "period",
        "sourceParty"
})
public class BudgetBreakdown {
    @JsonProperty("id")
    @NotNull
    private final String id;

    @JsonProperty("description")
    private final String description;

    @JsonProperty("amount")
    @Valid
    @NotNull
    private final Value amount;

    @JsonProperty("period")
    @Valid
    @NotNull
    private final Period period;

    @JsonProperty("sourceParty")
    @JsonPropertyDescription("The id and name of the party being referenced. Used to cross-reference to the parties " +
            "section")
    @Valid
    @NotNull
    private final OrganizationReference sourceParty;

    @JsonCreator
    public BudgetBreakdown(@JsonProperty("id") final String id,
                           @JsonProperty("description") final String description,
                           @JsonProperty("amount") final Value amount,
                           @JsonProperty("period") final Period period,
                           @JsonProperty("sourceParty") final OrganizationReference sourceParty) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.period = period;
        this.sourceParty = sourceParty;
    }
}