package ru.lionzxy.ampersand;

import kotlin.Pair;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import ru.lionzxy.ampersand.utils.ListWithTimeLimit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChartGUI extends Thread {
    private static final long PLOT_WIDTH_MILLIS = TimeUnit.MINUTES.toMillis(1);
    private static final int FPS = 30;
    private static final long SLEEP_TIME = 1000 / FPS;
    private ListWithTimeLimit<Integer> listWithTimeLimit = new ListWithTimeLimit<>(PLOT_WIDTH_MILLIS);
    private long initialTime = 0;
    private final XYChart chart;
    private final SwingWrapper<XYChart> swingWrapper;
    private boolean isDirty = false;

    public ChartGUI() {
        initialTime = System.currentTimeMillis();
        double[][] initdata = getData();

        chart = QuickChart.getChart("Ampersand", "time (ms)", "power (µW)", "power", initdata[0], initdata[1]);
        chart.getStyler().setYAxisMin(0.0);
        chart.getStyler().setYAxisMax(15000.0);
        swingWrapper = new SwingWrapper<>(chart);
    }

    public synchronized void addValue(int value) {
        listWithTimeLimit.add(value, System.currentTimeMillis());
        isDirty = true;
    }

    @Override
    public synchronized void start() {
        super.start();
        swingWrapper.displayChart();
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                Thread.sleep(SLEEP_TIME);
                if (isDirty) {
                    onUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void onUpdate() {
        double[][] data = getData();
        javax.swing.SwingUtilities.invokeLater(() -> {

            chart.updateXYSeries("power", data[0], data[1], null);
            swingWrapper.repaintChart();
        });
        isDirty = false;
    }

    private double[][] getData() {
        final List<Pair<Long, Integer>> local = new ArrayList(listWithTimeLimit);
        if (local.isEmpty()) {
            return new double[][]{new double[]{0}, new double[]{0}};
        }
        double[] xData = new double[local.size()];
        double[] yData = new double[local.size()];

        for (int i = 0; i < local.size(); i++) {
            final Pair<Long, Integer> point = local.get(i);
            xData[i] = point.getFirst() - initialTime;
            yData[i] = point.getSecond();
        }

        return new double[][]{xData, yData};
    }
}
