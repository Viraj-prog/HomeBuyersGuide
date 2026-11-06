package com.cmpt305.cmpt305groupwork;

import java.io.IOException;
import java.util.*;

/*
    The purpose of this class is to store a List of PropertyAssessment objects.
    It has two main fields, a List of PropertyAssessment objects called 'Properties'
    A field that houses the size of the PropertyAssessment list.

    By default, the List is SORTED based on each PropertyAssessment objects "assessedValue" field.
 */
public class PropertyAssessments {
    // This is the main field we will use for this class.
    List<PropertyAssessment> Properties = new ArrayList<>();

    // Base constructor, creates array list of ALL properties, from input String
    public PropertyAssessments(String csvFileName) throws IOException {
        String[][] data = CSVReader.readData(csvFileName);
        for (String[] entry : data){
            Properties.add(new PropertyAssessment(entry));
        }
        Collections.sort(Properties);
    }

    // Alternate constructor, creates a new PropertyAssessments object from a PropertyAssessment List
    public PropertyAssessments(List<PropertyAssessment> propertyList){
        Properties = propertyList;
    }

    // Default constructor, just creates an empty PropertyAssessment list (Used for hashSet initialization)
    public PropertyAssessments(){
        Properties = new ArrayList<>();
    }

    public int getSize(){
        return Properties.size();
    }

    // Returns a property object if target found, null otherwise.
    public PropertyAssessment searchProperty(int propertyNum){
        // returns FIRST instance of a match
        for(PropertyAssessment prop : Properties){
            if(prop.getAccountNumber() == propertyNum){
                return prop;
            }
        }
        // return null if nothing is found, flag for errors
        return null;
    }

    // Returns a List<PropertyAssessment> object based on a target filter
    public List<PropertyAssessment> filterByNeighborhood(String target){
        List<PropertyAssessment> propertyNeighborhood = new ArrayList<>();

        for (PropertyAssessment prop : this.Properties) {
            if (prop.getNeighborhood().equalsIgnoreCase(target)) {
                propertyNeighborhood.add(prop);
            }
        }
        // after filtering, sort by assessed value.
        Collections.sort(propertyNeighborhood);

        return propertyNeighborhood;
    }

    // Returns a List<PropertyAssessment> object based on assessment Class
    public List<PropertyAssessment> filterByAssessmentClass(String target){
        List<PropertyAssessment> propertyAC = new ArrayList<>();

        for(PropertyAssessment prop : this.Properties) {
            // make a set of the Assessment Class of this instance of prop
            Set<String> PropAC = new HashSet<>(List.of(prop.getACValues()));

            // check if the target assessment class exists in this prop's Property Class set.
            if(PropAC.contains(target.toUpperCase())){
                propertyAC.add(prop);
            }
        }
        // after constructing, sort by assessed value.
        Collections.sort(propertyAC);
        return propertyAC;
    }

    // The following methods assume that the PropertyAssessments List is SORTED
//    public int getMinimumSorted(){
//        return Properties.getFirst().getAssessedValue();
//    }
//    public int getMaximumSorted(){
//        return Properties.getLast().getAssessedValue();
//    }
//    public int getRangeSorted(){
//        int max = Properties.getLast().getAssessedValue();
//        int min = Properties.getFirst().getAssessedValue();
//        return max - min;
//    }

    public int getMean(){
        // get mean of properties (Round to nearest whole number)
        long mean = 0; // Overflow can occur when summing residential

        for(PropertyAssessment prop : Properties){
            mean += prop.getAssessedValue();
        }

        // type cast back to int
        long total = mean / (long)Properties.size();
        return (int)total;
    }
    public int getMedian(){
        // if odd, return middle. If even, return "middle two" average.
        int index = this.getSize();

        if(index % 2 == 0){ // even length list
            index = (index/2);
            int median1 = Properties.get(index).getAssessedValue();
            int median2 = Properties.get(index - 1).getAssessedValue();
            return (median1 + median2) / 2;

        }
        else{ // odd length list
            return Properties.get(index / 2).getAssessedValue();
        }
    }

    public PropertyAssessment getProperty(int index){
        return getProperties().get(index);
    }

    public List<PropertyAssessment> getProperties() {
        return Properties;
    }

    private void addProperty(PropertyAssessment prop){
        this.Properties.add(prop);
    }
    private void sortProperties(){
        Collections.sort(Properties);
    }
    private Map<String, PropertyAssessments> getAllNeighborhoodHomes(String houseOption){
        // method filters the list of PropertyAssessment object by neighborhood
        // only used within the class by "getBestValueNeighborhood" method
        Map<String, PropertyAssessments> neighborhoodMap = new HashMap<>();
        String houseType = houseOption.strip().toLowerCase();

        for(PropertyAssessment prop : this.Properties){
            if(prop.isHome() && prop.matchType(houseType)) { //ensure property is a home
                String key = prop.getNeighborhood();
                PropertyAssessments neighborhoodList = neighborhoodMap.get(key);

                if (neighborhoodList == null) {
                    // neighborhood is not tracked yet, create a new PropertyAssessments class with one entry:
                    PropertyAssessments newNeighborhoodList = new PropertyAssessments();
                    newNeighborhoodList.addProperty(prop);
                    neighborhoodMap.put(key, newNeighborhoodList);
                } else {
                    neighborhoodList.addProperty(prop);
                }
            }
        }

        return neighborhoodMap;
    }

    public List<NeighborhoodList> getBestValueNeighborhood(int target, String houseOption){
        List<NeighborhoodList> targetProperties = new ArrayList<>();

        // get the Hash map of all Neighborhood, PropertyAssessments
        Map<String, PropertyAssessments> propertyAssessmentsMap = this.getAllNeighborhoodHomes(houseOption);

        for(String neighborhood : propertyAssessmentsMap.keySet()){
            PropertyAssessments homes = propertyAssessmentsMap.get(neighborhood);
            homes.sortProperties();
            if(homes.getMedian() <= target && homes.getSize() >= 25) {
                targetProperties.add(new NeighborhoodList(neighborhood, homes.getMedian(), homes.getMean(), homes.getSize()));
            }
        }

        //sort low to high
        Collections.sort(targetProperties);

        return targetProperties;
    }

    // @ Override toString
    @Override
    public String toString(){
        return "PropertyAssessments Object.\n # of entries: " + Properties.size();
    }
}
