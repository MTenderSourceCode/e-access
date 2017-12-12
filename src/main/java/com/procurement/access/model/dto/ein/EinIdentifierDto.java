package com.procurement.access.model.dto.ein;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "scheme",
    "legalName",
    "uri"
})
public class EinIdentifierDto {
    @JsonProperty("id")
    @JsonPropertyDescription("The identifier of the organization in the selected scheme.")
    private final String id;

    @JsonProperty("scheme")
    @JsonPropertyDescription("Organization identifiers should be drawn from an existing organization identifier list." +
        " The scheme field is used to indicate the list or register from which the identifier is drawn. This value " +
        "should be drawn from the [Organization FsIdentifierDto Scheme](http://standard.open-contracting" +
        ".org/latest/en/schema/codelists/#organization-identifier-scheme) codelist.")
    private final String scheme;

    @JsonProperty("legalName")
    @JsonPropertyDescription("The legally registered name of the organization.")
//    @Pattern(regexp = "^(legalName_(((([A-Za-z]{2,3}(-([A-Za-z]{3}(-[A-Za-z]{3}){0,2}))?)|[A-Za-z]{4}|[A-Za-z]{5,
// 8})" +
//        "(-([A-Za-z]{4}))?(-([A-Za-z]{2}|[0-9]{3}))?(-([A-Za-z0-9]{5,8}|[0-9][A-Za-z0-9]{3}))*(-([0-9A-WY-Za-wy-z]" +
//        "(-[A-Za-z0-9]{2,8})+))*(-(x(-[A-Za-z0-9]{1,8})+))?)|(x(-[A-Za-z0-9]{1,8})+)))$")
    private final String legalName;

    @JsonProperty("uri")
    @JsonPropertyDescription("A URI to identify the organization, such as those provided by [Open Corporates]" +
        "(http://www.opencorporates.com) or some other relevant URI provider. This is not for listing the website of " +
        "the organization: that can be done through the URL field of the Organization contact point.")
    private final String uri;

    @JsonCreator
    public EinIdentifierDto(@JsonProperty("id") final String id,
                            @JsonProperty("scheme") final String scheme,
                            @JsonProperty("legalName") final String legalName,
                            @JsonProperty("uri") final String uri) {
        this.id = id;
        this.scheme = scheme;
        this.legalName = legalName;
        this.uri = uri;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id)
                                    .append(scheme)
                                    .append(legalName)
                                    .append(uri)
                                    .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof EinIdentifierDto)) {
            return false;
        }
        final EinIdentifierDto rhs = (EinIdentifierDto) other;
        return new EqualsBuilder().append(id, rhs.id)
                                  .append(scheme, rhs.scheme)
                                  .append(legalName, rhs.legalName)
                                  .append(uri, rhs.uri)
                                  .isEquals();
    }
}