package joglmaze;

import com.jogamp.opengl.util.texture.Texture;

import javax.media.opengl.GL2;

/**
 * Cell, class representing a cell in the maze
 * You need to add code to this class!
 *
 * @author You
 */
public class Cell {

    //Define the sets of coordinates that will make up the walls, floors and ceiling
    private float[][] wall1 = {{-0.5f, 0.5f, 0.5f},
            {-0.5f, -0.5f, 0.5f},
            {-0.5f, -0.5f, -0.5f},
            {-0.5f, 0.5f, -0.5f}};

    private float[][] wall2 = {{0.5f, 0.5f, 0.5f},
            {0.5f, 0.5f, -0.5f},
            {0.5f, -0.5f, -0.5f},
            {0.5f, -0.5f, 0.5f}};

    private float[][] wall3 = {{0.5f, 0.5f, -0.5f},
            {-0.5f, 0.5f, -0.5f},
            {-0.5f, -0.5f, -0.5f},
            {0.5f, -0.5f, -0.5f}};

    private float[][] wall4 = {{0.5f, 0.5f, 0.5f},
            {0.5f, -0.5f, 0.5f},
            {-0.5f, -0.5f, 0.5f},
            {-0.5f, 0.5f, 0.5f}};

    private float[][] floor = {{0.5f, -0.5f, 0.5f},
            {0.5f, -0.5f, -0.5f},
            {-0.5f, -0.5f, -0.5f},
            {-0.5f, -0.5f, 0.5f}};

    private float[][] ceiling = {{0.5f, 0.5f, 0.5f},
            {-0.5f, 0.5f, 0.5f},
            {-0.5f, 0.5f, -0.5f},
            {0.5f, 0.5f, -0.5f}};


    //The list of booleans that determine the existence of the walls
    boolean[] walls;
    //The X and Y index of the maze cell
    private float i;
    private float j;
    //The maze object containing the start/end properties of the maze
    private MazeEndPoint obj;

    //The drawlist variable
    private int rectList = -1;

    public Cell(boolean[] walls, float i, float j) {
        // This constructor gets passed three parameters
        // The first is a list of booleans listing whether
        // the cell has walls. The order of the walls is
        // +x, -x, +z, -z . So, if your cell is 1x1x1,
        // and the middle of your cell is at
        // (0.5, 0.5, 0.5) the +x wall is the wall
        // whose centre is at (1.0, 0.5, 0.5) and the 
        // -x wall is the wall whose centre is at
        // (0.0, 0.5, 0.5)
        // The i and j values are the cell index in the maze
        this.walls = walls;
        this.i = i;
        this.j = j;

        // Take the rectangle render list for drawing walls etc...
        this.rectList = ViewRenderer.rectList;
    }

    public void addItem(MazeEndPoint obj) {
        //Add the maze object to the list of variables
        this.obj = obj;

        //If it's the start cell, set the initial player position
        if (this.obj.isStart()) ViewRenderer.setPos(i, j);
    }

