package ru.lionzxy.ampersand

import com.fazecast.jSerialComm.SerialPort
import ru.lionzxy.ampersand.utils.MeasureDecoder
import java.nio.ByteBuffer

private const val MAX_VALUE = 4095
private const val MAX_VOLTAGE = 5.0
private const val MAX_CURRENT = 3.0

fun main() {
    val chartGUI = ChartGUI()
    chartGUI.start()
    var serialPort: SerialPort? = null
    for (port in SerialPort.getCommPorts()) {
        if (port.getPortDescription().contains("STM32 Virtual ComPort")) {
            serialPort = port
        }
    }
    if (serialPort == null) {
        return
    }

    serialPort.openPort()
    try {
        val test = "START".toByteArray()
        serialPort.writeBytes(test, test.size, 0)
        while (true) {
            while (serialPort.bytesAvailable() < 4) Thread.sleep(20)

            val readBuffer = ByteArray(3)
            val numRead = serialPort.readBytes(readBuffer, readBuffer.size, 0)
            println("Read $numRead")
            val readValue = readBuffer.map { it.toUByte() }.let {
                MeasureDecoder.decode(buffer = it)
            }
            if (readValue != null) {
                val (currentData, voltageData) = readValue
                val current = (currentData.toInt() / MAX_VALUE) * MAX_CURRENT
                val voltage = (voltageData.toInt() / MAX_VALUE) * MAX_VOLTAGE
                //chartGUI.addValue(readValue)
            }
            println("Read $readValue")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    serialPort.closePort()
}
