package gui;

import crawler.Crawler;
import crawler.threadpool.CustomThreadPool;
import crawler.threadpool.ExecutorProxyThreadPool;
import crawler.threadpool.ThreadPool;
import logger.GuiLogger;
import logger.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;
import java.util.function.Supplier;

final class StartButtonActionListener implements ActionListener {

    private final Logger logger = Logger.getLogger(GuiLogger.class);
    private final MainWindow mainWindow;

    StartButtonActionListener(MainWindow window) {
        mainWindow = window;
    }

    public void actionPerformed(ActionEvent e) {
        logger.clear();
        logger.result(String.format("Starting crawler with %d threads, pool %s, base url: %s%n", mainWindow.getNumberOfThreads(), mainWindow.getThreadPool(), mainWindow.getUrl()));
        try {
            Supplier<ThreadPool> threadPoolSupplier = getThreadPoolSupplier();
            Crawler crawler = new Crawler(mainWindow.getUrl(), mainWindow.getMode(), threadPoolSupplier);
            (new Thread(crawler)).start();
        } catch (URISyntaxException ex) {
            logger.error(String.format("Invalid initial URL: %s", ex.getMessage()));
        }
    }

    private Supplier<ThreadPool> getThreadPoolSupplier() {
        switch (mainWindow.getThreadPool()) {
            case CUSTOM:
                return () -> new CustomThreadPool(mainWindow.getNumberOfThreads());
            case FIXED:
                return () -> new ExecutorProxyThreadPool(mainWindow.getNumberOfThreads());
            default:
                throw new IllegalStateException();
        }
    }
}
