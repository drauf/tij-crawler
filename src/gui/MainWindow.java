package gui;

import crawler.CrawlerMode;
import logger.GuiLogger;
import logger.Logger;

import javax.swing.*;

import static gui.Constants.*;

public class MainWindow extends JFrame {

    private final Logger logger = Logger.getLogger(GuiLogger.class);

    private int numberOfThreads = 1;
    private CrawlerMode mode = CrawlerMode.SYNC;
    private final JTextField urlTextField = new JTextField(URL_TEXT_FIELD_TEXT);
    private final JTextArea outputTextArea = new JTextArea();

    public MainWindow() {
        logger.setHandler(outputTextArea);
        initUI();
    }

    String getUrl() {
        return urlTextField.getText();
    }

    int getNumberOfThreads() {
        return numberOfThreads;
    }

    void setNumberOfThreads(int threads) {
        numberOfThreads = threads;
    }

    CrawlerMode getMode() {
        return mode;
    }

    void setMode(CrawlerMode m) {
        mode = m;
    }

    private void initUI() {
        getContentPane().setLayout(null);

        urlTextField.setBounds(URL_TEXT_FIELD_X, URL_TEXT_FIELD_Y, URL_TEXT_FIELD_WIDTH, URL_TEXT_FIELD_HEIGHT);
        getContentPane().add(urlTextField);

        JButton startButton = new JButton(START_BUTTON_TEXT);
        startButton.setBounds(START_BUTTON_X, START_BUTTON_Y, START_BUTTON_WIDTH, START_BUTTON_HEIGHT);
        getContentPane().add(startButton);
        startButton.addActionListener(new StartButtonActionListener(this));

        JLabel threadsLabel = new JLabel(THREADS_LABEL_TEXT);
        threadsLabel.setBounds(THREADS_LABEL_X, THREADS_LABEL_Y, THREADS_LABEL_WIDTH, THREADS_LABEL_HEIGHT);
        getContentPane().add(threadsLabel);

        Integer[] threads = {1, 2, 4, 8, 16, 32};
        JComboBox<Integer> selectThreadsNo = new JComboBox<>(threads);
        selectThreadsNo.setBounds(THREADS_SELECT_X, THREADS_SELECT_Y, THREADS_SELECT_WIDTH, THREADS_SELECT_HEIGHT);
        getContentPane().add(selectThreadsNo);
        selectThreadsNo.addActionListener(new ThreadSelectorActionListener(this));

        JLabel selectModeLabel = new JLabel(SELECT_MODE_LABEL_TEXT);
        selectModeLabel.setBounds(SELECT_MODE_LABEL_X, SELECT_MODE_LABEL_Y, SELECT_MODE_LABEL_WIDTH, SELECT_MODE_LABEL_HEIGHT);
        getContentPane().add(selectModeLabel);

        String[] modes = {"Synchronous", "Asynchronous"};
        JComboBox<String> selectMode = new JComboBox<>(modes);
        selectMode.setBounds(SELECT_MODE_X, SELECT_MODE_Y, SELECT_MODE_WIDTH, SELECT_MODE_HEIGHT);
        getContentPane().add(selectMode);
        selectMode.addActionListener(new ModeSelectorActionListener(this));

        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        scrollPane.setBounds(OUTPUT_TEXT_AREA_X, OUTPUT_TEXT_AREA_Y, OUTPUT_TEXT_AREA_WIDTH, OUTPUT_TEXT_AREA_HEIGHT);
        getContentPane().add(scrollPane);

        setTitle(WINDOW_TITLE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
