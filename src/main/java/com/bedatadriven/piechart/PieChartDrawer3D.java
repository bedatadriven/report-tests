package com.bedatadriven.piechart;

import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;

public class PieChartDrawer3D extends PieChartDrawer {
    @Override
    protected CTPieSer getPieSer(CTPlotArea plotArea) {
        return plotArea.getPie3DChartArray(0).getSerArray(0);
    }

    @Override
    protected XDDFChartData getChartData(XWPFChart chart) {
        return chart.createData(ChartTypes.PIE3D, null, null);
    }
}
