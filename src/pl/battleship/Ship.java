package pl.battleship;

public class Ship {

    private Position front;
    private Position tail;
    private int size;


    public Ship(Position front, Position tail, int size) {
        this.front = front;
        this.tail = tail;
        this.size = size;
    }

    public Position getFront() {
        return front;
    }

    public Position getTail() {
        return tail;
    }

}


