package com.cmpt305.cmpt305groupwork;

import java.util.Objects;

public class NeighborhoodList implements Comparable<NeighborhoodList>{
    // data class to help keep track / sort neighborhoods by median (Assessment) values.

    private final String neighborhood;
    private final int median;
    private final int mean;
    private final int neighborhoodSize;

    public NeighborhoodList(String neighborhood, int Median, int Mean, int size){
        this.neighborhood = neighborhood;
        this.median = Median;
        this.mean = Mean;
        this.neighborhoodSize = size;
    }

    public String getNeighborhood(){
        return this.neighborhood;
    }

    public int getMedian(){
        return this.median;
    }
    public int getMean() { return this.mean; }
    public int getNeighborhoodSize() {return this.neighborhoodSize; }


    @Override
    public String toString(){
        return "Neighborhood: " + this.neighborhood + "\tMedian Value: " + this.median + "\tMean Value: " + this.mean + "\n";
    }

    @Override // Compare using assessmentValue
    public int compareTo(NeighborhoodList other) {
        if (other == null) {
            throw new NullPointerException();
        }
        return Integer.compare(this.getMedian(), other.getMedian());
    }

    @Override
    public final boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(!(o instanceof NeighborhoodList otherNeighborhoodMedian)){
            return false;
        }
        return this.median == otherNeighborhoodMedian.getMedian() &&
                this.neighborhood.equals(otherNeighborhoodMedian.getNeighborhood());
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.neighborhood, this.median, this.mean);
    }

}
