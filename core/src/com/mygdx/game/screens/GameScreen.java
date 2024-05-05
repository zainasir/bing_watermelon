package com.mygdx.game.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.listener.MyContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.WatermelonGame;
import com.mygdx.game.objects.circle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameScreen implements Screen
{
    private final WatermelonGame game;
    private static final float TIME_STEP = 1 / 30f;
    private float accumulator = 0;
    private OrthographicCamera camera;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private List<circle> circles;
    private circle nextCircle;
    private Stage UIstage;
    private Stage stage;
    private boolean isGameover;
    private boolean isPaused;
    private final SpriteBatch batch;
    private BitmapFont font;
    private int score;
    private int[] scoreArr = {20, 50, 80, 120, 200, 300, 400};
    private InputMultiplexer inputMultiplexer;
    private int curr;
    private int next;
    private Texture texture;
    private Texture nextTexture;
    private Sprite sprite;
    private Sprite nextSprite;
    private float lastDropTime;
    private static final String[] TEXTURE_PATHS = {
            "Balls/tomatoes.png",  // sizeIdx 0
            "Balls/pizza.png",     // sizeIdx 1
            "Balls/sandwich.png",  // sizeIdx 2
            "Balls/sushi.png",     // sizeIdx 3
            "Balls/bearcat.png",   // sizeIdx 4
            "Balls/harvey.png"     // sizeIdx 5
    };


    public GameScreen(WatermelonGame game)
    {
        lastDropTime = 1;
        this.game = game;
        isGameover = false;
        isPaused = false;
        world = new World(new Vector2(0, -150), true); // Gravity directed downwards
        world.setContactListener(new MyContactListener()); // Set the custom contact listener
        camera = new OrthographicCamera();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(6);
        score = 0;
        this.curr = MathUtils.random(0, 2);
        this.next = MathUtils.random(0, 2);
        this.texture = new Texture(Gdx.files.internal(TEXTURE_PATHS[curr]));
        this.sprite = new Sprite(texture);
        this.nextTexture = new Texture(Gdx.files.internal(TEXTURE_PATHS[next]));
        this.nextSprite = new Sprite(nextTexture);
        updateSpriteTexture();

        debugRenderer = new Box2DDebugRenderer();
        circles = new ArrayList<>();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 10f, Gdx.graphics.getHeight() / 10f); // Setup camera with a suitable viewport
        camera.update();
        createGround();
        createWalls();

        batch = new SpriteBatch();

        UIstage = new Stage(new ScreenViewport());
        stage = new Stage(new ScreenViewport());
        inputMultiplexer = new InputMultiplexer();

        initUi();
        Drawable pauseButtonNormal = new TextureRegionDrawable(new TextureRegion(new Texture("miscellaneous/pause.png")));
        ImageButton pauseButton = new ImageButton(pauseButtonNormal, pauseButtonNormal);
        float scaleFactor = 0.18f;
        pauseButton.setSize(pauseButton.getWidth() * scaleFactor, pauseButton.getHeight() * scaleFactor);
        pauseButton.setPosition(Gdx.graphics.getWidth() - pauseButton.getWidth(), Gdx.graphics.getHeight() - pauseButton.getHeight());
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Openning Menu!!!");
                openMenu();
                event.handle();
            }
        });
        UIstage.addActor(pauseButton);

    }
    private void updateSpriteTexture() {
        if(texture != null)
            texture.dispose();
        if(nextTexture != null)
            nextTexture.dispose();
        texture = new Texture(Gdx.files.internal(TEXTURE_PATHS[curr]));
        nextTexture = new Texture(Gdx.files.internal(TEXTURE_PATHS[next]));
        sprite.setTexture(texture);
        nextSprite.setTexture(nextTexture);
        sprite.setScale(0.4f); // Update scale if needed
        nextSprite.setScale(0.2f); // Update scale if needed
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2); // Re-center the origin
        nextSprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2); // Re-center the origin
    }

    private void initUi() {
        // Gameover popup
        Drawable ggButtonNormal = new TextureRegionDrawable(new TextureRegion(new Texture("miscellaneous/popup.png")));

        ImageButton ggButton = new ImageButton(ggButtonNormal, ggButtonNormal);
        ggButton.setSize(1000, 1000);

        ggButton.setPosition(Gdx.graphics.getWidth() / 2f - ggButton.getWidth() / 2, Gdx.graphics.getHeight() / 2f - ggButton.getHeight() / 2 + 100);
        ggButton.setName("8");
        stage.addActor(ggButton);
        ggButton.setVisible(false);
        float scaleFactor = 5.0f;


        Drawable restartButtonNormal = new TextureRegionDrawable(new TextureRegion(new Texture("miscellaneous/restart.png")));
        ImageButton restartButton = new ImageButton(restartButtonNormal, restartButtonNormal);
        restartButton.setSize(restartButton.getWidth() * scaleFactor, restartButton.getHeight() * scaleFactor);
        restartButton.setPosition(Gdx.graphics.getWidth() / 2 - restartButton.getWidth() / 2, Gdx.graphics.getHeight() / 2f - restartButton.getHeight() / 2);
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Restart Game");

                game.setScreen(new GameScreen(game));
            }
        });
        restartButton.setName("6");
        stage.addActor(restartButton);

        Drawable exitButtonNormal = new TextureRegionDrawable(new TextureRegion(new Texture("miscellaneous/exit.png")));
        ImageButton exitButton = new ImageButton(exitButtonNormal, exitButtonNormal);
        exitButton.setSize(exitButton.getWidth() * scaleFactor, exitButton.getHeight() * scaleFactor);
        exitButton.setPosition(Gdx.graphics.getWidth() / 2 - exitButton.getWidth() / 2, Gdx.graphics.getHeight() / 2f - exitButton.getHeight() / 2  - 250);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Exit Game");
                game.setScreen(new StartScreen(game));
                dispose();
            }
        });
        exitButton.setName("7");
        stage.addActor(exitButton);
        Drawable resumeButtonNormal = new TextureRegionDrawable(new TextureRegion(new Texture("miscellaneous/resume.png")));
        ImageButton resumeButton = new ImageButton(resumeButtonNormal, resumeButtonNormal);
        resumeButton.setSize(570, 570);
        resumeButton.setPosition(Gdx.graphics.getWidth() / 2 - resumeButton.getWidth() / 2, Gdx.graphics.getHeight() / 2f - resumeButton.getHeight() / 2 + 250);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Resume Game");
                resumeGame();
            }
        });
        resumeButton.setName("5");
        stage.addActor(resumeButton);
        restartButton.setVisible(false);
        resumeButton.setVisible(false);
        exitButton.setVisible(false);
    }

    private void openGameover(){
        Gdx.input.setInputProcessor(stage);
        for (Actor actor : stage.getActors()) { // Set Game over things
            int actorIndex = Integer.parseInt(actor.getName());
            if (actorIndex == 8 || actorIndex == 7) {
                actor.setVisible(true);
            } else {
                actor.setVisible(false);
            }
        }
    }
    private void openMenu(){
        System.out.println("Opened Menu");
        isPaused = true;
        Gdx.input.setInputProcessor(stage);
        for (Actor actor : stage.getActors()) { // Set Menu things
            int actorIndex = Integer.parseInt(actor.getName());
            if (actorIndex == 8) {
                actor.setVisible(false);
            } else {
                actor.setVisible(true);
            }
        }
    }
    private void resumeGame() {
        System.out.println("Resumed");
        isPaused = false;
        Gdx.input.setInputProcessor(UIstage);

        for (Actor actor : stage.getActors()) {
            actor.setVisible(false);
        }
    }

    @Override
    public void show() {Gdx.input.setInputProcessor(UIstage);}

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!isGameover && !isPaused) {
            accumulator += Math.min(delta, 0.25); // Cap delta

            while (accumulator >= TIME_STEP) {
                world.step(TIME_STEP, 6, 2);
                accumulator -= TIME_STEP;
            }

            handleInput(delta);
            // Check for bodies to remove or merge
            List<circle> newCircles = new ArrayList<>();
            Iterator<circle> iterator = circles.iterator();
            while (iterator.hasNext()) {
                circle c = iterator.next();
                if (c.shouldBeRemoved()) {
                    if (c.getMergePosition() != null && c.isMerge()) {
                        score += scoreArr[c.getSize()];
                        if(c.getSize() < scoreArr.length)
                            newCircles.add(new circle(world, c.getMergePosition(), c.getSize()));
                    }
                    world.destroyBody(c.getBody());
                    iterator.remove();
                }
            }
            circles.addAll(newCircles);
        }

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        renderShapes();
        UIstage.draw();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        checkGameOver();

        batch.begin();
        sprite.setPosition(5, Gdx.graphics.getHeight() - 450);
        sprite.draw(batch);
        nextSprite.setPosition(-120, Gdx.graphics.getHeight() - 490);
        nextSprite.draw(batch);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Score: " + score, 10, Gdx.graphics.getHeight() - 10);
        if(isGameover){
            // Save current score to leaderboard
            Preferences preferences = Gdx.app.getPreferences("High Score Storage");
            long prevHighScore = preferences.getLong("highScore", -1);

            if (score > prevHighScore) {
                preferences.putLong("highScore", score);
                preferences.flush();
            }

            openGameover();
            String scoreText = "Game Over\n"+ "Score: " + score + "\nHigh Score: " + prevHighScore;
            font.getData().setScale(5.0f);
            font.draw(batch, scoreText, Gdx.graphics.getWidth()/2-250, Gdx.graphics.getHeight()/2+150);
        }
        batch.end();
    }
    private void checkGameOver(){
        float maxHeight = 0.0f;
        for (circle c : circles){
            if(c.getTouched() && c.getHeight() > maxHeight)
                maxHeight = c.getHeight();
        }
        if(maxHeight >= 200.0f){
            isGameover = true;
        }
    }

    private void renderShapes() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        for (circle circle : circles) {
            circle.drawSprite(batch);
        }
        if (nextCircle != null) {
            nextCircle.drawSprite(batch);
        }
        batch.end();
        debugRenderer.render(world, camera.combined);

    }


    private void handleInput(float delta)
    {
        if (Gdx.input.justTouched() && (lastDropTime >= 1))
        {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if(touchPos.x < 90 || touchPos.y < 220) {
                float xPosition = touchPos.x;
                float yPosition = camera.viewportHeight - 40;
                circle newCircle = new circle(world, new Vector2(xPosition, yPosition), curr);
                this.curr = this.next;
                this.next = MathUtils.random(0, 2);
                updateSpriteTexture();
                circles.add(newCircle);
                score += scoreArr[newCircle.getSize()];
                lastDropTime = 0;
            }
            else{
                System.out.println("Opening Menu!!!");
                openMenu();
            }
        }
        lastDropTime += delta;
    }
    @Override
    public void resize(int width, int height)
    {
        camera.viewportWidth = width / 10f;
        camera.viewportHeight = height / 10f;
        camera.update();
    }

    private void createGround() {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(camera.viewportWidth / 2, 1);
        Body groundBody = world.createBody(groundBodyDef);

        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(camera.viewportWidth / 2, 10);

        groundBody.createFixture(groundShape, 0.0f);
        groundShape.dispose();
    }

    private void createWalls() {
        // Create left wall
        BodyDef leftWallDef = new BodyDef();
        leftWallDef.position.set(0, camera.viewportHeight / 2);
        leftWallDef.type = BodyDef.BodyType.StaticBody;
        Body leftWall = world.createBody(leftWallDef);

        PolygonShape leftWallShape = new PolygonShape();
        leftWallShape.setAsBox(10, camera.viewportHeight / 2);

        leftWall.createFixture(leftWallShape, 0.0f);
        leftWallShape.dispose();

        // Create right wall
        BodyDef rightWallDef = new BodyDef();
        rightWallDef.position.set(camera.viewportWidth, camera.viewportHeight / 2);
        rightWallDef.type = BodyDef.BodyType.StaticBody;
        Body rightWall = world.createBody(rightWallDef);

        PolygonShape rightWallShape = new PolygonShape();
        rightWallShape.setAsBox(10, camera.viewportHeight / 2);

        rightWall.createFixture(rightWallShape, 0.0f);
        rightWallShape.dispose();

        // Create bottom wall
        BodyDef bottomWallDef = new BodyDef();
        bottomWallDef.position.set(camera.viewportWidth / 2, 10); // Centered horizontally, 10 pixels above the bottom
        bottomWallDef.type = BodyDef.BodyType.StaticBody;
        Body bottomWall = world.createBody(bottomWallDef);

        PolygonShape bottomWallShape = new PolygonShape();
        bottomWallShape.setAsBox(camera.viewportWidth / 2, 5); // Half the width of the viewport and 5 pixels high

        bottomWall.createFixture(bottomWallShape, 0.0f);
        bottomWallShape.dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose()
    {
        for (circle c: circles)
            c.dispose();
        world.dispose();
        font.dispose();
        batch.dispose();
        debugRenderer.dispose();
    }
}
