module comp261.assig1 {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;

    opens comp261.assig1 to javafx.fxml;
    exports comp261.assig1;
}
