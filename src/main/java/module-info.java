module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.loanCalculator to javafx.fxml;
    exports com.loanCalculator;
}