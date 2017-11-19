package gui;

final class Constants {

    private static final int MARGIN = 10;

    static final int WINDOW_WIDTH = 800;
    static final int WINDOW_HEIGHT = 600;
    static final String WINDOW_TITLE = "TIJ2017 - 154962";

    static final int URL_TEXT_FIELD_X = MARGIN;
    static final int URL_TEXT_FIELD_Y = MARGIN;
    static final int URL_TEXT_FIELD_WIDTH = 640;
    static final int URL_TEXT_FIELD_HEIGHT = 28;
    static final String URL_TEXT_FIELD_TEXT = "http://www.pg.gda.pl/~manus/index.html";

    static final int START_BUTTON_X = URL_TEXT_FIELD_WIDTH + URL_TEXT_FIELD_X + MARGIN;
    static final int START_BUTTON_Y = 1 + MARGIN;
    static final int START_BUTTON_WIDTH = WINDOW_WIDTH - URL_TEXT_FIELD_WIDTH - MARGIN * 2 - 5;
    static final int START_BUTTON_HEIGHT = URL_TEXT_FIELD_HEIGHT;
    static final String START_BUTTON_TEXT = "Start crawler";

    static final int THREADS_LABEL_X = MARGIN;
    static final int THREADS_LABEL_Y = URL_TEXT_FIELD_Y + URL_TEXT_FIELD_HEIGHT + MARGIN;
    static final int THREADS_LABEL_WIDTH = 125;
    static final int THREADS_LABEL_HEIGHT = URL_TEXT_FIELD_HEIGHT;
    static final String THREADS_LABEL_TEXT = "Number of threads:";

    static final int THREADS_SELECT_X = THREADS_LABEL_X + THREADS_LABEL_WIDTH;
    static final int THREADS_SELECT_Y = 1 + URL_TEXT_FIELD_Y + URL_TEXT_FIELD_HEIGHT + MARGIN;
    static final int THREADS_SELECT_WIDTH = 85;
    static final int THREADS_SELECT_HEIGHT = URL_TEXT_FIELD_HEIGHT;

    static final int SELECT_MODE_LABEL_X = THREADS_SELECT_X + THREADS_SELECT_WIDTH + MARGIN * 2;
    static final int SELECT_MODE_LABEL_Y = URL_TEXT_FIELD_Y + URL_TEXT_FIELD_HEIGHT + MARGIN;
    static final int SELECT_MODE_LABEL_WIDTH = 85;
    static final int SELECT_MODE_LABEL_HEIGHT = URL_TEXT_FIELD_HEIGHT;
    static final String SELECT_MODE_LABEL_TEXT = "Select mode:";

    static final int SELECT_MODE_X = SELECT_MODE_LABEL_X + SELECT_MODE_LABEL_WIDTH;
    static final int SELECT_MODE_Y = 1 + URL_TEXT_FIELD_Y + URL_TEXT_FIELD_HEIGHT + MARGIN;
    static final int SELECT_MODE_WIDTH = 150;
    static final int SELECT_MODE_HEIGHT = URL_TEXT_FIELD_HEIGHT;

    static final int SELECT_THREAD_POOL_LABEL_X = SELECT_MODE_X + SELECT_MODE_WIDTH + MARGIN * 2;
    static final int SELECT_THREAD_POOL_LABEL_Y = URL_TEXT_FIELD_Y + URL_TEXT_FIELD_HEIGHT + MARGIN;
    static final int SELECT_THREAD_POOL_LABEL_WIDTH = 120;
    static final int SELECT_THREAD_POOL_LABEL_HEIGHT = URL_TEXT_FIELD_HEIGHT;
    static final String SELECT_THREAD_POOL_LABEL_TEXT = "Select thread pool:";

    static final int SELECT_THREAD_POOL_X = SELECT_THREAD_POOL_LABEL_X + SELECT_THREAD_POOL_LABEL_WIDTH;
    static final int SELECT_THREAD_POOL_Y = 1 + URL_TEXT_FIELD_Y + URL_TEXT_FIELD_HEIGHT + MARGIN;
    static final int SELECT_THREAD_POOL_WIDTH = 180;
    static final int SELECT_THREAD_POOL_HEIGHT = URL_TEXT_FIELD_HEIGHT;

    static final int OUTPUT_TEXT_AREA_X = MARGIN;
    static final int OUTPUT_TEXT_AREA_Y = THREADS_LABEL_Y + THREADS_LABEL_HEIGHT + MARGIN;
    static final int OUTPUT_TEXT_AREA_WIDTH = WINDOW_WIDTH - MARGIN * 2;
    static final int OUTPUT_TEXT_AREA_HEIGHT = WINDOW_HEIGHT - URL_TEXT_FIELD_Y - URL_TEXT_FIELD_HEIGHT - MARGIN * 9;
}
