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
        circle circleA = (circle) fixtureA.getBody().getUserData();
        circle circleB = (circle) fixtureB.getBody().getUserData();

        if (circleA != null && circleB != null) {
            handleCollision(circleA, circleB);
        }
    }

    private void handleCollision(circle circleA, circle circleB)
    {
        circleA.setTouched();
        circleB.setTouched();
        if (circleA.getSize() == circleB.getSize()) {
            circleA.markForRemoval();
            circleB.markForRemoval();
            Vector2 newPosition = circleA.getBody().getPosition();
            circleA.setMergeInfo(newPosition);
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
