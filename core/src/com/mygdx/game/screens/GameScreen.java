package com.mygdx.game.screens;

import com.mygdx.game.listener.MyContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    private static final float TIME_STEP = 1 / 60f;
    private float accumulator = 0;

    private OrthographicCamera camera;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private ShapeRenderer shapeRenderer;
    private List<circle> circles;

    private circle nextCircle; // the next circle to be dropped

    public GameScreen(WatermelonGame game)
    {
        this.game = game;
        world = new World(new Vector2(0, -10), true); // Gravity directed downwards
        world.setContactListener(new MyContactListener()); // Set the custom contact listener
        camera = new OrthographicCamera();
        debugRenderer = new Box2DDebugRenderer();
        shapeRenderer = new ShapeRenderer();
        circles = new ArrayList<>();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 10f, Gdx.graphics.getHeight() / 10f); // Setup camera with a suitable viewport
        camera.update();
        createGround();  // Initialize the ground
        createWalls();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        accumulator += Math.min(delta, 0.25); // Cap delta

        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, 6, 2); // Use fixed time step
            accumulator -= TIME_STEP;
        }

        // Check for bodies to remove or merge
        List<circle> newCircles = new ArrayList<>();
        Iterator<circle> iterator = circles.iterator();
        while (iterator.hasNext()) {
            circle c = iterator.next();
            if (c.shouldBeRemoved()) {
                if (c.getMergePosition() != null && c.getMergeSize() > 0) {
                    newCircles.add(new circle(world, c.getMergePosition(), c.getMergeSize()));
                }
                world.destroyBody(c.getBody());
                iterator.remove();
            }
        }
        circles.addAll(newCircles);

        handleInput();

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        renderShapes();

        debugRenderer.render(world, camera.combined);
    }

    private void renderShapes() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (circle circle : circles) {
            circle.draw(shapeRenderer);
        }
        if (nextCircle != null) {
            nextCircle.draw(shapeRenderer); // Draw the next circle at its position
        }
        shapeRenderer.end();
    }

    private void handleInput()
    {
        if (Gdx.input.justTouched())
        {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos); // Convert screen coordinates to world coordinates

            // Create and drop the circle directly at the x position where the screen was touched
            float xPosition = touchPos.x;
            float yPosition = camera.viewportHeight - 40; // Adjust to position correctly at the top

            circle newCircle = new circle(world, new Vector2(xPosition, yPosition), 20);
            circles.add(newCircle); // Add the new circle immediately to the list
        }
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
        groundShape.setAsBox(camera.viewportWidth / 2, 1);

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
        leftWallShape.setAsBox(1, camera.viewportHeight / 2);

        leftWall.createFixture(leftWallShape, 0.0f);
        leftWallShape.dispose();

        // Create right wall
        BodyDef rightWallDef = new BodyDef();
        rightWallDef.position.set(camera.viewportWidth, camera.viewportHeight / 2);
        rightWallDef.type = BodyDef.BodyType.StaticBody;
        Body rightWall = world.createBody(rightWallDef);

        PolygonShape rightWallShape = new PolygonShape();
        rightWallShape.setAsBox(1, camera.viewportHeight / 2);

        rightWall.createFixture(rightWallShape, 0.0f);
        rightWallShape.dispose();
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
        world.dispose();
        debugRenderer.dispose();
        shapeRenderer.dispose();
    }
}
