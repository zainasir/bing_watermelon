package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class circle {
    private final int size;
    private final Vector2 position;
    private Body body;
    private float defaultY;  // Default Y-coordinate
    private boolean shouldBeRemoved = false;
    private Vector2 mergePosition = null;
    private int mergeSize = 0;

    public circle(World world, Vector2 position, int size)
    {
        this.position = position;
        this.size = size;
        this.defaultY = position.y; // Store the default Y position


        //create physics body:
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        this.body = world.createBody(bodyDef);

        //circle shape:
        CircleShape shape = new CircleShape();
        shape.setRadius(size / 2.0f); //radius of circle

        //Fixture Properties:
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.4f; //rolling, adjust if needed
        fixtureDef.restitution = 0.1f; //bounciness, adjust if needed

        body.createFixture(fixtureDef);
        shape.dispose();

        this.body.setUserData(this);
    }

    public void markForRemoval() {
        shouldBeRemoved = true;
    }

    public boolean shouldBeRemoved() {
        return shouldBeRemoved;
    }

    public void setMergeInfo(Vector2 position, int newSize) {
        mergePosition = position;
        mergeSize = newSize;
    }

    public Vector2 getMergePosition() {
        return mergePosition;
    }

    public int getMergeSize() {
        return mergeSize;
    }

    public int getSize() {
        return size;
    }

    public Body getBody() {
        return body;
    }

    public void setPosition(float x) {
        float constantY = this.body.getPosition().y; // Maintain the current y position
        this.body.setTransform(new Vector2(x, constantY), this.body.getAngle());
    }

    public void draw(ShapeRenderer shapeRenderer)
    {
        Vector2 position = body.getPosition();
        shapeRenderer.setColor(1, 1, 1, 1); // Set color to white
        shapeRenderer.circle(position.x, position.y, size / 2);
    }
}
