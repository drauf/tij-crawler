package logger;

import javax.swing.*;

public class GuiLogger extends Logger {

    private static JTextArea outputTextArea;

    GuiLogger() {
    }

    @Override
    public void setHandler(Object handler) {
        if (handler instanceof JTextArea) {
            outputTextArea = (JTextArea) handler;
        } else {
            throw new IllegalArgumentException("Trying to initialize GuiLogger with handler that is not JTextArea");
        }
    }

    @Override
    public void clear() {
        outputTextArea.setText(null);
    }

    @Override
    void log(String message) {
        outputTextArea.append(message);
    }
}
