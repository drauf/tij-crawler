package gui;

import enums.CrawlerMode;
import logger.GuiLogger;
import logger.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class ModeSelectorActionListener implements ActionListener {

    private final Logger logger = Logger.getLogger(GuiLogger.class);
    private final MainWindow mainWindow;

    ModeSelectorActionListener(MainWindow window) {
        mainWindow = window;
    }

    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        String mode = (String) cb.getSelectedItem();
        logger.debug(String.format("Selected mode: %s%n", mode));
        mainWindow.setMode("Synchronous".equals(mode) ? CrawlerMode.SYNC : CrawlerMode.ASYNC);
    }
}
