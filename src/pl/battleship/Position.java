package pl.battleship;

public class Position {

    private int letter;
    private int number;

    public Position(int letter, int number) {
        this.letter = letter;
        this.number = number;
    }

    public int getLetter() {
        return letter;
    }

    public int getNumber() {
        return number;
    }


}
