package ru.lionzxy.ampersand;

import com.fazecast.jSerialComm.SerialPort;

import java.nio.ByteBuffer;
import java.util.Random;

public class ExperimentMain {
    private static final Random RANDOM = new Random();

    public static void main(String... args) throws InterruptedException {
        ChartGUI chartGUI = new ChartGUI();
        chartGUI.start();

        double baseline = 0.442;
        while (true) {
            int iterationNear = RANDOM.nextInt(3000);
            baseline = baseline + RANDOM.nextDouble() * 0.1 - 0.05;
            for (int i = 1; i <= iterationNear; i++) {
                double newValue;

                if (RANDOM.nextDouble() < 0.1) {
                    newValue = (baseline + RANDOM.nextDouble() * 0.5 - 0.25) * 1000;
                } else {
                    newValue = (baseline + RANDOM.nextDouble() * 0.1 - 0.05) * 1000;
                }
                chartGUI.addValue((int) newValue);
                Thread.sleep(1L);
            }
        }
    }

}
