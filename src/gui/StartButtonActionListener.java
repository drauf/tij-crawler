package gui;

import crawler.Crawler;
import logger.GuiLogger;
import logger.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;

class StartButtonActionListener implements ActionListener {

    private final Logger logger = Logger.getLogger(GuiLogger.class);
    private final MainWindow mainWindow;

    StartButtonActionListener(MainWindow window) {
        mainWindow = window;
    }

    public void actionPerformed(ActionEvent e) {
        logger.result(String.format("Starting crawler with %d threads with base url: %s%n", mainWindow.getNumberOfThreads(), mainWindow.getUrl()));
        try {
            (new Thread(new Crawler(mainWindow.getUrl(), mainWindow.getNumberOfThreads()))).start();
        } catch (URISyntaxException ex) {
            logger.error(String.format("Invalid initial URL: %s", ex.getMessage()));
        }
    }
}
