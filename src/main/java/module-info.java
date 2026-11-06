module com.cmpt305.cmpt305groupwork {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.cmpt305.cmpt305groupwork to javafx.fxml;
    exports com.cmpt305.cmpt305groupwork;
}