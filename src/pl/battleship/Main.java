package pl.battleship;


public class Main {


    public static void main(String[] args) {
        Game game = new Game();
        Fleet[] fleet = Fleet.values();

        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");

        game.deployFleet(fleet, player1);
        Game.promptEnterKey();
        game.deployFleet(fleet, player2);

        game.playGame(player1, player2);

    }

}

