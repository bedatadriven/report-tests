package com.bedatadriven.piechart;

import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;

public class PieChartDrawer2D extends PieChartDrawer {
    @Override
    protected CTPieSer getPieSer(CTPlotArea plotArea) {
        return plotArea.getPieChartArray(0).getSerArray(0);
    }

    @Override
    protected XDDFChartData getChartData(XWPFChart chart) {
        return chart.createData(ChartTypes.PIE, null, null);
    }
}
