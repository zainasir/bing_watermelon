package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.mygdx.game.WatermelonGame;
import com.mygdx.game.screens.GameScreen;

public class StartScreen implements Screen{
    private final WatermelonGame game;
    private Skin skin;
    private Stage stage;
    private final Sprite bgSprite;
    private final Sprite titleSprite;
    private final Texture texture;
    private final Texture titleTexture;
    private final SpriteBatch batch;
    private final float bgTextureHeight;
    private final float bgTextureWidth;
    private final float titleTextureHeight;
    private final float titleTextureWidth;
    Vector2 position;


    public StartScreen(WatermelonGame game){
        this.game = game;
        stage = new Stage(new ScreenViewport());
        this.position = new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);

        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("ScreenBG/startscreen.png"));
        bgSprite = new Sprite(texture);
        bgSprite.setScale(1.7f);
        bgTextureHeight = bgSprite.getHeight()/2f;
        bgTextureWidth = bgSprite.getWidth()/2f;

        titleTexture = new Texture(Gdx.files.internal("miscellaneous/GameTitle.png"));
        titleSprite = new Sprite(titleTexture);
        titleSprite.setScale(1.45f);
        titleTextureHeight = titleSprite.getHeight()/2f;
        titleTextureWidth = titleSprite.getWidth()/2f;

        Drawable testButtonNormal = new TextureRegionDrawable(new TextureRegion(new Texture("miscellaneous/play_button.png")));
        ImageButton testButton = new ImageButton(testButtonNormal, testButtonNormal);
        testButton.setPosition(Gdx.graphics.getWidth() / 2f - testButton.getWidth() / 2, Gdx.graphics.getHeight() / 4f - testButton.getHeight() / 2);
        testButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Game Started!!!");
                game.setScreen(new GameScreen(game));
            }
        });
        stage.addActor(testButton);
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        bgSprite.setPosition(position.x - bgTextureWidth, position.y -  bgTextureHeight);
        titleSprite.setPosition(position.x - titleTextureWidth, 340+position.y +  2*titleTextureHeight);
        batch.begin();
        bgSprite.draw(batch);
        titleSprite.draw(batch);
        batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        texture.dispose();
        titleTexture.dispose();
        batch.dispose();
    }

}

