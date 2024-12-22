package org.example;

import java.awt.Point;
import java.util.LinkedList;

public class Snake {
    private final LinkedList<Point> body;

    public Snake() {
        body = new LinkedList<Point>();
    }

    public LinkedList<Point> getBody() {
        return this.body;
    }

    public Point getHead() {
        return body.getFirst();
    }

    public void initHead(int boardWidth, int boardHeight) {
        body.add(new Point(boardWidth / 2, boardHeight / 2));
    }

    public void addHead(Point point) {
        body.addFirst(point);
    }

    public void removeTail() {
        body.removeLast();
    }
}
