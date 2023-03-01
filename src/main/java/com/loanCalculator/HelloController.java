package com.loanCalculator;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class HelloController implements Initializable {
    @FXML
    private TextField amount, years, months, interestRate;

    @FXML
    private RadioButton linear;

    @FXML
    private Label invalidAlert;

    @FXML
    private TableView<LoanEntry> tableView;

    @FXML
    private TableColumn<LoanEntry, Integer> month;
    @FXML
    private TableColumn<LoanEntry, BigDecimal> left;
    @FXML
    private TableColumn<LoanEntry, BigDecimal> credit;
    @FXML
    private TableColumn<LoanEntry, BigDecimal> interest;
    @FXML
    private TableColumn<LoanEntry, BigDecimal> total;


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
                    new BigDecimal(Double.parseDouble(amount.getText())),
                    yearsI * 12 + monthsI,
                    linear.isSelected() ? LoanType.LINEAR : LoanType.ANNUITY,
                    new BigDecimal(Double.parseDouble(interestRate.getText()))
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        month.setCellValueFactory(new PropertyValueFactory<LoanEntry, Integer>("month"));
        left.setCellValueFactory(new PropertyValueFactory<LoanEntry, BigDecimal>("left"));
        credit.setCellValueFactory(new PropertyValueFactory<LoanEntry, BigDecimal>("credit"));
        interest.setCellValueFactory(new PropertyValueFactory<LoanEntry, BigDecimal>("interest"));
        total.setCellValueFactory(new PropertyValueFactory<LoanEntry, BigDecimal>("total"));
    }
}