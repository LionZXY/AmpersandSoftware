package ru.lionzxy.ampersand;

import com.fazecast.jSerialComm.SerialPort;

import java.nio.ByteBuffer;

public class Main {
    public static void main(String... args) {
        ChartGUI chartGUI = new ChartGUI();
        chartGUI.start();
        SerialPort serialPort = null;
        for (SerialPort port : SerialPort.getCommPorts()) {
            if (port.getPortDescription().contains("CP2102")) {
                serialPort = port;
            }
        }
        if (serialPort == null) {
            return;
        }

        serialPort.openPort();
        try {
            byte[] test = "START".getBytes();
            serialPort.writeBytes(test, test.length);
            while (true) {
                while (serialPort.bytesAvailable() < 4)
                    Thread.sleep(20);

                byte[] readBuffer = new byte[4];
                int numRead = serialPort.readBytes(readBuffer, readBuffer.length);
                int readValue = convertBytesToInt(readBuffer);
                System.out.println("Read " + readValue);
                chartGUI.addValue(readValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        serialPort.closePort();
    }

    private static int convertBytesToInt(byte[] readBuffer) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(readBuffer);
        return byteBuffer.getInt();
    }
}