    public void draw(Texture[] textures, GL2 gl) {

        gl.glPushMatrix();


        //Move the current cell of the maze according to its relative position
        gl.glTranslatef(i, 0.0f, j);

        //Set up the lighting point position
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[]{-0.3f, 0.4f, 0.0f, 1.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, new float[]{0.3f, 0.4f, 0.0f, 1.0f}, 0);
        //gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPOT_DIRECTION, new float [] { 0.0f, -0.5f, 1.0f, 0.0f}, 0);

        //Enable and bind the textures to be used for mapping onto the faces
        gl.glActiveTexture(GL2.GL_TEXTURE0);
        textures[0].enable(gl);
        textures[0].bind(gl);

        gl.glColor3f(1.0f, 1.0f, 1.0f);

        //If a wall of the current maze cell exists, draw it. Repeat for all 4 walls.
        if (walls[0]) {
            gl.glNormal3f(1.0f, 0.0f, 0.0f);
            gl.glCallList(rectList);

        }
        if (walls[1]) {
            gl.glPushMatrix();
            gl.glRotatef(180, 0.0f, 1.0f, 0.0f);
            gl.glNormal3f(1.0f, 0.0f, 0.0f);
            gl.glCallList(rectList);
            gl.glPopMatrix();
        }
        if (walls[2]) {
            gl.glPushMatrix();
            gl.glRotatef(270, 0.0f, 1.0f, 0.0f);
            gl.glNormal3f(1.0f, 0.0f, 0.0f);
            gl.glCallList(rectList);
            gl.glPopMatrix();
        }
        if (walls[3]) {
            gl.glPushMatrix();
            gl.glRotatef(90, 0.0f, 1.0f, 0.0f);
            gl.glNormal3f(1.0f, 0.0f, 0.0f);
            gl.glCallList(rectList);
            gl.glPopMatrix();
        }

        //Change the textures to be used for next mapping
        textures[0].disable(gl);
        textures[1].enable(gl);
        textures[1].bind(gl);

        //The floor
        gl.glBegin(GL2.GL_POLYGON);
        //setNormal(floor[0], floor[1], floor[2], gl);
        gl.glMultiTexCoord2f(GL2.GL_TEXTURE0, 0, 0);
        gl.glVertex3fv(floor[0], 0);
        gl.glMultiTexCoord2f(GL2.GL_TEXTURE0, 0, 1);
        gl.glVertex3fv(floor[1], 0);
        gl.glMultiTexCoord2f(GL2.GL_TEXTURE0, 1, 1);
        gl.glVertex3fv(floor[2], 0);
        gl.glMultiTexCoord2f(GL2.GL_TEXTURE0, 1, 0);
        gl.glVertex3fv(floor[3], 0);
        gl.glEnd();

        //Change the textures to be used for next mapping
        textures[1].disable(gl);
        textures[2].enable(gl);
        textures[2].bind(gl);

        //The ceiling
        gl.glPushMatrix();
        gl.glRotatef(90, 0.0f, 0.0f, -1.0f);
        gl.glNormal3f(1.0f, 0.0f, 0.0f);
        gl.glCallList(rectList);
        gl.glPopMatrix();

        textures[2].disable(gl);

        //If the maze's end-point object exists, invoke its draw method
        if (obj != null) obj.draw(gl);

        gl.glPopMatrix();


    }

    /**
     * Calculates a normalized vector of a surface, and sends the
     * normalization command to the openGL context
     *
     * @param a  First point of the triangle which surface normal to calculate
     * @param b  Second point of the triangle which surface normal to calculate
     * @param c  Third point of the triangle which surface normal to calculate
     * @param gl The openGL context
     */
    public void setNormal(float[] a, float[] b, float[] c, GL2 gl) {

        //Create the vectors to apply the cross product on
        float v1[] = new float[3];
        float v2[] = new float[3];
        float vC[] = new float[3];
        float vN[] = new float[3];

        //Calculate the vectors by substracting one set of coordinates from another
        v1[0] = b[0] - a[0];
        v1[1] = b[1] - a[1];
        v1[2] = b[2] - a[2];

        v2[0] = c[0] - a[0];
        v2[1] = c[1] - a[1];
        v2[2] = c[2] - a[2];

        //Calculate the cross product between the 2 vectors
        vC[0] = (v1[1] * v2[2]) - (v1[2] * v2[1]);
        vC[1] = (v1[2] * v2[0]) - (v1[0] * v2[2]);
        vC[2] = (v1[0] * v2[1]) - (v1[1] * v2[0]);

        //Turn the resulting vector into a unit-vector
        float magnitude = (float) Math.sqrt((vC[0] * vC[0]) + (vC[1] * vC[1]) + (vC[2] * vC[2]));
        vN[0] = vC[0] / magnitude;
        vN[1] = vC[1] / magnitude;
        vN[2] = vC[2] / magnitude;
        //Send the normalization command with the vector to the GL context
        gl.glNormal3fv(vN, 0);
    }
}
