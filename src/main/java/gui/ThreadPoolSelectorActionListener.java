package gui;

import enums.ThreadPool;
import logger.GuiLogger;
import logger.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class ThreadPoolSelectorActionListener implements ActionListener {

    private final Logger logger = Logger.getLogger(GuiLogger.class);
    private final MainWindow mainWindow;

    ThreadPoolSelectorActionListener(MainWindow window) {
        mainWindow = window;
    }

    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        String threadPool = (String) cb.getSelectedItem();
        logger.debug(String.format("Selected thread pool: %s%n", threadPool));
        mainWindow.setThreadPool("CustomThreadPool".equals(threadPool) ? ThreadPool.CUSTOM : ThreadPool.FIXED);
    }
}
