package pl.battleship;


import java.io.IOException;
import java.util.Scanner;

public class Game {

    public static Scanner scanner = new Scanner(System.in);

    public static void promptEnterKey() {
        System.out.println("\nPress Enter and pass the move to another player");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Position setPosition() {

        String coordinate;
        int letter = 0;
        int number = 0;
        boolean isNotValidPosition = true;
        boolean validLetter;
        boolean validNumber;

        while (isNotValidPosition) {
            coordinate = getUserInput(scanner);
            letter = coordinate.toUpperCase().charAt(0) - 64;

            try {
                number = Integer.parseInt(coordinate.substring(1));
                validLetter = letter >= 1 && letter <= 10;
                validNumber = number >= 1 && number <= 10;
                if (validLetter && validNumber) {
                    isNotValidPosition = false;
                } else {
                    System.out.println("\nError! You entered the wrong coordinates! Try again:\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("NumberFormatException error");
            }

        }
        return new Position(letter, number);
    }


    private Ship createShip(int requiredSize, String shipName, char[][] fields) {

        Position p1 = setPosition();
        Position p2 = setPosition();

        if (isTooClose(p1, p2, fields)) {
            System.out.println("\nError! You placed it too close to another one. Try again:\n");
            return null;
        } else if (isIncorrectlyLocated(p1, p2)) {
            System.out.println("\nError! Wrong ship location! Try again:\n");
            return null;
        } else if (!isValidSize(p1, p2, requiredSize)) {
            System.out.println("\nError! Wrong length of the " + shipName + "! Try again:\n");
            return null;
        } else {
            return new Ship(p1, p2, requiredSize);
        }
    }


    private boolean isTooClose(Position p1, Position p2, char[][] fields) {

        int frontLetter = Math.min(p1.getLetter(), p2.getLetter()) - 1;
        int frontNumber = Math.min(p1.getNumber(), p2.getNumber()) - 1;
        int tailLetter = Math.max(p1.getLetter(), p2.getLetter()) - 1;
        int tailNumber = Math.max(p1.getNumber(), p2.getNumber()) - 1;

        for (int i = (Math.max(0, frontLetter - 1)); i <= (Math.min(Board.NUMBER_OF_FIELDS - 1, tailLetter + 1)); i++) {
            for (int j = (Math.max(0, frontNumber - 1)); j <= (Math.min(Board.NUMBER_OF_FIELDS - 1, tailNumber + 1)); j++) {
                if (fields[i][j] != '~') {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean isIncorrectlyLocated(Position p1, Position p2) {
        return p1.getLetter() != p2.getLetter() && p1.getNumber() != p2.getNumber();
    }


    private boolean isValidSize(Position p1, Position p2, int requiredSize) {
        int frontSum = p1.getLetter() + p1.getNumber();
        int tailSum = p2.getLetter() + p2.getNumber();

        if (requiredSize < 2 || requiredSize > 5) {
            return false;
        }
        return frontSum > tailSum ? frontSum - tailSum == requiredSize - 1 : tailSum - frontSum == requiredSize - 1;
    }


    private String getUserInput(Scanner scanner) {
        return scanner.next();
    }


    private void putShipOnBoard(Ship ship, char[][] fields) {

        int frontLetter = Math.min(ship.getFront().getLetter(), ship.getTail().getLetter()) - 1;
        int frontNumber = Math.min(ship.getFront().getNumber(), ship.getTail().getNumber()) - 1;
        int tailLetter = Math.max(ship.getFront().getLetter(), ship.getTail().getLetter()) - 1;
        int tailNumber = Math.max(ship.getFront().getNumber(), ship.getTail().getNumber()) - 1;

        for (int i = frontLetter; i <= tailLetter; i++) {
            for (int j = frontNumber; j <= tailNumber; j++) {
                fields[i][j] = 'O';
            }
        }
    }

    public void deployFleet(Fleet[] fleet, Player player) {

        Board board = player.getPlayerBoard();

        System.out.printf("%s, place your ships on the game field %n%n", player.getName());

        board.printBoard();
        for (int i = 0; i < Fleet.values().length; i++) {
            System.out.printf("\nEnter the coordinates of the %s (%d cells):\n", fleet[i].getName(), fleet[i].getSize());
            System.out.println();
            Ship ship = null;
            while (ship == null) {
                ship = createShip(fleet[i].getSize(), fleet[i].getName(), board.getFields());
            }

            putShipOnBoard(ship, board.getFields());
            System.out.println();
            board.printBoard();
        }
    }

    private Position takeShot() {
        return setPosition();
    }

    private void checkShot(Board playerBoard, Board enemyBoard, Position shot) {


        char[][] playerFields = playerBoard.getFields();
        char[][] enemyFields = enemyBoard.getFields();

        int horizontalIndex = shot.getLetter() - 1;
        int verticalIndex = shot.getNumber() - 1;

        char c = enemyFields[horizontalIndex][verticalIndex];
        String message;

        switch (c) {
            case '~':
                playerFields[horizontalIndex][verticalIndex] = 'M';
                enemyFields[horizontalIndex][verticalIndex] = 'M';
                message = "You missed!";
                break;
            case 'O':
                playerFields[horizontalIndex][verticalIndex] = 'X';
                enemyFields[horizontalIndex][verticalIndex] = 'X';
                String s = hasShipSunk(enemyBoard, shot) ? "You sank a ship!" : "You hit a ship!";
                message = hasShipsLeft(enemyBoard) ? s : "You sank the last ship. You won. Congratulations!";
                break;
            case 'M':
            case 'X':
                message = "You missed!";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + c);
        }
        System.out.println();
        System.out.println(message);
    }


    public void playGame(Player player1, Player player2) {
        Board p1Board = player1.getPlayerBoard();
        Board p1EnemyBoard = player1.getEnemyBoard();
        Board p2Board = player2.getPlayerBoard();
        Board p2EnemyBoard = player2.getEnemyBoard();
        do {
            promptEnterKey();
            player1.printGameStatus();
            System.out.println("\n" + player1.getName() + ", it's your turn:\n");
            Position p1Shot = takeShot();
            checkShot(p1EnemyBoard, p2Board, p1Shot);
            promptEnterKey();
            player2.printGameStatus();
            System.out.println("\n" + player2.getName() + ", it's your turn:\n");
            Position p2Shot = takeShot();
            checkShot(p2EnemyBoard, p1Board, p2Shot);
        } while (hasShipsLeft(p1Board) && hasShipsLeft(p2Board));
    }

    private boolean hasShipSunk(Board p1, Position shot) {
        char[][] fields = p1.getFields();

        int verticalStart = Math.max(0, shot.getLetter() - 2);
        int verticalEnd = Math.min(shot.getLetter(), Board.NUMBER_OF_FIELDS - 1);
        int horizontalStart = Math.max(0, shot.getNumber() - 2);
        int horizontalEnd = Math.min(shot.getNumber(), Board.NUMBER_OF_FIELDS - 1);

        for (int i = verticalStart; i <= verticalEnd; i++) {
            for (int j = horizontalStart; j <= horizontalEnd; j++) {
                if (fields[i][j] == 'O') {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasShipsLeft(Board p1) {
        char[][] fields = p1.getFields();

        for (char[] field : fields) {
            for (char c : field) {
                if (c == 'O') {
                    return true;
                }
            }
        }
        return false;
    }
}






