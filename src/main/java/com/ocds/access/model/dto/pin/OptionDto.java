
package com.ocds.access.model.dto.pin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@JsonPropertyOrder({
    "hasOptions"
})
public class OptionDto {
    @JsonProperty("hasOptions")
    @JsonPropertyDescription("A True/False field to indicate if lot options will be accepted. Required by the EU")
    @NotNull
    private final Boolean hasOptions;



    @JsonCreator
    public OptionDto(@JsonProperty("hasOptions") final Boolean hasOptions) {
        this.hasOptions = hasOptions;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(hasOptions)
                                    .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof OptionDto)) {
            return false;
        }
        final OptionDto rhs = (OptionDto) other;
        return new EqualsBuilder().append(hasOptions, rhs.hasOptions)
                                  .isEquals();
    }
}