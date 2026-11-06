package com.cmpt305.cmpt305groupwork;

import java.util.Arrays;
import java.util.Objects;

/*
    This class houses information about individual properties from a .csv file.
    It has 6 fields:
        3 of which are primitive, containing either commonly used fields, or something that uniquely identifies
        a specific property.
        The other 3 are data classes, which contain closely related fields, such as address information,
        location information, or AssessmentClass data.

    By default, this class is sorted by the 'assessedValue' field, because more often that not we care about
    the monetary value of a property to evaluate it's total worth.
 */
public class PropertyAssessment implements Comparable<PropertyAssessment>{
    // final fields cannot be modified after assignment. ( may need changing later )
    private final int accountNumber;
    private final int assessedValue;
    private boolean garage = false;

    // following fields hold similar, grouped info about the property (final?)
    private final Location coordinates;
    private final Address address;
    private final Assessment assessmentClass;

    // call constructor for the data, input will be the csv line
    public PropertyAssessment(String[] entry){
        this.accountNumber = Integer.parseInt(entry[0]);
        this.assessedValue = Integer.parseInt(entry[7]);
        if(entry[9].equals("Y")){
            this.garage = true;
        }

        this.address = new Address(Arrays.copyOfRange(entry, 1, 7));
        this.assessmentClass = new Assessment(Arrays.copyOfRange(entry, 10, 16), entry[8]);
        this.coordinates = new Location(Arrays.copyOfRange(entry, 16, 18));
    }

    // getters ( Class fields )
    public int getAccountNumber(){
        return accountNumber;
    }
    public int getAssessedValue(){
        return assessedValue;
    }
    public boolean hasGarage(){
        return garage;
    }

    // getters ( Address Class )
    public String getNeighborhood() { return address.getNeighborhood(); }
    public String getWard() { return address.getWard(); }
    public int getHouseNumber() { return address.getHouseNumber(); }
    public String getStreetName() { return address.getStreetName(); }
    public String getSuite() { return address.getSuite(); }
    public int getNeighborhoodNum() { return address.getNeighborhoodID(); }

    // getters ( Assessment Class )
    public String[] getACValues() { return assessmentClass.getACValues();}
    public String[] getACPercents() { return assessmentClass.getACPercents();}

    // getters ( Location )
    public Double getLatitude() { return coordinates.getLat(); }
    public Double getLongitude() { return coordinates.getLong(); }

    // TODO: MAKE TEST CASES FOR FOLLOWING METHODS:
    // Used for property searching, meant to filter out actual residential properties.
    public boolean isHome(){
        return assessmentClass.getTaxClass().equals("Residential")
                && address.getHouseNumber() != -1
                && !Objects.equals(address.getStreetName(), "")
                && assessedValue >= 50000;
    }
    public boolean isCondo(){
        return !address.getSuite().isEmpty();
    }
    public boolean matchType(String type){
        if(type.equalsIgnoreCase("Condo")){ return this.isCondo(); }
        if(type.equalsIgnoreCase("House")){ return !this.isCondo(); }
        return false;
    }

    @Override
    public String toString(){
        return "Property Number " + accountNumber +
                "\nAccount Value " + assessedValue;
    }

    @Override // Compare using assessmentValue
    public int compareTo(PropertyAssessment other) {
        if (other == null) {
            throw new NullPointerException();
        }
        return Integer.compare(this.getAssessedValue(), other.getAssessedValue());
        // this.age - other.age normally also returns the correct result, but the compare method avoids overflows.
    }

    @Override // Properties are equal if they have the same Account number
    public final boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(!(o instanceof PropertyAssessment otherProperty)){
            return false;
        }
        return this.accountNumber == otherProperty.getAccountNumber();
    }

    @Override // Because accountNumber is unique, a hashed accountNumber should be unique
    public int hashCode() {
        // note, this assumes that accountNumber is UNIQUE. Fails if accountNumber is not.
        return Objects.hash(accountNumber);
    }
}


