package com.cmpt305.cmpt305groupwork;// Data class for Address

import java.util.Objects;

public class Address {
    private String suite = ""; // Possible Null
    private int houseNumber = -1; // Possible Null
    private String streetName = ""; // Possible empty value
    private int neighborhoodID = -1; // Possible Null
    private final String neighborhood;
    private final String ward;

    // Takes an array input from a parsed property line.
    public Address(String[] addressInfo){

        this.suite = addressInfo[0];
        if(!addressInfo[1].isEmpty()){this.houseNumber = Integer.parseInt(addressInfo[1]);} // catch parse
        this.streetName = addressInfo[2];
        if(!addressInfo[3].isEmpty()){this.neighborhoodID = Integer.parseInt(addressInfo[3]);} // catch parse
        this.neighborhood = addressInfo[4];
        this.ward = addressInfo[5];
    }

    // getters
    public String getSuite(){ return suite; }
    public int getHouseNumber(){ return houseNumber; }
    public String getStreetName(){ return streetName; }
    public int getNeighborhoodID(){ return neighborhoodID; }
    public String getNeighborhood(){ return neighborhood;}
    public String getWard(){ return ward;}

    public String getAddress(){
        return houseNumber + " " + streetName;
    }

    @Override
    public String toString(){
        return "Suite: " + suite +
                "\nHouse Number: " + houseNumber +
                "\nStreet Name: " + streetName +
                "\nNeighborhoodID: " + neighborhoodID +
                "\nNeighborhood: " + neighborhood +
                "\nWard: " + ward + "\n";
    }

    // Override Equals
    // NOTE: There is a possibility that an address can be the same in the case where two properties
    // have no suite, house number, and street name, but have the same Neighborhood, Neighborhood Num, and ward.
    // This is something that I cannot fix without the introduction of the Account number in address. (which i don't want to refactor)
    @Override
    public final boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(!(o instanceof Address otherAddress)){
            return false;
        }

        // Primary key? Suite, houseNum, StreetName makes a unique address (in edmonton)
        return (this.suite.equals( otherAddress.getSuite() ) ) &&
                (this.streetName.equals( otherAddress.getStreetName() ) ) &&
                (this.houseNumber == otherAddress.getHouseNumber());
    }

    @Override
    public int hashCode() {
        // note, this assumes that accountNumber is UNIQUE. Fails if accountNumber is not.
        return Objects.hash(suite, streetName, houseNumber);
    }
}
