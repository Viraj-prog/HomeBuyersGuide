package com.cmpt305.cmpt305groupwork;

import java.util.Arrays;
import java.util.Objects;

// Data class for Assessment Classes

public class Assessment {

    private String taxClass;
    private String[] assessmentClass = new String[3];
    private String[] percentAC = new String[3];

    // Takes an array input from Parsed property line, as well as the taxclass field.
    public Assessment(String[] assessmentInfo, String taxClass){
        this.taxClass = taxClass;
        this.assessmentClass = Arrays.copyOfRange(assessmentInfo, 0, 3);
        this.percentAC = Arrays.copyOfRange(assessmentInfo, 3, 6);
    }

    public String[] getACValues(){
        return assessmentClass;
    }
    public String[] getACPercents(){
        return percentAC;
    }
    public String getTaxClass() { return taxClass; }

    @Override
    public String toString(){
        // Output the two arrays, and tax class.
        return Arrays.toString(assessmentClass) + "\n" + Arrays.toString(percentAC) + "\n" + taxClass;
    }

    @Override
    public final boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(!(o instanceof Assessment otherAssessment)){
            return false;
        }

        // we now know that we're dealing with another AssessmentClass object, compare hard fields
        return (Arrays.equals(this.assessmentClass, otherAssessment.getACValues())) &&
                (Arrays.equals(this.percentAC, otherAssessment.getACPercents())) &&
                (this.taxClass.equals( otherAssessment.getTaxClass() ) );
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(assessmentClass), Arrays.hashCode(percentAC), taxClass);
    }
}
