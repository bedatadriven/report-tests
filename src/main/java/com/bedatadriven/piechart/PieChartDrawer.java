package com.bedatadriven.piechart;

import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;

import java.util.Map;

public abstract class PieChartDrawer {

    public void drawChart(XWPFChart chart, String title, Map<String, Double> data) {
        chart.setTitleText(title);
        chart.setTitleOverlay(false);
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.RIGHT);
        XDDFDataSource<String> cat = XDDFDataSourcesFactory.fromArray(data.keySet().toArray(new String[0]));
        XDDFNumericalDataSource<Double> val = XDDFDataSourcesFactory.fromArray(data.values().toArray(new Double[0]));

        XDDFChartData chartData = getChartData(chart);
        chartData.setVaryColors(true);
        XDDFChartData.Series series = chartData.addSeries(cat, val);

        chart.plot(chartData);
        CTPlotArea plotArea = chart.getCTChart().getPlotArea();
        CTPieSer ctPieSer = getPieSer(plotArea);
        if (!ctPieSer.isSetDLbls()) {
            ctPieSer.addNewDLbls();
        }
        ctPieSer.getDLbls().addNewShowPercent().setVal(true);
        ctPieSer.getDLbls().addNewShowVal().setVal(true);

        setPieColors(series.getCategoryData().getPointCount(), ctPieSer);
    }

    private void setPieColors(int categoryCount, CTPieSer ctPieSer) {
        for (int i = 0; i < categoryCount; i++) {
            ctPieSer.addNewDPt().addNewIdx().setVal(i);
            ctPieSer.getDPtArray(i)
                    .addNewSpPr()
                    .addNewSolidFill()
                    .addNewSrgbClr()
                    .setVal(DefaultIndexedColorMap.getDefaultRGB(i + 20));
        }
    }

    protected abstract CTPieSer getPieSer(CTPlotArea plotArea);

    protected abstract XDDFChartData getChartData(XWPFChart chart);
}
