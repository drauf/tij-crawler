package logger;

public abstract class Logger {

    enum LOGGING_LEVEL {
        DEBUG, INFO, WARN, ERROR
    }

    private LOGGING_LEVEL loggingLevel = LOGGING_LEVEL.DEBUG;

    public static Logger getLogger(Class c) {
        return GuiLogger.getInstance();
    }

    public abstract void setHandler(Object handler);

    abstract void log(String message);

    public void setLogLevel(LOGGING_LEVEL level) {
        loggingLevel = level;
    }

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

    private void compareLevelAndLog(String message, LOGGING_LEVEL level) {
        if (loggingLevel.ordinal() <= level.ordinal()) {
            log(message);
        }
    }
}
