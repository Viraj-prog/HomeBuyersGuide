package com.cmpt305.cmpt305groupwork;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        BorderPane borderPane = new BorderPane();
        TilePane cardView = new TilePane();
        Scene scene = new Scene(borderPane, 700, 700);
        ScrollPane scroller = new ScrollPane();
        VBox vBox = new VBox();


        scroller.setContent(cardView);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); //removes horizontal scrolling
        scroller.setFitToWidth(true);
        // -- PropertyAssessments -- //
        PropertyAssessments properties;
        String csvFileName = "Property_Assessment_Data_2026_Small.csv";
        try{
            properties = new PropertyAssessments(csvFileName);
        }
        catch(IOException e){
            System.out.println("Error in opening " + csvFileName);
            return; // do not proceed with no file
        }



        //
        borderPane.setCenter(scroller);
        //cardView.getChildren().addAll(createPropertyCardv2(),createPropertyCardv2(),createPropertyCardv2());
        createPropertyCards(createPropertyCardsList(properties),cardView);

        cardView.setHgap(20);
        cardView.setVgap(20);

        //scene.getStylesheets().add("com/cmpt305/cmpt305groupwork/stylesheet.css");

        stage.setTitle("CMPT 305");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createPropertyCardv2(){
        VBox vBox = new VBox(3);
        HBox hBox0 = new HBox();

        Text assessedValueText = new Text("$4,000,000");
        Text neighborhoodText = new Text("Castle Downs");
        Button viewPropertyButton = new Button("View");
        Label address = new Label("123 Main Street");

        vBox.getChildren().addAll(address,neighborhoodText, hBox0);
        hBox0.getChildren().addAll(assessedValueText,viewPropertyButton);
        HBox.setMargin(assessedValueText,new Insets(5,15,0,0));
        VBox.setMargin(hBox0,new Insets(5,0,0,0));
        return vBox;
    }

    private void createPropertyCards(List<VBox> propertyCards, TilePane cardView){
        for (VBox card : propertyCards){
            cardView.getChildren().add(card);
        }
    }

    private List<VBox> createPropertyCardsList(PropertyAssessments properties){
        List<VBox> propertyCardsList = new ArrayList<>();
        for (int i=0;i<properties.getSize();i++){
            propertyCardsList.add(createIndividualPropertyCard(properties.getProperty(i)));
        }
        return propertyCardsList;
    }

    private VBox createIndividualPropertyCard(PropertyAssessment property){
        VBox vBox = new VBox(3);
        HBox hBox0 = new HBox();

        Text assessedValueText = new Text(String.valueOf(property.getAssessedValue()));
        Text neighborhoodText = new Text(property.getNeighborhood());
        Label address = new Label(property.getSuite()+" "+property.getHouseNumber()+" "+property.getStreetName());
        Button viewPropertyButton = new Button("View");
        vBox.getChildren().addAll(address,neighborhoodText, hBox0);
        hBox0.getChildren().addAll(assessedValueText,viewPropertyButton);
        HBox.setMargin(assessedValueText,new Insets(5,15,0,0));
        VBox.setMargin(hBox0,new Insets(5,0,0,0));
        return vBox;
    }

}
