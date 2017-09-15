package com.lukederrynz.application_Hub.OpenGL;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

/**
 * Created by Luke Derry 6/09/2017.
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 *
 */
class MyGLRenderer implements GLSurfaceView.Renderer {

    // Primitive instances
    public OpenGL_Triangle triangle;
    public OpenGL_Square square;

    // Matrices
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    //private final float[] mRotationMatrix = new float[16];

    // Angle modified via MyGLSurfaceView.onTouchEvent
    //private volatile float mAngle;

    public static float gravity = -0.1f;

    // View
    private static int OpenGL_Width = 0;
    private static int OpenGL_Height = 0;
    private static float depthZ = 0.1f; // Our Z Axis value for all vertices

    // Lines
    public static List<OpenGL_Line> Lines;
    OpenGL_Line newLine;

    // Balls
    public static List<Ball> Balls;
    public static Vector3f BallEmitter = new Vector3f(100f, 1000f, depthZ);
    public static int EmitterRateMillis = 2000;
    public static float BallSize = 16.0f;
    public Timer timer;


    /**
     * Called once upon surface creation.
     *
     * @param unused - GL10 : Unused
     * @param config - EGLConfig : Config data
     */
    @Override public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.2f, 0.2f, 0.4f, 1.0f);

        // Instantiate scene primitives
        //triangle = new OpenGL_Triangle();
        //square = new OpenGL_Square( 100f, 1000f, 8f);

        // Instantiate our lines
        Lines = new ArrayList<>();
        newLine = new OpenGL_Line();
        newLine.setColor(1f, 0f, 0f, 1f);

        // Instantiate Balls
        Balls = new ArrayList<>();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Balls.add(new Ball());
            }
        }, 0, EmitterRateMillis);
    }


    /**
     * Called when the renderer is ready to process a frame.
     *
     * @param unused - GL10 : Unused
     */
    @Override public void onDrawFrame(GL10 unused) {

        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        processBalls();
        processLines();
    }


    /**
     * Process physics for all Balls and Draw.
     * TODO: Segregate this method into 3 distinct methods:
     *       Physics / Collisions / Drawing
     */
    private void processBalls() {

        List<Integer> removalList = new ArrayList<>();

        for (int j=0; j<Lines.size(); j++) {

            for (int i=0; i<Balls.size(); i++) {

                // Remove if outside of screen bounds
                if (isOOB(Balls.get(i).getSquare().getPosition())) {
                    // Add index to removalList
                    removalList.add(i);
                    continue;
                }

                float posx = Balls.get(i).getSquare().getPosX();
                float posy = Balls.get(i).getSquare().getPosY();

                // Apply gravity
                Balls.get(i).addForceY(gravity);

                // Apply force
                Balls.get(i).tick();
                //Balls.get(i).getSquare().setPosX(posx + Balls.get(i).getForceX());
                //Balls.get(i).getSquare().setPosY(posy + Balls.get(i).getForceY());


                // Finally draw balls
                Balls.get(i).getSquare().draw(mMVPMatrix);
            }
        }

        // Remove Balls
        for (int k=0; k<removalList.size(); k++) {
            Balls.remove(removalList.get(k));
        }
    }


    /**
     * Process all Lines and Draw.
     *
     */
    private void processLines() {
        // Draw the new line currently being dragged
        newLine.draw(mMVPMatrix);

        for (OpenGL_Line l : Lines) {
            l.draw(mMVPMatrix);
        }
    }


    /**
     * Helper function, returns true if Vector3f is out of screen bounds.
     *
     * @return - boolean : true if OOB, else false
     */
    private boolean isOOB(Vector3f v) {
        return v.x < 0 || v.x > OpenGL_Width || v.y < 0 || v.y > OpenGL_Height;
    }

    /**
     * Called once upon surface changed - such as phone rotation.
     *
     * @param unused - GL10 : Unused
     * @param width - Int : denoting the width of the screen in pixels
     * @param height - Int : denoting the height of the screen in pixels
     */
    public void onSurfaceChanged(GL10 unused, int width, int height) {

        OpenGL_Width = width;
        OpenGL_Height = height;

        GLES20.glViewport(0, 0, width, height);

        // Projection matrix is applied to object coords in onDrawFrame()
        //Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

        Matrix.orthoM(mProjectionMatrix, 0, 0f, width, 0f, height, 0, 1);
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 1, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
    }


    /**
     * Utility method for compiling a OpenGL shader.
     *
     * @param type - int : Vertex or fragment shader type.
     * @param shaderCode - String : Contains the shader code.
     * @return - int : Shader ID
     */
    public static int loadShader(int type, String shaderCode) {

        // Create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // Add the source code and compile
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }


    /**
     * Utility method for debugging OpenGLES calls.
     * Provides the name of the call that caused the error
     * <pre>
     *     mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     *     MyGLRenderer.checkGLError("glGetUniformLocation");
     * </pre>
     * @param glOperation - String : Name of the OpenGLES call
     */
    public static void checkGLError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }


    /**
     * Set the Current Line as a new line on touch(x,y).
     *
     * @param x - float : X axis value
     * @param y - float : Y axis value
     */
    public void startNewLine(float x, float y) {
        newLine.setStartVerts(x, y);
        newLine.setEndVerts(x, y);
    }


    /**
     * Store a new line in our Lines list.
     *
     */
    public void storeNewLine() {
        OpenGL_Line line = new OpenGL_Line();
        line.setStartVerts(OpenGL_Line.lineCoords[0], OpenGL_Line.lineCoords[1]);
        line.setEndVerts(OpenGL_Line.lineCoords[3], OpenGL_Line.lineCoords[4]);
        Lines.add(line);
    }


    /**
     * Get the Z axis value(depth).
     *
     * @return - float : depth
     */
    public static float getDepth() {
        return depthZ;
    }

}