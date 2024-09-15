package io.github.liamfromgithubreal.testgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Player {

    Rectangle hitbox;
    Texture sprite;


    private int runSpeed = 600;
    private int jumpSpeed = 0;

    public Player() {
        sprite = new Texture(Gdx.files.internal("sprites/bucket/bucket.png"));
        hitbox = new Rectangle();
        hitbox.x = 800 / 2 - 64 / 2;
        hitbox.y = 20;
        hitbox.width = 64;
        hitbox.height = 64;
    }


    public int getRunSpeed() {
        return runSpeed;
    }

    public int getJumpSpeed() {
        return jumpSpeed;
    }
    public void setJumpSpeed(int jumpSpeed) {
        this.jumpSpeed = jumpSpeed;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public Texture getSprite() {
        return sprite;
    }
}
