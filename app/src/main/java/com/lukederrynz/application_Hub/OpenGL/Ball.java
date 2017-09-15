package com.lukederrynz.application_Hub.OpenGL;

/**
 * Created by Luke Derry on 13/09/2017.
 *
 */

public class Ball {

    private OpenGL_Square square;
    private float forceX;
    private float forceY;

    // TODO: Velocity or Vector describing movement vector

    /**
     * Constructor, instantiates our square
     *
     */
    public Ball() {
        Vector3f v = MyGLRenderer.BallEmitter;
        square = new OpenGL_Square(v.x, v.y, MyGLRenderer.BallSize);

        this.forceY += MyGLRenderer.gravity;
    }

    public void tick() {
        this.square.setPosition(square.getPosX() + forceX, square.getPosY() + forceY);
    }


    /**
     * Gets this square.
     *
     * @return - OpenGL_Square : This square instance
     */
    public OpenGL_Square getSquare() {
        return this.square;
    }


    public float getForceX() {
        return forceX;
    }
    public float getForceY() {
        return forceY;
    }
    public void setForceX(float x) {
        forceX = x;
    }
    public void setForceY(float y) {
        forceY = y;
    }
    public void addForceX(float x) {
        forceX += x;
    }
    public void addForceY(float y) {
        forceY += y;
    }


    /**
     *
     * @param line
     * @return
     */
    public boolean ballIntersectsLine(OpenGL_Line line) {

        // TODO: Fix this mess!
        Vector3f l1 = new Vector3f(line.getStartVerts());
        float x1 = l1.x;
        float y1 = l1.y;
        Vector3f l2 = new Vector3f(line.getEndVerts());
        float x2 = l2.x;
        float y2 = l2.y;

        float hs = square.getSize();
        float minX = square.getPosX() - hs;
        float maxX = square.getPosX() + hs;
        float minY = square.getPosY() + hs;
        float maxY = square.getPosY() - hs;

        // Outside
        if ((x1 <= minX && x2 <= minX) || (y1 <= minY && y2 <= minY) || (x1 >= maxX && x2 >= maxX) || (y1 >= maxY && y2 >= maxY))
            return false;

        float m = (y2 - y1) / (x2 - x1);

        float y = m * (minX - x1) + y1;
        if (y > minY && y < maxY) return true;

        y = m * (maxX - x1) + y1;
        if (y > minY && y < maxY) return true;

        float x = (minY - y1) / m + x1;
        if (x > minX && x < maxX) return true;

        x = (maxY - y1) / m + x1;
        return x > minX && x < maxX;

    }

    public void bounce(float x1, float y1, float x2, float y2) {
        Vector3f vec = new Vector3f(this.forceX, this.forceY, 0f);

        //Log.d(TAG, "Start FourceX="+this.forceX+"  ForceY="+this.forceY);
        // N
        Vector3f n = new Vector3f(x2 - x1, y2 - y1, 0f);
        n = n.getLeftNormal();
        n.normalize();

        // 2 * V [dot] N
        float dotVec = vec.dot(n) * 2;

        // (2 * V [dot] N ) N
        n.scaleVector(dotVec);

        // V - ( 2 * V [dot] N ) N
        // Change Direction
        Vector3f mvn = vec.minVecNew(n);
        this.forceX = mvn.x * 2;
        this.forceY = mvn.y * 2;
        //Log.d(TAG, "End FourceX="+this.forceX+"  ForceY="+this.forceY);

    }


}
