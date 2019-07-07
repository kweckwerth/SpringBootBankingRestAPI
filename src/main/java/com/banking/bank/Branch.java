package com.banking.bank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Branch
{
    private String branchName;
    private float latitude;
    private float longitude;
    private String streetAddress;
    private String city;
    private String countrySubDivision;
    private String country;
    private String postCode;

    public Branch() {
    }

    /*
    for junit testing
     */
    public Branch(String branchName, String streetAddress, String city, String countrySubDivision, String country, String postCode, float latitude, float longitude) {
        this.branchName = branchName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.countrySubDivision = countrySubDivision;
        this.country = country;
        this.postCode = postCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }


}
