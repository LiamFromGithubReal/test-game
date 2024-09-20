package io.github.liamfromgithubreal.testgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    // player has hitbox (for collision) and sprite (for texture)
    Rectangle hitbox;
    Texture sprite;
    // player run speed means how fast they can run
    private int runSpeed = 600;
    // player jump speed is their current jumping speed, so while they are not jumping it is always 0,
    // then when they jump its increased, then gravitySpeed is used to lower it back down to 0, making
    // player fall back down to the ground
    private int jumpSpeed = 0;
    // player gravity speed which can be adjusted independently from global gravity speed
    private int gravitySpeed = -100;

    // Player class constructer generates the sprite (using path) and hitbox, along with the size of the player
    public Player() {
        sprite = new Texture(Gdx.files.internal("sprites/bucket/bucket.png"));
        hitbox = new Rectangle();
        hitbox.x = 800 / 2 - 64 / 2;
        hitbox.y = 20;
        hitbox.width = 64;
        hitbox.height = 64;
    }

    // this is more specific, you can fully customise player position and size
    // BUT obviously you need to ensure the hitbox matches texture
    //      PLANS: add a way to resize player mathematically to an integer multiple of itself,
    //              BUT also add method to restore to default size (because resizing player by
    //              double values such as 0.5 will result in rounding a value, losing accuracy)
    public Player(int x, int y, int width, int height) {
        sprite = new Texture(Gdx.files.internal("sprites/bucket/bucket.png"));
        hitbox = new Rectangle();
        hitbox.x = x;
        hitbox.y = y;
        hitbox.width = width;
        hitbox.height = height;
    }

    // calculates player movement
    public void move() {
        // move left
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            hitbox.x -= runSpeed * Gdx.graphics.getDeltaTime();
        }
        // move right
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            hitbox.x += runSpeed * Gdx.graphics.getDeltaTime();
        }
        // check if out of bounds
        if(hitbox.x < 0) hitbox.x = 0;
        if(hitbox.x > 800 - 64) hitbox.x = 800 - 64;
        // if up input pressed AND player is on floor (y-level = 10 ...for now)
        if((Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) && (hitbox.y == 10)) {
            // jump by setting jumpSpeed to be a high positive int
            jumpSpeed = 1500;
        }
        // move player object up
        hitbox.y += (jumpSpeed + gravitySpeed) * Gdx.graphics.getDeltaTime();
        // add gravity (to start acting as a force against the jump)
        jumpSpeed += gravitySpeed;
        // if player falls below floor (y-level = 10 ...for now)
        if (hitbox.y < 10) {
            // reset them back to the correct y-level and reset jump speed to 0
            hitbox.y = 10;
            jumpSpeed = 0;
        }
    }

    // getter methods for player attributes
    public int getRunSpeed() {
        return runSpeed;
    }
    public int getJumpSpeed() {
        return jumpSpeed;
    }
    public Rectangle getHitbox() {
        return hitbox;
    }
    public Texture getSprite() {
        return sprite;
    }

    // setter methods for player attributes
    public void setJumpSpeed(int jumpSpeed) {
        this.jumpSpeed = jumpSpeed;
    }
}
