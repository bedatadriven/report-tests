package com.bedatadriven;

import com.bedatadriven.piechart.PieChartDrawer;
import com.bedatadriven.piechart.PieChartDrawer2D;
import com.bedatadriven.piechart.PieChartDrawer3D;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PieChartTest {
    private Map<String, Double> data;
    private XWPFDocument document;
    private XWPFChart chart;

    @BeforeEach
    void setUp() throws IOException, InvalidFormatException {
        InputStream testData = PieChartTest.class.getClassLoader().getResourceAsStream("testData");
        data = new HashMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(testData)));
        String line;
        while ((line = reader.readLine()) != null) {
            Pattern pattern = Pattern.compile("(\\S+) (.+)$");
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                String team = matcher.group(2);
                data.put(team, data.getOrDefault(team, 0.0) + 1);
            }
        }
        data = data.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(
                        Collectors.toMap(
                                e -> e.getValue() == 1 ? "Teams have 1 Champ." : e.getKey(),
                                Map.Entry::getValue, Double::sum, LinkedHashMap::new));
        document = new XWPFDocument();
        chart = document.createChart(18 * Units.EMU_PER_CENTIMETER, 20 * Units.EMU_PER_CENTIMETER);
    }

    @Test
    void test2DPieChart() {
        PieChartDrawer pieChartDrawer = new PieChartDrawer2D();
        pieChartDrawer.drawChart(chart, "Netherlands League Championships", data);
        assertNotNull(pieChartDrawer);
    }

    @Test
    void test3DPieChart() {
        PieChartDrawer pieChartDrawer = new PieChartDrawer3D();
        pieChartDrawer.drawChart(chart, "Netherlands League Championships", data);
        assertNotNull(pieChartDrawer);
    }

    @AfterEach
    void tearDown(TestInfo testInfo) throws IOException {
        if (testInfo.getTestMethod().isPresent()) {
            try (OutputStream out = new FileOutputStream(testInfo.getTestMethod().get().getName() + ".docx")) {
                document.write(out);
            }
        }
        document.close();
    }
}
