package io.github.liamfromgithubreal.testgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class BatchHandler {


    private SpriteBatch batch;

    public BatchHandler() {
        batch = new SpriteBatch();
    }


    public void draw(Player player, Array<Rectangle> raindrops, Texture dropImage, OrthographicCamera camera) {

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(player.getSprite(), player.getHitbox().x, player.getHitbox().y);
        for(Rectangle raindrop: raindrops) {
            batch.draw(dropImage, raindrop.x, raindrop.y);
        }
        batch.end();
    }

    public long spawnRaindrop(Array<Rectangle> raindrops) {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800-64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        return TimeUtils.nanoTime();
    }

    public void rainLoop(Array<Rectangle> raindrops, Player player, Sound[] dropSounds) {
        for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.y + 64 < 0) {
                iter.remove();
            }
            if (raindrop.overlaps(player.getHitbox())) {
                // when player jump speed is less than 0 it means theyre falling
                if (player.getJumpSpeed() < 0) {
                    player.setJumpSpeed(1500);
                }
                // this will be used to index the dropSound array for one of 3 possible sounds, so upper bound is 3
                // (from 0 up to but NOT including 3)
                int soundIndex = Main.rand.nextInt(3);
                // play drop sound at random index
                dropSounds[soundIndex].play();
                // remove rain drop as it collided with bucket
                iter.remove();
            }
        }
    }





    public SpriteBatch getBatch() {
        return batch;
    }
}
