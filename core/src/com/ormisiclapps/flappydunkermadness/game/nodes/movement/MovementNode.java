package com.ormisiclapps.flappydunkermadness.game.nodes.movement;

/**
 * Created by Anis on 10/30/2016.
 */

public class MovementNode {
    public String type;
    public int points;

    public String[] pointsX;
    public String[] pointsY;

    public float rotationSpeed;

    public MovementNode()
    {

    }

    public void setNode(String type, int points)
    {
        this.type = type;
        this.points = points;
        // Create the points position arrays
        pointsX = new String[points];
        pointsY = new String[points];
    }

    public void setPoint(int position, String pointX, String pointY)
    {
        pointsX[position] = pointX;
        pointsY[position] = pointY;
    }


}
