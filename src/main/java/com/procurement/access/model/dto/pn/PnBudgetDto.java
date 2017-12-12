package com.procurement.access.model.dto.pn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@JsonPropertyOrder({
    "description",
    "amount",
    "isEuropeanUnionFunded",
    "budgetBreakdown"
})
public class PnBudgetDto {

    @JsonProperty("description")
    @JsonPropertyDescription("A short free text description of the budget source. May be used to provide the title of" +
        " the budget line, or the programme used to fund this project.")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private final String description;

    @JsonProperty("amount")
    @Valid
    @NotNull
    private final PnValueDto amount;

    @JsonProperty("isEuropeanUnionFunded")
    @JsonPropertyDescription("A True or False field to indicate whether this procurement is related to a project " +
        "and/or programme financed by European Union funds.")
    @NotNull
    private final Boolean isEuropeanUnionFunded;

    @JsonProperty("budgetBreakdown")
    @JsonPropertyDescription("A detailed breakdown of the budget by period and/or participating funders.")
    @Valid
    @NotNull
    private final List<PnBudgetBreakdownDto> budgetBreakdown;

    @JsonCreator
    public PnBudgetDto(@JsonProperty("description") final String description,
                       @JsonProperty("amount") final PnValueDto amount,
                       @JsonProperty("isEuropeanUnionFunded") final Boolean isEuropeanUnionFunded,
                       @JsonProperty("budgetBreakdown") final List<PnBudgetBreakdownDto> budgetBreakdown) {
        this.description = description;
        this.amount = amount;
        this.isEuropeanUnionFunded = isEuropeanUnionFunded;
        this.budgetBreakdown = budgetBreakdown;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(description)
                                    .append(amount)
                                    .append(isEuropeanUnionFunded)
                                    .append(budgetBreakdown)
                                    .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PnBudgetDto)) {
            return false;
        }
        final PnBudgetDto rhs = (PnBudgetDto) other;
        return new EqualsBuilder().append(description, rhs.description)
                                  .append(amount, rhs.amount)
                                  .append(isEuropeanUnionFunded, rhs.isEuropeanUnionFunded)
                                  .append(budgetBreakdown, rhs.budgetBreakdown)
                                  .isEquals();
    }
}