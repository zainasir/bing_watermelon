package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.WatermelonGame;
import com.mygdx.game.screens.StartScreen;

public class FlashScreen implements Screen {
    private final WatermelonGame game;
    private final SpriteBatch batch;
    private final Sprite logoSprite;
    private final Texture texture;
    Vector2 position;
    private float timeSinceLastSpawn = 0f;
    private final float spawnInterval = 4f;
    private final float logoTextureHeight;
    private final float logoTextureWidth;

    public FlashScreen(WatermelonGame game){
        this.game = game;
        batch = new SpriteBatch();

        this.position = new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        texture = new Texture(Gdx.files.internal("miscellaneous/B-logo.png"));
        logoSprite = new Sprite(texture);
        logoSprite.setScale(0.3f);
        logoTextureHeight = logoSprite.getHeight()/2f;
        logoTextureWidth = logoSprite.getWidth()/2f;
        // After a certain time, game.setScreen(new StartScreen(game));

    }
    @Override
    public void show() {

    }
    @Override
    public void render(float delta) {
        float red = 0 / 255f;
        float green = 93 / 255f;
        float blue = 64 / 255f;
        float alpha = 1;

        Gdx.gl.glClearColor(red, green, blue, alpha);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        timeSinceLastSpawn += delta;
        System.out.println(delta);
        if (timeSinceLastSpawn >= spawnInterval) {
            System.out.println("End Flashscreen");
//            this.dispose();
            game.setScreen(new StartScreen(game));
            dispose();
        }
        else{
            logoSprite.setPosition(position.x - logoTextureWidth, position.y -  logoTextureHeight);
            batch.begin();
            logoSprite.draw(batch);
            batch.end();
        }
    }
    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
    @Override
    public void dispose() {
        logoSprite.getTexture().dispose();
        texture.dispose();
        batch.dispose();
    }

}
