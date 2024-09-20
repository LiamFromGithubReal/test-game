package io.github.liamfromgithubreal.testgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;   // add this import and NOT the one in the standard library
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;
import java.util.Random;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private Texture image;


    private Texture dropImage;

    private Sound[] dropSounds = new Sound[3];
    private Music rainMusic;
    private OrthographicCamera camera;

    private Array<Rectangle> raindrops;

    // Instance of the random class
    static Random rand = new Random();


    long lastDropTime;

    // global gravity speed (need to look into whether needs to be static, but this would be the global default gravity)
    public static int gravitySpeed = -100;


    Player player;
    BatchHandler batchHandler;


    @Override
    public void create() {
        // load the images for the droplet and the bucket, 64x64 pixels each
        dropImage = new Texture(Gdx.files.internal("sprites/drop/drop.png"));

        // create player object
        player = new Player();

        String dropSoundPath = new String("sounds/drop/drop_#.mp3");
        for (int i = 0; i < 3; i++) {
            String dropSoundPathTemp = dropSoundPath.replace("#", Integer.toString(i + 1));
            Sound dropSound = Gdx.audio.newSound(Gdx.files.internal(dropSoundPathTemp));
            dropSounds[i] = dropSound;
        }


        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/ambience/rain.mp3"));

        // start the playback of the background music immediately
        rainMusic.setLooping(true);
        rainMusic.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);



        batchHandler = new BatchHandler();
        image = new Texture("libgdx.png");

        raindrops = new Array<Rectangle>();
        batchHandler.spawnRaindrop(raindrops);


    }

    @Override
    public void render() {
        ScreenUtils.clear(0f, 0f, 0.2f, 1f);


//        //  scrapped code, will probably not use mouse to move as development progresses
//        if(Gdx.input.isTouched()) {
//            Vector3 touchPos = new Vector3();
//            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
//            camera.unproject(touchPos);
//
//
////            // no else because if its somehow same as mouse, do nothing
////            if (touchPos.x - 64 / 2 - Math.abs(bucket.x) < 20) {
////                // do nothing if mouse isnt moving and bucket has reached mouse range
////            }
//
//            // this line below calculates the middle of the bucket relative to the mouse (using the width of the bucket)
//            // IDEALLY, the width of the bucket would be a variable instead of hard-coded 64 below, will change in
//            // future but as of right now will remain hard-coded
//            float middleTouchPosX = touchPos.x - 64/2;
//            if (middleTouchPosX > player.getHitbox().x) {
//                player.getHitbox().x += player.getRunSpeed() * Gdx.graphics.getDeltaTime();
//                // this line (same in next statement) prevents the bucket from shaking if it has reached the mouse
//                // because it would try to go where the mouse is, would overshoot, would come back, would overshoot and
//                // this would loop, making it go right, left, right, left of the stationary mouse pointer
//                if (player.getHitbox().x > middleTouchPosX) player.getHitbox().x = middleTouchPosX;
//            } else if (middleTouchPosX < player.getHitbox().x) {
//                player.getHitbox().x -= player.getRunSpeed() * Gdx.graphics.getDeltaTime();
//                if (player.getHitbox().x < middleTouchPosX) player.getHitbox().x = middleTouchPosX;
//            }
//
////            // moves directly to mouse instead of a bit each frame
////            bucket.x = touchPos.x - 64 / 2;
//        }
//        // this is here to ensure BOTH keyboard and mouse are not pressed, otherwise can be exploited and both used at
//        // same time to make the bucket go much faster
//        else {
//            // this is where the keys check INSTEAD of mouse movement used to be (they were in different if/else
//            // statements because otherwise it would do both calculations for mouse and keyboard and double-move)
//        }


        // calculates player movement
        player.move();





        // render rain if last drop was a while ago
        if(TimeUtils.nanoTime() - lastDropTime > 500000000) {
            lastDropTime = batchHandler.spawnRaindrop(raindrops);
        }
        // loop for all rain drops (checks for collisions with player)
        batchHandler.rainLoop(raindrops, player, dropSounds);

        // draw the batch (for now just rain, the player is need to account for collisions but this will change)
        batchHandler.draw(player, raindrops, dropImage, camera);

        // update camera to display
        camera.update();


    }

//    private void spawnRaindrop() {
//        Rectangle raindrop = new Rectangle();
//        raindrop.x = MathUtils.random(0, 800-64);
//        raindrop.y = 480;
//        raindrop.width = 64;
//        raindrop.height = 64;
//        raindrops.add(raindrop);
//        lastDropTime = TimeUtils.nanoTime();
//    }



    // this is to dispose objects that link to assets in the directory
    @Override
    public void dispose() {
        dropImage.dispose();
        player.getSprite().dispose();
        for (Sound s : dropSounds) s.dispose();
        rainMusic.dispose();
        batchHandler.getBatch().dispose();
    }
}
