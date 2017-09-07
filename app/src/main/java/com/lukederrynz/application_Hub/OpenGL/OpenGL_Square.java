package com.lukederrynz.application_Hub.OpenGL;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Luke on 6/09/2017.
 */

public class OpenGL_Square {

    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            // The matrix must be included as a modifier of gl_Position.
            // NOTE: The uMVPMatrix must be first.
            "   gl_Position = uMVPMatrix * vPosition;" +
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "   gl_FragColor = vColor;" +
            "}";


    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    // Number of coords per vertex
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {-0.5f,  0.5f, 0.0f,  // Top Left
                                   -0.5f, -0.5f, 0.0f,  // Bottom Left
                                    0.5f, -0.5f, 0.0f,  // Bottom Right
                                    0.5f,  0.5f, 0.0f}; // Top Right

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 };

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    float color[] = {1.0f, 0.0f, 0.0f, 1.0f};

    /**
     * Sets up the drawing object data for use in OpenGLES context
     */
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

        // Prepare shaders and OpenGLES program
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        // Create an empty OpenGLES program
        mProgram = GLES20.glCreateProgram();
        // Attach vertex and fragment shader to program
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        // And link
        GLES20.glLinkProgram(mProgram);
    }

    /**
     * Encapsulates the OpenGLES instructions for a square
     * @param mvpMatrix - The Model View Project Matrix in which
     *                  to draw this shape.
     */
    public void draw(float[] mvpMatrix) {
        // Add program to OpenGLES environment
        GLES20.glUseProgram(mProgram);
        // Get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        // Enable handle to triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        // Prepare triangle coords data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        // Get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        // Set color for triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        // Get handle to square's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        // TODO: IMPLEMENT CHECK IF GL ERROR //MyGLRenderer.checkGLError("glGetUniformLocation");
        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        // TODO: IMPLEMENT CHECK IF GL ERROR //MyGLRenderer.checkGLError("glGetUniformLocation");

        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
