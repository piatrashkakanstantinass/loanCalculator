package com.loanCalculator;

import com.coreCalculator.LoanEntry;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GraphWindow extends LineChart<Number,Number> {

    private Stage stage = new Stage();

    public GraphWindow(ArrayList<LoanEntry> linearData, ArrayList<LoanEntry> annuityData) {
        super(new NumberAxis(), new NumberAxis());
        if (linearData == null)
            linearData = new ArrayList<>();
        if (annuityData == null)
            annuityData = new ArrayList<>();
        NumberAxis xAxis = (NumberAxis) this.getXAxis();
        NumberAxis yAxis = (NumberAxis) this.getYAxis();
        xAxis.setLabel("Month");
        yAxis.setLabel("Interest");
        XYChart.Series linearSeries = new XYChart.Series();
        linearSeries.setName("Linear");
        XYChart.Series annuitySeries = new XYChart.Series();
        annuitySeries.setName("Annuity");
        for (LoanEntry entry: linearData) {
            linearSeries.getData().add(new XYChart.Data(entry.getMonth(), entry.getInterest()));
        }
        for (LoanEntry entry: annuityData) {
            annuitySeries.getData().add(new XYChart.Data(entry.getMonth(), entry.getInterest()));
        }
        this.getData().add(linearSeries);
        this.getData().add(annuitySeries);
        this.setCreateSymbols(false);
        Scene scene = new Scene(this, 800, 800);
        stage.setScene(scene);
        stage.setTitle("Loan graph");
    }

    public void show() {
        stage.show();
    }
}
