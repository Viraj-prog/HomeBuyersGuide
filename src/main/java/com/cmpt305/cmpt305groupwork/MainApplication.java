package com.cmpt305.cmpt305groupwork;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        BorderPane borderPane = new BorderPane();
        TilePane propertyCards = new TilePane();
        Scene scene = new Scene(borderPane);

        borderPane.setCenter(propertyCards);
        //Pane newa= createPropertyCardv1();
        //Pane newa2= createPropertyCardv1();
        //propertyCards.getChildren().addAll(newa,newa2);

        propertyCards.getChildren().addAll(createPropertyCardv2(),createPropertyCardv2(),createPropertyCardv2());

        propertyCards.setHgap(20);
        propertyCards.setVgap(20);
        stage.setTitle("CMPT 305");
        stage.setScene(scene);
        stage.show();
    }

    private Pane createPropertyCardv1(){
        //Container
        Pane propertyCard = new Pane();

        //Icon
        //Image homeIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("HomeIcon.png")));
        //ImageView image = new ImageView(homeIcon);

        //AssessedValue
        Text assessedValueText = new Text("$4,000,000");
        assessedValueText.relocate(0,30);

        //Neighborhood Name
        Text neighborhoodText = new Text("Castle Downs");
        neighborhoodText.relocate(0,15);
        //Button
        Button viewPropertyButton = new Button("View");
        viewPropertyButton.relocate(50,30);

        //Address
        Label address = new Label("123 Main Street");


        propertyCard.getChildren().addAll(assessedValueText,neighborhoodText,address,viewPropertyButton);

        return propertyCard;
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

}
