package gui;

import logger.GuiLogger;
import logger.Logger;

import javax.swing.*;

import static gui.Constants.*;

public class MainWindow extends JFrame {

    private final Logger logger = Logger.getLogger(GuiLogger.class);

    private int numberOfThreads = 1;
    private final JTextField urlTextField = new JTextField(URL_TEXT_FIELD_TEXT);
    private final JTextArea outputTextArea = new JTextArea();

    public MainWindow() {
        logger.setHandler(outputTextArea);
        initUI();
    }

    private void initUI() {
        getContentPane().setLayout(null);

        urlTextField.setBounds(URL_TEXT_FIELD_X, URL_TEXT_FIELD_Y, URL_TEXT_FIELD_WIDTH, URL_TEXT_FIELD_HEIGHT);
        getContentPane().add(urlTextField);

        JButton startButton = new JButton(START_BUTTON_TEXT);
        startButton.setBounds(START_BUTTON_X, START_BUTTON_Y, START_BUTTON_WIDTH, START_BUTTON_HEIGHT);
        getContentPane().add(startButton);
        startButton.addActionListener(new StartButtonActionListener(urlTextField.getText(), numberOfThreads));

        Integer[] threads = {1, 2, 4, 8, 16, 32};
        JComboBox<Integer> selectThreadsNo = new JComboBox<>(threads);
        selectThreadsNo.setBounds(START_BUTTON_X, START_BUTTON_Y + 20, START_BUTTON_WIDTH, START_BUTTON_HEIGHT);
        getContentPane().add(selectThreadsNo);

        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        scrollPane.setBounds(OUTPUT_TEXT_AREA_X, OUTPUT_TEXT_AREA_Y, OUTPUT_TEXT_AREA_WIDTH, OUTPUT_TEXT_AREA_HEIGHT);
        getContentPane().add(scrollPane);

        setTitle(WINDOW_TITLE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
