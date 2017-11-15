package gui;

import crawler.Crawler;
import logger.GuiLogger;
import logger.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class StartButtonActionListener implements ActionListener {

    private final Logger logger = Logger.getLogger(GuiLogger.class);
    private final String initialUrl;
    private final int numberOfThreads;

    StartButtonActionListener(String url, int threads) {
        initialUrl = url;
        numberOfThreads = threads;
    }

    public void actionPerformed(ActionEvent e) {
        logger.debug(String.format("Starting crawler for url: %s\n", initialUrl));
        (new Thread(new Crawler(initialUrl, numberOfThreads))).start();
    }
}
