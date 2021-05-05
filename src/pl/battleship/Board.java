package pl.battleship;

import java.util.Arrays;

public class Board {

    public final static int NUMBER_OF_FIELDS = 10;

    private final char[][] fields;

    public Board() {
        this.fields = new char[NUMBER_OF_FIELDS][NUMBER_OF_FIELDS];
        setFogOfWar();
    }

    public char[][] getFields() {
        return fields;
    }


    private void setFogOfWar() {
        for (char[] field : fields) {
            Arrays.fill(field, '~');
        }
    }

    public void printBoard() {
        for (int i = 0; i <= NUMBER_OF_FIELDS; i++) {
            for (int j = 0; j <= NUMBER_OF_FIELDS; j++) {
                if (i == 0 && j == 0) {
                    System.out.print("  ");
                } else if (i == 0) {
                    System.out.print(j + " ");
                } else if (j == 0) {
                    System.out.print((char) (i + 64) + " ");
                } else {
                    System.out.print(fields[i - 1][j - 1] + " ");
                }
            }
            System.out.println();
        }
    }

}
