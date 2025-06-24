package ru.lionzxy.ampersand.ui

import org.knowm.xchart.XChartPanel
import org.knowm.xchart.internal.chartpart.Chart
import java.awt.Container
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JFrame
import javax.swing.WindowConstants

class MainGUI<T : Chart<*, *>>(
    chart: T
) {
    private val frame: JFrame = JFrame("Ampersand")
    private val checkboxPower = JCheckBox("Мощность")
    private val checkboxVoltage = JCheckBox("Напряжение")
    private val checkboxCurrent = JCheckBox("Сила тока")
    private val start = JButton("START")
    private val chartPanel = XChartPanel(chart)


    init {
        init(frame)
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
        frame.repaint()
    }

    private fun init(pane: Container) {
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
        pane.add(start, c)

        c.weightx = 0.0
        c.gridwidth = 4
        c.gridx = 0
        c.gridy = 1
        pane.add(chartPanel, c)
    }
}