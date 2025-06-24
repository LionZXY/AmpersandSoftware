package ru.lionzxy.ampersand.ui

fun runOnUiThread(block: () -> Unit) {
    javax.swing.SwingUtilities.invokeAndWait { block() }
}