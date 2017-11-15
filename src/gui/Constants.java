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
    static final String URL_TEXT_FIELD_TEXT = "http://www.pg.gda.pl/~manus/RAI/index.html";

    static final int START_BUTTON_X = URL_TEXT_FIELD_WIDTH + URL_TEXT_FIELD_X + MARGIN;
    static final int START_BUTTON_Y = 1 + MARGIN;
    static final int START_BUTTON_WIDTH = WINDOW_WIDTH - URL_TEXT_FIELD_WIDTH - MARGIN * 2;
    static final int START_BUTTON_HEIGHT = URL_TEXT_FIELD_HEIGHT;
    static final String START_BUTTON_TEXT = "Start crawler";

    static final int OUTPUT_TEXT_AREA_X = MARGIN;
    static final int OUTPUT_TEXT_AREA_Y = URL_TEXT_FIELD_Y + URL_TEXT_FIELD_HEIGHT + MARGIN * 2;
    static final int OUTPUT_TEXT_AREA_WIDTH = WINDOW_WIDTH - MARGIN * 2;
    static final int OUTPUT_TEXT_AREA_HEIGHT = WINDOW_HEIGHT - URL_TEXT_FIELD_Y - URL_TEXT_FIELD_HEIGHT - MARGIN * 5;
}
