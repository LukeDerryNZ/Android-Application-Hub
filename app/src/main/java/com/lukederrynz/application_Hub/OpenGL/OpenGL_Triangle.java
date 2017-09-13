package com.lukederrynz.application_Hub.OpenGL;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Luke Derry on 6/09/2017.
 *
 *
 */
class OpenGL_Triangle {

    // Number of coordinates per vertex
    static final int COORDS_PER_VERTEX = 3;

    private FloatBuffer vertexBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mMVPMatrixHandle;
    private int mColorHandle;
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 Bytes per vertex
    float color[] = { 0.2f, 0.6f, 0.2f, 1.0f };

    // Coords in counter-clockwise order
    static float triangleCoords[] = {
          0.3f,  0.622008459f, MyGLRenderer.getDepth(),  // Top
         -0.5f, -0.311004243f, MyGLRenderer.getDepth(),  // Bottom Left
          0.5f, -0.311004243f, MyGLRenderer.getDepth()}; // Bottom Right

    private final String vertexShaderCode =
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +
        "void main() {" +
        // The matrix must be included as a modifier of gl_Position
        // NOTE: The uMVPMatrix must be first.
        "   gl_Position = uMVPMatrix * vPosition;" +
                "}";

    private final String fragmentShaderCode =
        "precision mediump float;" +
        "uniform vec4 vColor;" +
        "void main() {" +
        "   gl_FragColor = vColor;" +
        "}";

    /**
     *  Sets up the drawing object data for use in OpenGLES context
     */
    OpenGL_Triangle() {

        // Prepare shaders
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        // Create empty OpenGLES program
        mProgram = GLES20.glCreateProgram();
        // Attach vertex shader
        GLES20.glAttachShader(mProgram, vertexShader);
        // Attach fragment shader
        GLES20.glAttachShader(mProgram, fragmentShader);
        // Create OpenGLES program exe
        GLES20.glLinkProgram(mProgram);

        // Init vertex byte buffer for shape coords ( 4bytes per tri cord )
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        // Use native byte order
        bb.order(ByteOrder.nativeOrder());
        // Create a float buffer from bb
        vertexBuffer = bb.asFloatBuffer();
        // Add coords to float buffer
        vertexBuffer.put(triangleCoords);
        // Set buffer to read first coord
        vertexBuffer.position(0);
    }

    /** Draw method for triangle
     *
     * @param mvpMatrix - Calculated transformation matrix
     */
    void draw(float[] mvpMatrix) {

        // Add program to OpenGLES
        GLES20.glUseProgram(mProgram);

        // Get a handle to the shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        // Get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        // Enable a handle to the triangle's vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        // Get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Prepare the triangle's coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
        // Set color for drawing triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        // Pas projection and view transformation to shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }


}
