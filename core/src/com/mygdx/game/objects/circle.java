package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class circle {
    private final int size;
    private Body body;
    private boolean shouldBeRemoved = false;
    private Vector2 mergePosition = null;
    private boolean merge = false;
    private final int[] sizeArr = {13, 23, 35, 43, 50, 59};
    private final float[] scaleArr = {0.37f, 0.7f, 0.9f, 1.1f, 1.3f, 1.5f};
    private static final String[] TEXTURE_PATHS = {
            "Balls/tomatoes.png",  // sizeIdx 0
            "Balls/pizza.png",     // sizeIdx 1
            "Balls/sandwich.png",  // sizeIdx 2
            "Balls/sushi.png",     // sizeIdx 3
            "Balls/bearcat.png",   // sizeIdx 4
            "Balls/harvey.png"     // sizeIdx 5
    };
    private int sizeIdx;
    private final Texture texture;
    private final Sprite sprite;
    private static float scale = 0.3f;
    private boolean touched;

    public circle(World world, Vector2 position, int sizeIdx)
    {

        this.sizeIdx = sizeIdx;
        this.size = sizeArr[sizeIdx];
        this.touched = false;
        //create physics body:
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.bullet = true;
        this.body = world.createBody(bodyDef);
        //circle shape:
        CircleShape shape = new CircleShape();
        shape.setRadius(size / 2.0f);
        //Fixture Properties:
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.05f;

        body.createFixture(fixtureDef);
        shape.dispose();

        this.body.setUserData(this);
        texture = new Texture(Gdx.files.internal(TEXTURE_PATHS[sizeIdx]));

        scale = scaleArr[sizeIdx];
        this.sprite = new Sprite(texture);
        this.sprite.setScale(scale);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

        updateSprite();
    }

    public void updateSprite() {
        Vector2 bodyPos = body.getPosition();
        sprite.setPosition(10*bodyPos.x-250, 10*bodyPos.y-250);
        if(sizeIdx == 5)
            sprite.setPosition(10*bodyPos.x-200, 10*bodyPos.y-204);

        sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
    }
    public float getHeight(){
        return body.getPosition().y;
    }
    public void markForRemoval() {
        shouldBeRemoved = true;
        this.dispose();
    }

    public boolean shouldBeRemoved() {
        return shouldBeRemoved;
    }
    public void setTouched(){
        touched = true;
    }
    public boolean getTouched(){
        return touched;
    }
    public void setMergeInfo(Vector2 position) {
        mergePosition = position;
        sizeIdx++;
        if(sizeIdx < sizeArr.length)
            merge = true;
    }

    public Vector2 getMergePosition() {
        return mergePosition;
    }

    public boolean isMerge() {
        return merge;
    }

    public int getSize() {
        return sizeIdx;
    }

    public Body getBody() {
        return body;
    }
    public void drawSprite(SpriteBatch batch){
        updateSprite();
        sprite.draw(batch);
    }
    public void dispose(){texture.dispose();}

}
