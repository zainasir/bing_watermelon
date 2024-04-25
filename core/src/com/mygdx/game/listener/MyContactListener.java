package com.mygdx.game.listener;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.objects.circle;

public class MyContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // Extract the circle objects from the user data of the body
        circle circleA = (circle) fixtureA.getBody().getUserData();
        circle circleB = (circle) fixtureB.getBody().getUserData();

        if (circleA != null && circleB != null) {
            handleCollision(circleA, circleB);
        }
    }

    private void handleCollision(circle circleA, circle circleB)
    {
        //TODO:Adjust this
        if (Math.abs(circleA.getSize() - circleB.getSize()) <= 10) {
            circleA.markForRemoval();
            circleB.markForRemoval();
            Vector2 newPosition = circleA.getBody().getPosition();
            int newSize = circleA.getSize() + circleB.getSize();
            circleA.setMergeInfo(newPosition, newSize);
        }
    }

    @Override
    public void endContact(Contact contact) {
        // Implement logic for when contact ends, if needed
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // Implement pre-solve contact conditions, if needed
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // Implement post-solve contact conditions, if needed
    }
}
