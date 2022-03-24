package com.bedatadriven;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.Units;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFShapeProperties;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BarChartTest {

    public static String[] COLORS = new String[] { "#7fc97f", "#beaed4", "#fdc086", "#ffff99", "#386cb0", "#f0027f", "#bf5b17"};

    @Test
    public void testBarChart() throws IOException, InvalidFormatException {
        XWPFDocument document = new XWPFDocument();
        XWPFChart chart = document.createChart(15 * Units.EMU_PER_CENTIMETER, 10 * Units.EMU_PER_CENTIMETER);
        chart.setTitleText("My example chart");

        // https://stackoverflow.com/questions/63245147/second-line-in-an-apache-poi-chart-with-seperate-axis

        // create the data
        String[] categories = new String[] { "A", "B", "C"};
        String[] legendTitles = new String[] { "X", "Y", "Z" };
        int numOfPoints = categories.length;
        Double[][] values = new Double[][] { { 10d, 20d, 30d}, {20d, 40d, 50d}, {30d, 50d, 60d}};

        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.BOTTOM);

        // create data sources
        String categoryDataRange = chart.formatRange(new CellRangeAddress(1, numOfPoints, 0, 0));
        XDDFDataSource<String> categoriesData = XDDFDataSourcesFactory.fromArray(categories, categoryDataRange, 0);
        // first line chart
        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
        leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);

        XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
        XDDFBarChartData barChartData = (XDDFBarChartData) data;
        barChartData.setBarDirection(BarDirection.COL);
        barChartData.setBarGrouping(BarGrouping.CLUSTERED);

        for(int i = 0; i < values.length; i++){
            String valuesDataRange = chart.formatRange(new CellRangeAddress(1, numOfPoints, i +1, i +1));
            XDDFNumericalDataSource<Double> valuesData = XDDFDataSourcesFactory.fromArray(values[i], valuesDataRange, i+1);
            XDDFChartData.Series series = data.addSeries(categoriesData, valuesData);
            series.setTitle(legendTitles[i], null);
            applySolidSeries(series, graphElementColor(i));
        }
        chart.plot(data);

        try(OutputStream out = new FileOutputStream("barchart.docx")) {
            document.write(out);
        }
        document.close();
    }

    private static void applySolidSeries(XDDFChartData.Series series, XDDFColor color) {

        XDDFShapeProperties properties = series.getShapeProperties();
        if (properties == null) {
            properties = new XDDFShapeProperties();
        }
        XDDFSolidFillProperties fill = new XDDFSolidFillProperties(color);
        properties.setFillProperties(fill);
        series.setShapeProperties(properties);
    }

    private XDDFColor graphElementColor(int i) {
        String hexColor = COLORS[i % COLORS.length];
        float[] rgb = Color.decode(hexColor).getRGBColorComponents(null);
        int[] rgb_ = new int[rgb.length];
        for(int j = 0; j < rgb_.length; j++) {
            rgb_[j] = (int) (rgb[j]*100_000);
        }

        return XDDFColor.from(rgb_[0], rgb_[1], rgb_[2]);
    }
}
