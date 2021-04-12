package com.egormor.obey.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.egormor.obey.OBEY;
import com.egormor.obey.Screens.PlayScreen;

public class MainCharacter extends Sprite {
    public World world;
    public Body b2body;
    private TextureRegion mainCharacterStand;

    public MainCharacter(World world, PlayScreen screen){
        super(screen.getAtlas().findRegion("sprites"));
        this.world = world;
        defineMainCharacter();
        mainCharacterStand = new TextureRegion(getTexture(), 12, 14, 44, 65);
        setBounds(0, 0, 44 / OBEY.PPM, 65 / OBEY.PPM);
        setRegion(mainCharacterStand);
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }

    public void defineMainCharacter(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(300 / OBEY.PPM, 230 / OBEY.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(18 / OBEY.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);

    }

}