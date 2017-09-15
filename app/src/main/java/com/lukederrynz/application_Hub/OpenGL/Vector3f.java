package com.lukederrynz.application_Hub.OpenGL;

/**
 * Created by Luke on 13/09/2017.
 * A Vector class consisting of three floats (x, y, z)
 * Provides some mathematical functions:
 * - getLength
 * - dot product
 * - normalize
 * - scaleVector
 * - minVecNew
 *
 */
class Vector3f {

    float x;
    float y;
    float z;

    Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Vector3f(Vector3f v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    /**
     * Returns the dot product of this vector.
     *
     * @param v - Vector3f : Input vector
     * @return - float : The dot product
     */
    float dot(Vector3f v) {
        return ( (x*v.x) + (y*v.y) + (z*v.z));
    }


    /**
     * Returns the length of this vector.
     *
     * @return - float : The length of this vector
     */
   float getLength() {
        return (float)Math.sqrt(x*x + y*y + z*z);
    }

    /**
     * Normalizes this vector.
     *
     */
    void normalize() {
        float norm = this.getLength();
        x /= norm;
        y /= norm;
        z /= norm;
    }


    /**
     * Returns the right normal of this vector.
     *
     * @return - Vector3f : The Right Normal
     */
    public Vector3f getRightNormal() {
        return new Vector3f(y, -x, MyGLRenderer.getDepth());
    }


    /**
     * Returns the left normal of this vector.
     *
     * @return - Vector3f : The Left Normal
     */
    public Vector3f getLeftNormal() {
        return new Vector3f(-y, x, MyGLRenderer.getDepth());
    }


    /**
     * Scales the Vector by the provided float param.
     * Note: Does not alter ther Z Axis value.
     *
     * @param s - float : The scalar value
     */
    void scaleVector(float s) {
        this.x *= s;
        this.y *= s;
        //this.z *= s; // NOTE: We do not need to scale the z axis value at all for this application
    }


    /**
     * Minus this vector by input vector.
     * Returns result as a new Vector3f.
     * NOTE: Does not alter the Z Axis value.
     *
     * @param v - Vector3f : The input vector to subtract from this vector
     * @return - Vector3f : The subtracted new vector
     */
    Vector3f minVecNew(Vector3f v) {
        return new Vector3f(this.x - v.x, this.y - v.y, MyGLRenderer.getDepth());
    }

}
