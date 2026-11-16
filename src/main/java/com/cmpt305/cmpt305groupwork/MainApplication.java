package com.cmpt305.cmpt305groupwork;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

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

        // -- Scroller & Property Cards -- //
        TilePane cardView = createNewCardView(properties);
        cardView.setHgap(20);
        cardView.setVgap(20);

        ScrollPane scroller = new ScrollPane();
        scroller.setContent(cardView);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); //removes horizontal scrolling
        scroller.setFitToWidth(true);

        // -- Labels -- //
        Label neighborhoodLabel = new Label("Filter by Neighborhood");
        Label propertyValueLabel = new Label("Property Value");

        // -- Separators -- //
        Separator sep0 = new Separator();
        Separator sep1 = new Separator();
        sep1.setPadding(new Insets(10));
        Separator sep2 = new Separator();
        sep2.setPadding(new Insets(10));

        // -- Slider -- //
        Slider propertyValueSlider = new Slider();

        // -- ComboBoxes -- //
        ComboBox<String> searchAddressBar = createSearchAddressBar(properties);
        ComboBox<String> searchNeighborhoodBar = createSearchNeighborhoodBar(properties);
        searchAddressBar.setPromptText("search for an address");

        // -- BorderPane Structure -- //
        //Top Section
        HBox topSection = new HBox();
        topSection.getChildren().addAll(searchAddressBar);
        topSection.setAlignment(Pos.TOP_CENTER);

        //Left Section
        VBox leftSection = new VBox();
        leftSection.getChildren().addAll(sep0,neighborhoodLabel,searchNeighborhoodBar,sep1,propertyValueLabel,propertyValueSlider,sep2);
        leftSection.setSpacing(10);

        //BorderPane
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(scroller);
        borderPane.setTop(topSection);
        borderPane.setLeft(leftSection);

        Scene scene = new Scene(borderPane, 1200, 600);

        // -- Window/Stage -- //
        stage.setResizable(true);
        stage.setMinHeight(600);
        stage.setMinWidth(1200);
        stage.setTitle("CMPT 305");
        stage.setScene(scene);
        stage.show();

    }

    private ComboBox<String> createSearchAddressBar(PropertyAssessments properties){
        ObservableList<String> addresses = FXCollections.observableList((new ArrayList<>(properties.findUniqueAddresses())));
        FilteredList<String> filteredAddresses = new FilteredList<>(addresses, s -> true);

        ComboBox<String> comboBox = new ComboBox<>(filteredAddresses);
        comboBox.setEditable(true);

        // Flag to avoid recursive updates
        final boolean[] ignore = {false};

        // ---- 1. Filter when the user types ----
        comboBox.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            if (ignore[0]) {
                return;
            }

            // Defer filtering until after JavaFX finishes updating caret/selection
            Platform.runLater(() -> {
                String filter = (newText == null) ? "" : newText.trim().toLowerCase();

                if (filter.isEmpty()) {
                    filteredAddresses.setPredicate(s -> true);
                } else {
                    filteredAddresses.setPredicate(address -> address.toLowerCase().contains(filter));
                }

                // Only reopen popup when user is typing text (not when selecting)
                if (!comboBox.isShowing() && !filter.isEmpty()) {
                    try {
                        comboBox.show();
                    } catch (Exception ignored) {
                        // Ignore internal selection exceptions safely
                    }
                }
            });
        });

        // ---- 2. When an address is selected, safely update the editor ----
        comboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                return;
            }

            ignore[0] = true;
            Platform.runLater(() -> {
                System.out.println(newVal);
                comboBox.getEditor().setText(newVal);
                ignore[0] = false;
            });
        });

        return comboBox;
    }

    private ComboBox<String> createSearchNeighborhoodBar(PropertyAssessments properties){
        ObservableList<String> neighborhoodNames = FXCollections.observableList((new ArrayList<>(properties.findUniqueNeighborhoods())));
        FilteredList<String> filteredNeighborhoods = new FilteredList<>(neighborhoodNames, s -> true);

        ComboBox<String> comboBox = new ComboBox<>(filteredNeighborhoods);
        comboBox.setEditable(true);

        // Flag to avoid recursive updates
        final boolean[] ignore = {false};

        // ---- 1. Filter when the user types ----
        comboBox.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            if (ignore[0]) {
                return;
            }

            // Defer filtering until after JavaFX finishes updating caret/selection
            Platform.runLater(() -> {
                String filter = (newText == null) ? "" : newText.trim().toLowerCase();

                if (filter.isEmpty()) {
                    filteredNeighborhoods.setPredicate(s -> true);
                } else {
                    filteredNeighborhoods.setPredicate(neighborhood -> neighborhood.toLowerCase().contains(filter));
                }

                // Only reopen popup when user is typing text (not when selecting)
                if (!comboBox.isShowing() && !filter.isEmpty()) {
                    try {
                        comboBox.show();
                    } catch (Exception ignored) {
                        // Ignore internal selection exceptions safely
                    }
                }
            });
        });

        // ---- 2. When a neighborhood is selected, safely update the editor ----
        comboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                return;
            }

            ignore[0] = true;
            Platform.runLater(() -> {
                System.out.println(newVal);
                comboBox.getEditor().setText(newVal);
                ignore[0] = false;
            });
        });

        return comboBox;
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
        vBox.setPadding(new Insets(10));
        viewPropertyButton.setStyle("-fx-background-color: black;-fx-text-fill: white");
        vBox.setStyle("-fx-background-color: white; -fx-background-radius: 15;");

        return vBox;
    }

    private TilePane createNewCardView(PropertyAssessments properties){
        return createPropertyCardsv2(createPropertyCardsList(properties));
    }



}
