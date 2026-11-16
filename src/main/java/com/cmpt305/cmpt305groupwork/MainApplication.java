package com.cmpt305.cmpt305groupwork;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

        Scene scene = new Scene(borderPane, 700, 700);
        ScrollPane scroller = new ScrollPane();
        VBox leftSection = new VBox();
        HBox topSection = new HBox();
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search Something");


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
        topSection.getChildren().addAll(searchBar);
        borderPane.setCenter(scroller);
        borderPane.setTop(topSection);
        topSection.setAlignment(Pos.TOP_CENTER);

        TilePane cardView = createNewCardView(properties);
        createPropertyCards(createPropertyCardsList(properties),cardView);
        scroller.setContent(cardView);
        cardView.setHgap(20);
        cardView.setVgap(20);

        //scene.getStylesheets().add("com/cmpt305/cmpt305groupwork/stylesheet.css");

        stage.setTitle("CMPT 305");
        stage.setScene(scene);
        stage.show();

        searchBar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(searchBar.getText());

            }
        });
    }

    private TilePane createPropertyCardsv2(List<VBox> propertyCards){
        TilePane tmpTilePane = new TilePane();
        for (VBox card : propertyCards){
            tmpTilePane.getChildren().add(card);
        }

        return tmpTilePane;
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

    private TilePane createNewCardView(PropertyAssessments properties){
        return createPropertyCardsv2(createPropertyCardsList(properties));
    }



}
