package com.evan.echartsbackend.result;

import com.evan.echartsbackend.pojo.Chart;


public class Charts {
    Chart chart;

    public Charts (Chart chart) {
        this.chart = chart;
    }

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }
}
