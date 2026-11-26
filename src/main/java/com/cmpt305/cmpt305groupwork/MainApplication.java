package com.cmpt305.cmpt305groupwork;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javafx.scene.Cursor.DEFAULT;
import static javafx.scene.Cursor.TEXT;

public class MainApplication extends Application {
    private TilePane cardView;
    private PropertyAssessments properties;
    private PropertyAssessments filteredProperties;
    private ScrollPane scroller;

    // filtered states
    private String neighborhoodFilter = null;
    private String addressFilter = null;
    private int budgetMinFilter = -1;
    private int budgetMaxFilter = 2000000000;

    // used for slider
    private int minValue;
    private int maxValue;

    @Override
    public void start(Stage stage) throws IOException {

        // -- PropertyAssessments -- //
        if (!loadProperties()){
            return;
        }

        // -- Scroller & Property Cards -- //
        cardView = createNewCardView(filteredProperties);
        scroller = new ScrollPane();
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

        // -- Min / Max Budget Boxes -- //
        VBox budgetFilterBox = createBudgetFilterBox(filteredProperties);

        // -- ComboBoxes -- //
        ComboBox<String> searchAddressBar = createSearchAddressBar(filteredProperties);
        ComboBox<String> searchNeighborhoodBar = createSearchNeighborhoodBar(filteredProperties);
        searchAddressBar.setPromptText("search for an address");

        // -- BorderPane Structure -- //
        //Top Section
        HBox topSection = new HBox();
        topSection.getChildren().addAll(searchAddressBar);
        topSection.setAlignment(Pos.TOP_CENTER);

        //Left Section
        VBox leftSection = new VBox();
        leftSection.getChildren().addAll(sep0,neighborhoodLabel,searchNeighborhoodBar,
                sep1,propertyValueLabel,budgetFilterBox,
                sep2);
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

    //From: Philip Mees -- Determines actions for filtering by searched Addresses
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
            if (newVal == null || newVal.isEmpty()) {
                addressFilter = null;
                return;
            }

            ignore[0] = true;
            Platform.runLater(() -> {
                System.out.println(newVal + "in cSABar");
                comboBox.getEditor().setText(newVal);
                addressFilter = newVal;
                filterProperties(properties);
                ignore[0] = false;
            });
        });

        return comboBox;
    }

    //From: Philip Mees -- Determines actions for filtering by searched Neighborhoods
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
            if (newVal == null || newVal.isEmpty()) {
                neighborhoodFilter = null;
                return;
            }

            ignore[0] = true;
            Platform.runLater(() -> {
                System.out.println("Updated value " + newVal);
                neighborhoodFilter = newVal;
                filterProperties(properties);
                ignore[0] = false;
            });
        });

        return comboBox;
    }

    // Budget Boxes
    private VBox createBudgetFilterBox(PropertyAssessments properties){
        TextField minBudgetField = createNumberField("Min Budget: ");
        TextField maxBudgetField = createNumberField("Max Budget: ");

        Button applyButton = new Button ("Apply");

        // Show message if user enters something invalid
        Label budgetMessage = new Label();
        budgetMessage.setTextFill(Color.RED);

        // Event Handling (parse text in boxes)
        EventHandler<ActionEvent> applyBudget = event -> {
            int min = budgetMinFilter;
            int max = budgetMaxFilter;

            if(!minBudgetField.getText().isBlank()){
                min = Integer.parseInt(minBudgetField.getText().trim());
            }
            if(!maxBudgetField.getText().isBlank()){
                max = Integer.parseInt(maxBudgetField.getText().trim());
            }

            // Catch an invalid price range
            if (min >= 0 && max >= 0 && min > max){
                budgetMessage.setText("Min cannot be more than Max");
                return;
            }

            budgetMessage.setText("");
            budgetMinFilter = min;
            budgetMaxFilter = max;
            System.out.println("Budget Range: " + min + " - " + max);
            filterProperties(properties);
        };

        applyButton.setOnAction(applyBudget);
        minBudgetField.setOnAction(applyBudget);
        maxBudgetField.setOnAction(applyBudget);

        HBox buttons = new HBox(5, applyButton);
        return new VBox(5, new Label("Minimum"), minBudgetField, new Label("Maximum"), maxBudgetField, buttons, budgetMessage);
    }

    // Make the textfield digits only
    private TextField createNumberField(String text){
        TextField field = new TextField();
        field.setPromptText(text);
        field.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().matches("\\d{0,9}") ? change : null));
        return field;
    }

    // Not sure what does yet
    private TilePane createPropertyCardsv2(List<VBox> propertyCards){
        TilePane tmpTilePane = new TilePane();
        for (VBox card : propertyCards){
            tmpTilePane.getChildren().add(card);
        }
        return tmpTilePane;
    }

    // Not sure what does yet
    private void createPropertyCards(List<VBox> propertyCards, TilePane cardView){
        for (VBox card : propertyCards){
            cardView.getChildren().add(card);
        }
    }

    // Creates the list of propertyCards objects. Each card corresponds to a property.
    private List<VBox> createPropertyCardsList(PropertyAssessments properties){
        List<VBox> propertyCardsList = new ArrayList<>();
        String idName = "";
        for (int i=0;i<properties.getSize();i++){
            PropertyAssessment prop = properties.getProperty(i);
            if (!prop.getAddress().strip().equalsIgnoreCase("-1")) {
                idName = "propertyCard" + i;
                propertyCardsList.add(createIndividualPropertyCard(prop, idName));
            }
        }
        return propertyCardsList;
    }

    // Creates a propertyCard that goes inside the list, holds information based on the property given.
    private VBox createIndividualPropertyCard(PropertyAssessment property, String idName){
        VBox card = new VBox(3);
        HBox hBox0 = new HBox();

        String addressStr = property.getHouseNumber()+" "+property.getStreetName();
        Text assessedValueText = new Text(String.valueOf(property.getAssessedValue()));
        Text neighborhoodText = new Text(property.getNeighborhood());
        Label addressLabel = new Label(addressStr);
        Button viewPropertyButton = new Button("View");

        card.getChildren().addAll(addressLabel,neighborhoodText, hBox0);
        hBox0.getChildren().addAll(assessedValueText,viewPropertyButton);
        HBox.setMargin(assessedValueText,new Insets(5,15,0,0));
        VBox.setMargin(hBox0,new Insets(5,0,0,0));
        card.setPadding(new Insets(10));

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(1.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0, 0, 0,0.05));

        viewPropertyButton.setStyle("-fx-background-color: black;-fx-text-fill: white");
        viewPropertyButton.setEffect(dropShadow);
        viewPropertyButton.setId(idName);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
        card.setEffect(dropShadow);

        viewPropertyButton.setOnAction(new EventHandler<ActionEvent>() {
            // TODO: Expand button for information about the property
            @Override
            public void handle(ActionEvent event) {
                System.out.println("I was pressed: "+viewPropertyButton.getId());
            }
        });

        return card;
    }

    // Creates the visual(?) cards for the
    private TilePane createNewCardView(PropertyAssessments properties){
        TilePane tmp = createPropertyCardsv2(createPropertyCardsList(properties));
        tmp.setHgap(20);
        tmp.setVgap(20);
        return tmp;
    }

    // Method updates the card view based on a specific argument(s)
    private void filterProperties(PropertyAssessments properties){

        filteredProperties = properties;
        cardView.getChildren().clear(); // remove all children from the cardView

        if (neighborhoodFilter != null){
            filteredProperties = new PropertyAssessments(filteredProperties.filterByNeighborhood(neighborhoodFilter));
            System.out.println("Neighborhood: " + neighborhoodFilter);
        }
        if (addressFilter != null){
            filteredProperties = new PropertyAssessments(filteredProperties.filterByAddress(addressFilter));
            System.out.println("Address: " + addressFilter);
        }
        if (budgetMinFilter >= 0 || budgetMaxFilter >= 0){
            filteredProperties = new PropertyAssessments(filteredProperties.filterByBudget(budgetMinFilter, budgetMaxFilter));
            System.out.println("Budget: " + budgetMinFilter + " - " + budgetMaxFilter);
        }

        createPropertyCards(createPropertyCardsList(filteredProperties), cardView);

    }

    private boolean loadProperties(){
        // -- PropertyAssessments -- //
        String csvFileName = "Property_Assessment_Data_2026_Small.csv";
        try{

            properties = new PropertyAssessments(csvFileName);
            System.out.println(properties.getSize());

            filteredProperties = new PropertyAssessments(properties.filterByAssessmentClass("Residential"));
            System.out.println(filteredProperties.getSize());
        }
        catch(IOException e){
            System.out.println("Error in opening " + csvFileName);
            return false; // do not proceed with no file
        }

        return true;
    }

}
