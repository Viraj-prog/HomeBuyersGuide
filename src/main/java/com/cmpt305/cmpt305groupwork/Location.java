package com.cmpt305.cmpt305groupwork;// Data class for Location

import java.util.Objects;

public class Location {

    private final double Latitude;
    private final double Longitude;

    // Takes an array input of Latitude Longitude. Parse to double
    public Location(String[] entry){
        this.Latitude = Double.parseDouble(entry[0]);
        this.Longitude = Double.parseDouble(entry[1]);
    }

    // getters
    public Double getLat(){
        return Latitude;
    }
    public Double getLong(){
        return Longitude;
    }

    @Override
    public String toString(){
        // Returns lat & long in the same format as the dataset.
        return "POINT ("+ getLat() + " " + getLong() + ")";
    }

    @Override
    public final boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(!(o instanceof Location otherLocation)){
            return false;
        }
        return (this.Latitude == otherLocation.getLat()) && (this.Longitude == otherLocation.getLong());
    }

    @Override //Hash?
    public int hashCode() {
        return Objects.hash(Latitude, Longitude);
    }

}
