package com.egormor.obey.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.egormor.obey.OBEY;
import com.egormor.obey.Screens.PlayScreen;

public class RobotEnemy extends Enemy{
    public enum State { FALLING, JUMPING, STANDING, RUNNING};
    private float stateTime;
    public State previousState;
    public State currentState;

    private TextureRegion CharacterStand;
    private Animation<TextureRegion> walkAnimation;
    private BodyDef bdef;
    private FixtureDef fdef;
    private Fixture fixture;

    private Array<TextureRegion> frames;

    public boolean runningRight, fallingDown;
    private float x_pos, y_pos;

    private boolean isVisible = true;

    public RobotEnemy(PlayScreen screen, float x, float y, float x_pos, float y_pos, int index) {
        super(screen, x, y);

        this.x_pos = x_pos;
        this.y_pos = y_pos;

        currentState = State.STANDING;
        previousState = State.STANDING;
        runningRight = true;
        fallingDown = true;
        stateTime = 0;

        frames = new Array<TextureRegion>();
        for (int i = 0;i<7;i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("robotEnemy_sprite"), 2 + i * 28, 2, 24, 86));  //robotEnemy_sprite
        walkAnimation = new Animation<TextureRegion>(0.05f, frames);
        setBounds(0, 0, 24 / OBEY.PPM, 86 / OBEY.PPM);
        frames.clear();

        defineEnemy();
        CharacterStand = new TextureRegion(screen.getAtlas().findRegion("robotEnemy_sprite"), 2, 2, 24, 86);
        setRegion(CharacterStand);
    }

    public void update(float dt){
        if (!isVisible)
            return;
        stateTime += dt;
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if ((screen.getPlayerX() > b2body.getPosition().x) && (b2body.getLinearVelocity().x <= 5)){
            b2body.applyLinearImpulse(new Vector2(5 / OBEY.PPM, 0), b2body.getWorldCenter(), true);
        }
        else if ((screen.getPlayerX() < b2body.getPosition().x) && (b2body.getLinearVelocity().x >= -5)){
            b2body.applyLinearImpulse(new Vector2(-5 / OBEY.PPM, 0), b2body.getWorldCenter(), true);
        }
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            //  Uncomment when jump animation added
            /*case JUMPING:
            region = mainCharacterJump.getKeyFrame(stateTimer);
            break;*/
            case RUNNING:
                region = walkAnimation.getKeyFrame(stateTime, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = CharacterStand;
                break;
        }
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

        stateTime = currentState == previousState ? stateTime + dt : 0;
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

    @Override
    protected void defineEnemy() {
        bdef = new BodyDef();
        bdef.position.set(x_pos, y_pos);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.set(new Vector2[]{new Vector2(-12 / OBEY.PPM, 43 / OBEY.PPM),
                new Vector2(12 / OBEY.PPM, 43 / OBEY.PPM),
                new Vector2(12 / OBEY.PPM, -43 / OBEY.PPM),
                new Vector2(-12 / OBEY.PPM, -43 / OBEY.PPM)});

        fdef.filter.categoryBits = OBEY.ENEMY_BIT;
        fdef.filter.maskBits = OBEY.GROUND_BIT | OBEY.BRICK_BIT | OBEY.LASER_BIT | OBEY.ENEMY_BIT | OBEY.OBJECT_BIT | OBEY.MAIN_CHARACTER_BIT;

        fdef.shape = shape;

        fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);
    }


    @Override
    public void onHandHit() {
        OBEY.manager.get(OBEY.SOUND_ROBOT_ENEMY_KILL_PATH, Sound.class).play();
        Gdx.app.log("Enemy", "Collision");
    }

    @Override
    public void onHeadBottomHit() {
        OBEY.manager.get(OBEY.SOUND_ROBOT_ENEMY_KILL_PATH, Sound.class).play();
        Gdx.app.log("Enemy", "Collision");
        screen.game_over = true;
    }

}
