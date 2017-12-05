
package com.ocds.access.model.dto.cn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ocds.access.model.dto.json.LocalDateTimeDeserializer;
import com.ocds.access.model.dto.json.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@JsonPropertyOrder({
    "startDate",
    "endDate"
})
public class BudgetBreakdownPeriodDto {
    @JsonProperty("startDate")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonPropertyDescription("The start date for the period. When known, a precise start date must always be provided.")
    @NotNull
    private final LocalDateTime startDate;

    @JsonProperty("endDate")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonPropertyDescription("The end date for the period. When known, a precise end date must always be provided.")
    @NotNull
    private final LocalDateTime endDate;


    @JsonCreator
    public BudgetBreakdownPeriodDto(@JsonProperty("startDate") @JsonDeserialize(using = LocalDateTimeDeserializer.class) final LocalDateTime startDate,
                                    @JsonProperty("endDate") @JsonDeserialize(using = LocalDateTimeDeserializer.class) final LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(startDate)
                                    .append(endDate)
                                    .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof BudgetBreakdownPeriodDto)) {
            return false;
        }
        final BudgetBreakdownPeriodDto rhs = (BudgetBreakdownPeriodDto) other;
        return new EqualsBuilder().append(startDate, rhs.startDate)
                                  .append(endDate, rhs.endDate)
                                  .isEquals();
    }
}