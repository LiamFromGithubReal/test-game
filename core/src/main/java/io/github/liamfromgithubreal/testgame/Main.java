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
    private SpriteBatch batch;
    private Texture image;


    private Texture dropImage;
    private Texture bucketImage;
    private Sound dropSound;
    private Sound[] dropSoundArray = new Sound[3];
    private Music rainMusic;
    private OrthographicCamera camera;
    private Rectangle bucket;
    private Array<Rectangle> raindrops;
    private long lastDropTime;

    // Instance of the random class
    Random rand = new Random();
    // index for drop sound in dropSoundArray
    private int soundIndex = 0;

    private int bucketSpeed = 600;


    @Override
    public void create() {
        // load the images for the droplet and the bucket, 64x64 pixels each
        dropImage = new Texture(Gdx.files.internal("sprites/drop/drop.png"));
        bucketImage = new Texture(Gdx.files.internal("sprites/bucket/bucket.png"));

        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("sounds/drop/drop_1.mp3"));

        String dropSoundPath = new String("sounds/drop/drop_#.mp3");
        for (int i = 0; i < 3; i++) {
            String dropSoundPathTemp = dropSoundPath.replace("#", Integer.toString(i + 1));
            Sound dropSound = Gdx.audio.newSound(Gdx.files.internal(dropSoundPathTemp));
            dropSoundArray[i] = dropSound;
        }


        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/ambience/rain.mp3"));

        // start the playback of the background music immediately
        rainMusic.setLooping(true);
        rainMusic.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);



        batch = new SpriteBatch();
        image = new Texture("libgdx.png");

        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2;
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 64;

        raindrops = new Array<Rectangle>();
        spawnRaindrop();


    }

    @Override
    public void render() {
        ScreenUtils.clear(0f, 0f, 0.2f, 1f);


        if(Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);


//            // no else because if its somehow same as mouse, do nothing
//            if (touchPos.x - 64 / 2 - Math.abs(bucket.x) < 20) {
//                // do nothing if mouse isnt moving and bucket has reached mouse range
//            }

            // this line below calculates the middle of the bucket relative to the mouse (using the width of the bucket)
            // IDEALLY, the width of the bucket would be a variable instead of hard-coded 64 below, will change in
            // future but as of right now will remain hard-coded
            float middleTouchPosX = touchPos.x - 64/2;
            if (middleTouchPosX > bucket.x) {
                bucket.x += bucketSpeed * Gdx.graphics.getDeltaTime();
                // this line (same in next statement) prevents the bucket from shaking if it has reached the mouse
                // because it would try to go where the mouse is, would overshoot, would come back, would overshoot and
                // this would loop, making it go right, left, right, left of the stationary mouse pointer
                if (bucket.x > middleTouchPosX) bucket.x = middleTouchPosX;
            } else if (middleTouchPosX < bucket.x) {
                bucket.x -= bucketSpeed * Gdx.graphics.getDeltaTime();
                if (bucket.x < middleTouchPosX) bucket.x = middleTouchPosX;
            }

//            // moves directly to mouse instead of a bit each frame
//            bucket.x = touchPos.x - 64 / 2;
        }
        // this is here to ensure BOTH keyboard and mouse are not pressed, otherwise can be exploited and both used at
        // same time to make the bucket go much faster
        else {
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= bucketSpeed * Gdx.graphics.getDeltaTime();
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += bucketSpeed * Gdx.graphics.getDeltaTime();
        }

        if(bucket.x < 0) bucket.x = 0;
        if(bucket.x > 800 - 64) bucket.x = 800 - 64;


        if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();
        for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if(raindrop.y + 64 < 0) iter.remove();
            if(raindrop.overlaps(bucket)) {
                // this will be used to index the dropSound array for one of 3 possible sounds, so upper bound is 3
                // (from 0 up to but NOT including 3)
                soundIndex = rand.nextInt(3);
                // play drop sound at random index
                dropSoundArray[soundIndex].play();
                // remove rain drop as it collided with bucket
                iter.remove();
            }

        }



        camera.update();


        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(bucketImage, bucket.x, bucket.y);
        for(Rectangle raindrop: raindrops) {
            batch.draw(dropImage, raindrop.x, raindrop.y);
        }
        batch.end();
    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800-64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }


    @Override
    public void dispose() {
        dropImage.dispose();
        bucketImage.dispose();
        for (Sound s : dropSoundArray) s.dispose();
        rainMusic.dispose();
        batch.dispose();
    }
}
