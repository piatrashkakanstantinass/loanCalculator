package com.loanCalculator;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;


public class HelloController {
    @FXML
    private TextField amount, years, months, interestRate;

    @FXML
    private RadioButton linear;

    @FXML
    private Label invalidAlert;

    @FXML
    private TableView tableView;

    private ArrayList<LoanEntry> data;

    @FXML
    void calculate(ActionEvent event) {
        tableView.getItems().clear();
        try {
            int yearsI = Integer.parseInt(years.getText());
            int monthsI = Integer.parseInt(months.getText());
            if (Math.min(yearsI, monthsI) < 0)
                throw new NumberFormatException();
            LoanCalculator calc = new LoanCalculator(
                    Double.parseDouble(amount.getText()),
                    yearsI * 12 + monthsI,
                    linear.isSelected() ? LoanType.LINEAR : LoanType.ANNUITY,
                    Double.parseDouble(interestRate.getText())
            );
            data = calc.generateTable();
            invalidAlert.setVisible(false);
        } catch (NumberFormatException e) {
            invalidAlert.setVisible(true);
        }
        if (data == null)
            return;
        ObservableList items = tableView.getItems();
        for (int i = 0; i < data.size(); ++i) {
            items.add(data.get(i));
        }
    }
}