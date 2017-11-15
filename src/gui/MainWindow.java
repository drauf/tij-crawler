package gui;

import logger.GuiLogger;
import logger.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static gui.Constants.*;

public class MainWindow extends JFrame implements ActionListener {

    private final Logger logger = Logger.getLogger(GuiLogger.class);

    private final JTextField urlTextField = new JTextField(URL_TEXT_FIELD_TEXT);
    private final JTextArea outputTextArea = new JTextArea();

    public MainWindow() {
        initUI();
    }

    private void initUI() {
        getContentPane().setLayout(null);

        urlTextField.setBounds(URL_TEXT_FIELD_X, URL_TEXT_FIELD_Y, URL_TEXT_FIELD_WIDTH, URL_TEXT_FIELD_HEIGHT);
        getContentPane().add(urlTextField);

        JButton startButton = new JButton(START_BUTTON_TEXT);
        startButton.setBounds(START_BUTTON_X, START_BUTTON_Y, START_BUTTON_WIDTH, START_BUTTON_HEIGHT);
        getContentPane().add(startButton);
        startButton.addActionListener(this);

        outputTextArea.setBounds(OUTPUT_TEXT_AREA_X, OUTPUT_TEXT_AREA_Y, OUTPUT_TEXT_AREA_WIDTH, OUTPUT_TEXT_AREA_HEIGHT);
        logger.setHandler(outputTextArea);
        getContentPane().add(outputTextArea);

        setTitle(WINDOW_TITLE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        logger.debug(String.format("Starting crawler for url: %s\n", urlTextField.getText()));
    }
}
