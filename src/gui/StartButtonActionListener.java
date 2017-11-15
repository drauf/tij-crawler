package gui;

import crawler.Crawler;
import logger.GuiLogger;
import logger.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;

class StartButtonActionListener implements ActionListener {

    private final Logger logger = Logger.getLogger(GuiLogger.class);
    private final MainWindow mainWindow;

    StartButtonActionListener(MainWindow window) {
        mainWindow = window;
    }

    public void actionPerformed(ActionEvent e) {
        logger.info(String.format("Starting crawler with %d threads for url: %s\n", mainWindow.getNumberOfThreads(), mainWindow.getUrl()));
        try {
            (new Thread(new Crawler(mainWindow.getUrl(), mainWindow.getNumberOfThreads()))).start();
        } catch (MalformedURLException ex) {
            logger.error(String.format("Invalid initial URL: %s", ex.getMessage()));
        }
    }
}
