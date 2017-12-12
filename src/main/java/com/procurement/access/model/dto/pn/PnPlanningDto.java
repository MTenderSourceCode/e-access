package com.procurement.access.model.dto.pn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@JsonPropertyOrder("budget")
public class PnPlanningDto {

    @JsonProperty("budget")
    @JsonPropertyDescription("This section contain information about the budget line, and associated projects, " +
        "through which this contracting process is funded. It draws upon data model of the [Fiscal Data Package]" +
        "(http://fiscal.dataprotocols.org/), and should" +
        " be used to cross-reference to more detailed information held " +
        "using a CnBudgetDto Data Package, or, where no linked CnBudgetDto Data Package is available, to provide " +
        "enough " +
        "information to allow a user to manually or automatically cross-reference with another published source of " +
        "budget and project information.")
    @Valid
    @NotNull
    private final PnBudgetDto budget;

    @JsonCreator
    public PnPlanningDto(@JsonProperty("budget") final PnBudgetDto budget) {
        this.budget = budget;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(budget)
                                    .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PnPlanningDto)) {
            return false;
        }
        final PnPlanningDto rhs = (PnPlanningDto) other;
        return new EqualsBuilder().append(budget, rhs.budget)
                                  .isEquals();
    }
}