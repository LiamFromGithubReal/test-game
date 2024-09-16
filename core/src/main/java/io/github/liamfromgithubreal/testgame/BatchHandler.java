package io.github.liamfromgithubreal.testgame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

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

    public SpriteBatch getBatch() {
        return batch;
    }
}
