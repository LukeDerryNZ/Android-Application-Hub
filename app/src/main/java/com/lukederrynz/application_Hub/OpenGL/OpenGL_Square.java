package com.lukederrynz.application_Hub.OpenGL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Luke on 6/09/2017.
 */

public class OpenGL_Square {

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    // Number of coords per vertex
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {-0.5f,  0.5f, 0.0f,  // Top Left
                                   -0.5f, -0.5f, 0.0f,  // Bottom Left
                                    0.5f, -0.5f, 0.0f,  // Bottom Right
                                    0.5f,  0.5f, 0.0f}; // Top Right

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 };

    public OpenGL_Square() {

        // Init vertex byte buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // Init byte buffer for draw list ( 2 bytes per short)
        ByteBuffer dlBuffer = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlBuffer.order(ByteOrder.nativeOrder());
        drawListBuffer = dlBuffer.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
    }
}
