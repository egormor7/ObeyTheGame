package com.egormor.obey.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.egormor.obey.OBEY;
import com.egormor.obey.Screens.PlayScreen;

public class RobotEnemy extends Enemy{
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;

    public boolean runningRight, fallingDown;

    public RobotEnemy(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        runningRight = true;
        fallingDown = true;

        frames = new Array<TextureRegion>();
        for (int i = 0;i<7;i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("robotEnemy_sprite"), 2 + i * 28, 2, 24, 86));  //robotEnemy_sprite
        walkAnimation = new Animation<TextureRegion>(0.05f, frames);
        stateTime = 0;
        setBounds(0, 0, 24 / OBEY.PPM, 86 / OBEY.PPM);
    }

    public void update(float dt){
        stateTime += dt;
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(walkAnimation.getKeyFrame(stateTime, true));
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(1500 / OBEY.PPM, 830 / OBEY.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.set(new Vector2[]{new Vector2(-12 / OBEY.PPM, 43 / OBEY.PPM),
                new Vector2(12 / OBEY.PPM, 43 / OBEY.PPM),
                new Vector2(12 / OBEY.PPM, -43 / OBEY.PPM),
                new Vector2(-12 / OBEY.PPM, -43 / OBEY.PPM)});

        fdef.filter.categoryBits = OBEY.ENEMY_BIT;
        fdef.filter.maskBits = OBEY.GROUND_BIT | OBEY.BRICK_BIT | OBEY.LASER_BIT | OBEY.ENEMY_BIT | OBEY.OBJECT_BIT | OBEY.MAIN_CHARACTER_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }
}
