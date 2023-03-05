package com.loanCalculator;

import com.coreCalculator.DelayInfo;
import com.coreCalculator.LoanCalculator;
import com.coreCalculator.LoanEntry;
import com.coreCalculator.LoanType;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    private TextField amount, years, months, interestRate, filterFrom, filterTo, delayFrom, delayDuration, delayRate;

    @FXML
    private RadioButton linear;

    @FXML
    private Label invalidAlert;

    @FXML
    private TableView<LoanEntry> tableView;

    @FXML
    private CheckBox delayCheckBox;

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
    private ArrayList<LoanEntry> linearData;
    private ArrayList<LoanEntry> annuityData;


    @FXML
    void calculate(ActionEvent event) {
        tableView.getItems().clear();
        data = null;
        linearData = null;
        annuityData = null;

        DelayInfo delayInfo = null;
        if (delayCheckBox.isSelected()) {
            try {
                int from = Integer.parseInt(delayFrom.getText());
                int duration = Integer.parseInt(delayDuration.getText());
                BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(delayRate.getText()));
                delayInfo = new DelayInfo(from, duration, rate);
            } catch (NumberFormatException e) {
                invalidAlert.setVisible(true);
                return;
            }
        }

        try {
            int yearsI = Integer.parseInt(years.getText());
            int monthsI = Integer.parseInt(months.getText());
            if (Math.min(yearsI, monthsI) < 0)
                throw new NumberFormatException();
            LoanCalculator calc = new LoanCalculator(
                    BigDecimal.valueOf(Double.parseDouble(amount.getText())),
                    yearsI * 12 + monthsI,
                    linear.isSelected() ? LoanType.LINEAR : LoanType.ANNUITY,
                    BigDecimal.valueOf(Double.parseDouble(interestRate.getText())),
                    delayInfo
            );
            data = calc.generateTable();
            linearData = calc.generateTable(LoanType.LINEAR);
            annuityData = calc.generateTable(LoanType.ANNUITY);
            invalidAlert.setVisible(false);
        } catch (NumberFormatException e) {
            invalidAlert.setVisible(true);
            return;
        }
        if (data == null)
            return;
        ObservableList<LoanEntry> items = tableView.getItems();
        items.addAll(data);
    }

    @FXML
    void filter(ActionEvent event) {
        if (data == null)
            return;
        int from, to;
        try {
            from = Integer.parseInt(filterFrom.getText());
            to = Integer.parseInt(filterTo.getText());
        } catch (NumberFormatException e) {
            /*from = 0;
            to = data.get(data.size() - 1).getMonth();
            filterFrom.setText("0");
            filterTo.setText(Integer.toString(to));*/
            resetFilter(event);
            return;
        }
        ObservableList<LoanEntry> items = tableView.getItems();
        items.clear();
        for (LoanEntry datum : data) {
            int month = datum.getMonth();
            if (from <= month && month <= to)
                items.add(datum);
        }
    }

    @FXML
    void resetFilter(ActionEvent event) {
        if (data == null)
            return;
        filterFrom.setText("0");
        filterTo.setText(Integer.toString(data.get(data.size() - 1).getMonth()));
        filter(event);
    }

    @FXML
    void saveFile(ActionEvent event) {
        if (data == null)
            return;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("output.csv");
        Stage stage = (Stage) amount.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        if (file == null)
            return;
        try {
            FileOutputStream stream = new FileOutputStream(file);
            PrintWriter writer = new PrintWriter(stream);
            writer.println("Month,Left,Credit,Interest,Total");
            for (LoanEntry entry: data) {
                writer.println(entry.getMonth() + "," + entry.getLeft() + "," + entry.getCredit() + "," + entry.getInterest() + "," + entry.getTotal());
            }
            writer.flush();
            stream.close();
        } catch (IOException e) {
            return;
        }
    }

    @FXML void showChart(ActionEvent event) {
        new GraphWindow(linearData, annuityData).show();
    }

    @FXML
    void delayToggle(ActionEvent event) {
        delayFrom.setDisable(!delayFrom.isDisabled());
        delayDuration.setDisable(!delayDuration.isDisabled());
        delayRate.setDisable(!delayRate.isDisabled());
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