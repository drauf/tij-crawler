package gui;

import logger.GuiLogger;
import logger.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ThreadSelectorActionListener implements ActionListener {

    private final Logger logger = Logger.getLogger(GuiLogger.class);
    private final MainWindow mainWindow;

    ThreadSelectorActionListener(MainWindow window) {
        mainWindow = window;
    }

    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        Integer threads = (Integer) cb.getSelectedItem();
        logger.debug(String.format("Selected number of threads: %s\n", threads));
        mainWindow.setNumberOfThreads(threads);
    }
}
