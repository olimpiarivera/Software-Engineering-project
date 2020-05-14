package it.polimi.ingsw.view.cli;




public class Cell {

    private String[] stripes = new String[5];

    public Cell() {
        for (int i = 0; i < 5; i++) {
            stripes[i] = new String();
        }

    }



    public String uniformStripe(String BackgroundAnsiColour) {
        String stripe = BackgroundAnsiColour + "            " + AnsiCode.RESET;
        return stripe;
    }

    public String domeStripe(String BackgroundAnsiColour1) {
        String stripe = BackgroundAnsiColour1 + "  " + AnsiCode.BACKGROUND_BLUE + "        " + BackgroundAnsiColour1 + "  " + AnsiCode.RESET;
        return stripe;
    }

    public String emptyStripe(){
       String stripe = "            ";
       return stripe;
    }


    public String coordinateXStripe(int value) {
        String num = String.valueOf(value);
        String stripe = "     "+AnsiCode.TEXT_BLACK + "X" + num+ "     "+AnsiCode.RESET;
        return stripe;
    }

    public String coordinateYStripe(int value) {
        String num = String.valueOf(value);
        String stripe = "     "+AnsiCode.TEXT_BLACK + "Y" + num+ "     "+AnsiCode.RESET;
        return stripe;
    }

    public String workerStripe(String BackgroundAnsiColour, int workerNumber) {
        String num = String.valueOf(workerNumber);
        String stripe = BackgroundAnsiColour + "     " + AnsiCode.TEXT_BLACK + "W" + num + "     " + AnsiCode.RESET;
        return stripe;
    }

    public String levelStripe(int level) {
        String num = String.valueOf(level);
        String stripe = AnsiCode.BACKGROUND_WHITE + "    " + AnsiCode.TEXT_BLACK + "LV " + num + "    " + AnsiCode.RESET;
        return stripe;
    }

    public String getStripe(int n) {
        return stripes[n];
    }


}