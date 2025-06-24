package ru.lionzxy.ampersand.ui

import org.knowm.xchart.XChartPanel
import ru.lionzxy.ampersand.chart.AmpersandChart
import java.awt.BorderLayout
import java.awt.Container
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants
import javax.swing.border.Border

private const val BTN_TITLE_START = "START"
private const val BTN_TITLE_STOP = "STOP"

class MainGUI(
    private val chart: AmpersandChart
) {
    private val frame: JFrame = JFrame("Ampersand")
    private val checkboxPower = JCheckBox("Мощность", chart.isPowerChartEnabled).apply {
        addItemListener { chart.isPowerChartEnabled = isSelected }
    }
    private val checkboxVoltage = JCheckBox("Напряжение", chart.isVoltageChartEnabled).apply {
        addItemListener { chart.isVoltageChartEnabled = isSelected }
    }
    private val checkboxCurrent = JCheckBox("Сила тока", chart.isCurrentChartEnabled).apply {
        addItemListener { chart.isCurrentChartEnabled = isSelected }
    }
    private val stateButton = JButton("START").apply {
        isEnabled = false
    }

    init {
        layout(frame)
    }

    fun start() {
        runOnUiThread {
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
            // Display the window.
            frame.isVisible = true

            frame.size = Dimension(1000, 900)
        }
    }

    fun repaint() {
        frame.repaint()
        chart.revalidate()
    }

    fun onChangeStartState(onChange: (Boolean) -> Unit) {
        stateButton.isEnabled = true
        stateButton.addActionListener {
            if (stateButton.text == BTN_TITLE_START) {
                onChange(true)
                stateButton.text = BTN_TITLE_STOP
            } else {
                onChange(false)
                stateButton.text = BTN_TITLE_START
            }
        }
    }

    private fun layout(pane: Container) {
        val root = BorderLayout()

        pane.layout = root

        val flowPanel = JPanel()
        flowPanel.layout = FlowLayout()


        flowPanel.add(checkboxPower)
        flowPanel.add(checkboxVoltage)
        flowPanel.add(checkboxCurrent)
        flowPanel.add(stateButton)

        pane.add(flowPanel, BorderLayout.PAGE_START)

        pane.add(chart, BorderLayout.CENTER)
    }
}