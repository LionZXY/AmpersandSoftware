package ru.lionzxy.ampersand

import com.fazecast.jSerialComm.SerialPort
import ru.lionzxy.ampersand.chart.ChartPoint
import ru.lionzxy.ampersand.utils.MeasureDecoder

private const val MAX_VALUE = 4095
private const val MAX_VOLTAGE = 5.0
private const val MAX_CURRENT = 3.0

fun main() {
    val updateLooper = UpdateLooper()
    updateLooper.start()
    var serialPort: SerialPort? = null
    while (serialPort == null) {
        for (port in SerialPort.getCommPorts()) {
            if (port.portDescription.contains("STM32 Virtual ComPort")) {
                serialPort = port
            }
        }
    }



    serialPort.openPort()
    try {
        updateLooper.onChangeStartState { enabled ->
            if (enabled) {
                val buffer = "START".toByteArray()
                serialPort.writeBytes(buffer, buffer.size, 0)
            } else {
                val buffer = "STOP".toByteArray()
                serialPort.writeBytes(buffer, buffer.size, 0)
            }
        }
        readLoop(serialPort, updateLooper)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    serialPort.closePort()
}

private fun readLoop(serialPort: SerialPort, updateLooper: UpdateLooper) {
    var offset = 0
    val readBuffer = ByteArray(3)
    while (true) {
        while (serialPort.bytesAvailable() < 4) Thread.sleep(20)

        val numRead = serialPort.readBytes(readBuffer, readBuffer.size - offset, offset)
        if (numRead < 3) {
            println("Read $numRead")
            offset = numRead
            continue;
        }
        val readValue = readBuffer.map { it.toUByte() }.let {
            MeasureDecoder.decode(buffer = it)
        }
        if (readValue != null) {
            val (currentData, voltageData) = readValue
            val current = (currentData.toInt() / MAX_VALUE) * MAX_CURRENT
            val voltage = (voltageData.toInt() / MAX_VALUE) * MAX_VOLTAGE
            updateLooper.addValue(
                ChartPoint(
                    current = current,
                    voltage = voltage
                )
            )
        }
    }
}
