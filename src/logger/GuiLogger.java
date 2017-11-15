package logger;

import javax.swing.*;

public class GuiLogger extends Logger {

    private final static GuiLogger instance = new GuiLogger();
    private static JTextArea outputTextArea;

    private GuiLogger() {
    }

    static GuiLogger getInstance() {
        return instance;
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
