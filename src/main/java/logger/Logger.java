package logger;

public abstract class Logger {

    enum LOGGING_LEVEL {
        DEBUG, INFO, WARN, ERROR, RESULT
    }

    private static final LOGGING_LEVEL loggingLevel = LOGGING_LEVEL.RESULT;

    public static Logger getLogger(Class c) {
        return GuiLogger.getInstance();
    }

    public abstract void setHandler(Object handler);

    abstract void log(String message);

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public void debug(String message) {
        compareLevelAndLog(message, LOGGING_LEVEL.DEBUG);
    }

    public void info(String message) {
        compareLevelAndLog(message, LOGGING_LEVEL.INFO);
    }

    public void warn(String message) {
        compareLevelAndLog(message, LOGGING_LEVEL.WARN);
    }

    public void error(String message) {
        compareLevelAndLog(message, LOGGING_LEVEL.ERROR);
    }

    public void result(String message) {
        compareLevelAndLog(message, LOGGING_LEVEL.RESULT);
    }

    private void compareLevelAndLog(String message, LOGGING_LEVEL level) {
        if (loggingLevel.ordinal() <= level.ordinal()) {
            log(message);
        }
    }
}
