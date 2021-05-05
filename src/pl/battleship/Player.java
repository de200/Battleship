package pl.battleship;

public class Player {
    private Board playerBoard;
    private Board enemyBoard;
    private String name;

    public Player(String name) {
        this.name = name;
        this.playerBoard = new Board();
        this.enemyBoard = new Board();
    }

    public Board getPlayerBoard() {
        return playerBoard;
    }

    public Board getEnemyBoard() {
        return enemyBoard;
    }

    public String getName() {
        return name;
    }

    public void printGameStatus() {
        enemyBoard.printBoard();
        System.out.println("---------------------");
        playerBoard.printBoard();
    }
}
