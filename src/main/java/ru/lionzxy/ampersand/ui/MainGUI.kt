package ru.lionzxy.ampersand.ui

import org.knowm.xchart.XChartPanel
import ru.lionzxy.ampersand.chart.AmpersandChart
import java.awt.Container
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JFrame
import javax.swing.WindowConstants

private const val BTN_TITLE_START = "START"
private const val BTN_TITLE_STOP = "STOP"

class MainGUI(
    chart: AmpersandChart
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
    private val chartPanel = XChartPanel(chart)

    init {
        layout(frame)
    }

    fun start() {
        runOnUiThread {
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
            // Display the window.
            frame.pack()
            frame.setLocationRelativeTo(null)
            frame.isVisible = true
        }
    }

    fun repaint() {
        chartPanel.revalidate()
        chartPanel.repaint()
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
        val root = GridBagLayout()

        pane.layout = root

        val c = GridBagConstraints()
        c.fill = GridBagConstraints.HORIZONTAL
        c.gridx = 0
        c.gridy = 0
        pane.add(checkboxPower, c)

        c.gridx = 1
        pane.add(checkboxVoltage, c)

        c.gridx = 2
        pane.add(checkboxCurrent, c)

        c.gridx = 3
        pane.add(stateButton, c)

        c.weightx = 0.0
        c.gridwidth = 4
        c.gridx = 0
        c.gridy = 1
        pane.add(chartPanel, c)
    }
}