package com.egormor.obey.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.egormor.obey.Sprites.Enemy;
import com.egormor.obey.Sprites.InteractiveTileObject;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        //  Detects collision with four sides of MainCharacter with InteractiveTileObject / Enemy and calls their onHandHit()
        //  for Enemy also onHeadBottomHit()
        if ("right_hand".equals(fixA.getUserData()) || "right_hand".equals(fixB.getUserData()) || "left_hand".equals(fixA.getUserData()) || "left_hand".equals(fixB.getUserData())){
            Fixture hand, object;
            if ("right_hand".equals(fixA.getUserData()) || "left_hand".equals(fixA.getUserData())){
                hand = fixA;
                object = fixB;
            }
            else{
                hand = fixB;
                object = fixA;
            }

            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject) object.getUserData()).onHandHit();
            }
            if (object.getUserData() != null && Enemy.class.isAssignableFrom(object.getUserData().getClass())){
                ((Enemy) object.getUserData()).onHandHit();
            }
        }

        if ("head".equals(fixA.getUserData()) || "head".equals(fixB.getUserData()) || "bottom".equals(fixA.getUserData()) || "bottom".equals(fixB.getUserData())){
            Gdx.app.log("Head/Bottom", "collision");
            Fixture hand, object;
            if ("head".equals(fixA.getUserData()) || "bottom".equals(fixA.getUserData())){
                hand = fixA;
                object = fixB;
            }
            else{
                hand = fixB;
                object = fixA;
            }

            if (object.getUserData() != null && Enemy.class.isAssignableFrom(object.getUserData().getClass())){
                ((Enemy) object.getUserData()).onHeadBottomHit();
            }
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
