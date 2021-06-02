package com.egormor.obey.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.egormor.obey.OBEY;
import com.egormor.obey.Screens.PlayScreen;

public class MainCharacter extends Sprite {
    public enum State { FALLING, JUMPING, STANDING, RUNNING};
    public State currentState;
    public State previousState;
    private float stateTimer;
    public boolean runningRight, fallingDown;

    public World world;
    public Body b2body;

    private TextureRegion mainCharacterStand;
    private Animation <TextureRegion> mainCharacterRun;
    private Animation <TextureRegion> mainCharacterJump;

    private float x_create_cord, y_create_cord;

    public MainCharacter(PlayScreen screen, float x_create_cord, float y_create_cord){
        super(screen.getAtlas().findRegion("sprites"));

        this.world = screen.getWorld();

        this.x_create_cord = x_create_cord;
        this.y_create_cord = y_create_cord;

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        fallingDown = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        //  Same for other movements
        for(int i = 0;i < 10; i++)
            frames.add(new TextureRegion(getTexture(), 12 + i * 46, 28, 45, 65));
        mainCharacterRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        defineMainCharacter();
        mainCharacterStand = new TextureRegion(getTexture(), 12, 28, 45, 65);
        setBounds(0, 0, 44 / OBEY.PPM, 65 / OBEY.PPM);
        setRegion(mainCharacterStand);
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            //  Uncomment when jump animation added
            /*case JUMPING:
                region = mainCharacterJump.getKeyFrame(stateTimer);
                break*/
            case RUNNING:
                region = mainCharacterRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = mainCharacterStand;
                break;
        }
        //  flips the region to looks right / left if character runs right / left
        //  flips the region to looks fall up / down if character falls up / down
        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        if ((!fallingDown) && !region.isFlipY()){
            region.flip(false, true);
        }
        else if ((fallingDown) && region.isFlipY()){
            region.flip(false, true);
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState(){
        if (b2body.getLinearVelocity().y > 0)
            return State.JUMPING;
        else if (b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public void defineMainCharacter(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(x_create_cord, y_create_cord);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.set(new Vector2[]{new Vector2(-22 / OBEY.PPM, 33 / OBEY.PPM),
                new Vector2(22 / OBEY.PPM, 33 / OBEY.PPM),
                new Vector2(22 / OBEY.PPM, -33 / OBEY.PPM),
                new Vector2(-22 / OBEY.PPM, -33 / OBEY.PPM)});

        //  assert MainCharacter "class" (OBEY.MAIN_CHARACTER_BIT) and "classes" with which collide
        fdef.filter.categoryBits = OBEY.MAIN_CHARACTER_BIT;
        fdef.filter.maskBits = OBEY.GROUND_BIT | OBEY.BRICK_BIT | OBEY.LASER_BIT | OBEY.OBJECT_BIT | OBEY.ENEMY_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);

        //  creating sensors which isn't collide to anything physically, but detect sensor collision
        //  for every four sides of the body. Collision detects in WorldContactListener.java
        EdgeShape right_hand = new EdgeShape();
        right_hand.set(new Vector2(25 / OBEY.PPM, 25 / OBEY.PPM), new Vector2(25 / OBEY.PPM, -25 / OBEY.PPM));
        fdef.shape = right_hand;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("right_hand");

        EdgeShape left_hand = new EdgeShape();
        left_hand.set(new Vector2(-25 / OBEY.PPM, 25 / OBEY.PPM), new Vector2(-25 / OBEY.PPM, -25 / OBEY.PPM));
        fdef.shape = left_hand;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("left_hand");

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-25 / OBEY.PPM, 35 / OBEY.PPM), new Vector2(25 / OBEY.PPM, 35 / OBEY.PPM));
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("head");

        EdgeShape bottom = new EdgeShape();
        bottom.set(new Vector2(-25 / OBEY.PPM, -35 / OBEY.PPM), new Vector2(25 / OBEY.PPM, -35 / OBEY.PPM));
        fdef.shape = bottom;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("bottom");

    }

}