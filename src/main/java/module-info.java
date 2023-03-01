module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.loanCalculator to javafx.fxml;
    exports com.loanCalculator;
    exports com.coreCalculator;
    opens com.coreCalculator to javafx.fxml;
}