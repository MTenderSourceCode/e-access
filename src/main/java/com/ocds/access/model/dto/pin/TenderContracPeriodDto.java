
package com.ocds.access.model.dto.pin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ocds.access.model.dto.json.LocalDateTimeDeserializer;
import com.ocds.access.model.dto.json.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import lombok.Getter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@JsonPropertyOrder({
    "startDate",
    "endDate",
    "durationInDays"
})
public class TenderContracPeriodDto {
    @JsonProperty("startDate")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonPropertyDescription("The start date for the period. When known, a precise start date must always be provided.")
    private final LocalDateTime startDate;

    @JsonProperty("endDate")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonPropertyDescription("The end date for the period. When known, a precise end date must always be provided.")
    private final LocalDateTime endDate;


    @JsonProperty("durationInDays")
    @JsonPropertyDescription("The maximum duration of this period in days. A user interface may wish to collect or " +
        "display this data in months or years as appropriate, but should convert it into days when completing this " +
        "field. This field can be used when exact dates are not known.  Where a startDate and endDate are given, this" +
        " field is optional, and should reflect the difference between those two days. Where a startDate and " +
        "maxExtentDate are given, this field is optional, and should reflect the difference between startDate and " +
        "maxExtentDate.")
    private final Integer durationInDays;

    @JsonCreator
    public TenderContracPeriodDto(@JsonProperty("startDate") @JsonDeserialize(using = LocalDateTimeDeserializer.class) final LocalDateTime startDate,
                                  @JsonProperty("endDate") @JsonDeserialize(using = LocalDateTimeDeserializer.class) final LocalDateTime endDate,
                                  @JsonProperty("durationInDays") final Integer durationInDays) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.durationInDays = durationInDays;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(startDate)
                                    .append(endDate)
                                    .append(durationInDays)
                                    .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PeriodDto)) {
            return false;
        }
        final TenderContracPeriodDto rhs = (TenderContracPeriodDto) other;
        return new EqualsBuilder().append(startDate, rhs.startDate)
                                  .append(endDate, rhs.endDate)
                                  .append(durationInDays, rhs.durationInDays)
                                  .isEquals();
    }
}