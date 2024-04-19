package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class circle {
    private final Texture texture;
    private final Sprite sprite;
    private final int size;
    private final Vector2 position;

    public circle(Vector2 position, int size){
        this.position = position;
        this.size = size;
        texture = new Texture(Gdx.files.internal("badlogic.jpg"));
        sprite = new Sprite(texture);
        sprite.setScale(0.22f);
    }
}
