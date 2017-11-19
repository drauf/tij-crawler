package gui;

import crawler.Crawler;
import crawler.CustomThreadPoolCrawler;
import crawler.FixedThreadPoolCrawler;
import crawler.ThreadPool;
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
        logger.clear();
        logger.result(String.format("Starting crawler with %d threads, pool %s, base url: %s%n", mainWindow.getNumberOfThreads(), mainWindow.getThreadPool(), mainWindow.getUrl()));
        try {
            Crawler crawler;

            switch (mainWindow.getThreadPool()) {
                case CUSTOM:
                    crawler = new CustomThreadPoolCrawler(mainWindow.getUrl(), mainWindow.getNumberOfThreads());
                    break;
                case FIXED:
                default:
                    crawler = new FixedThreadPoolCrawler(mainWindow.getUrl(), mainWindow.getNumberOfThreads());
                    break;
            }

            (new Thread(crawler)).start();
        } catch (URISyntaxException ex) {
            logger.error(String.format("Invalid initial URL: %s", ex.getMessage()));
        }
    }
}
