package com.cmpt305.cmpt305groupwork;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField nameField;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Hello, "+ nameField.getText() + " !");

    }
}
