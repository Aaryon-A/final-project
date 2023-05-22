package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class BackgroundTemp extends ApplicationAdapter {
    private MainChar mainChar;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private int flipVal = 1;
    static int groundLevel = 86; 

    private Boss boss;
    private Items items;

    private Texture background1;
	private Texture background2;
    private float backgroundX = 0;
	private float screenHeight;
	private float screenWidth;

    private boolean attackStart = true;
    private boolean ableHit = false;
    

    @Override
    public void create() {
        mainChar = new MainChar();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1000, 580);
        batch = new SpriteBatch();

        boss = new Boss();
        items = new Items();

        screenHeight = Gdx.graphics.getHeight();
		screenWidth = Gdx.graphics.getWidth();
		background1 = new Texture("backgrounds/Background.png");
		background2 = new Texture("backgrounds/Background.png");

    }

    @Override
    public void render() {
        ScreenUtils.clear(0,0.2f,0,1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        mainChar.loadIdle();
        mainChar.attack();
        mainChar.fall();
        mainChar.jump();
        mainChar.hitAnimation();
        MainChar.timePassed += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = (TextureRegion) MainChar.animation.getKeyFrame(MainChar.timePassed, true);

        if (!MainChar.jump && !MainChar.fall){
            if (Gdx.input.isKeyPressed(Keys.SPACE)) {
                MainChar.attack1 = true;
                if(boss.health == 2) {
                    items.initCoins(boss.bossXPos, boss.flip);
                    items.setCoinCount(20);
                }
                if(Gdx.input.isKeyJustPressed(Keys.SPACE) && attackStart) {
                    attackStart = false;
                    boss.getHit(MainChar.charX, MainChar.charY);
                }

                if(MainChar.animation.isAnimationFinished(MainChar.timePassed)) {
                    attackStart = true;
                }
            }
        }

        // if (ableHit){
        //     MainChar.damageDone = true;
        //     mainChar.getHit(boss.bossXPos, groundLevel);
        // }
        
        if (!MainChar.attack1 && !MainChar.jump && !MainChar.fall && MainChar.jumpPossible){
            if (Gdx.input.isKeyPressed(Keys.UP))
                MainChar.jump = true;
        }
        if (!MainChar.jump){
            if (MainChar.charY > groundLevel){
                MainChar.fall = true;
            }
            else{
                MainChar.jumpPossible = true;
                MainChar.fall = false;
            }
        }
        
        if (!MainChar.attack1 && !MainChar.jump && !MainChar.fall){
            if (Gdx.input.isKeyPressed(Keys.RIGHT)){
                mainChar.run();
                flipVal = 1;
            }
            else if (Gdx.input.isKeyPressed(Keys.LEFT)){
                mainChar.run();
                flipVal = -1;
            }
        }

        if (Gdx.input.isKeyPressed(Keys.RIGHT)){
            flipVal = 1;
            MainChar.charX += 300 * Gdx.graphics.getDeltaTime();
        }
        else if (Gdx.input.isKeyPressed(Keys.LEFT)){
            flipVal = -1;
            MainChar.charX -= 300 * Gdx.graphics.getDeltaTime();
        }
        

        boss.move(MainChar.charX);

        boss.stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion bossFrame = boss.getFrame(boss.stateTime);
        if(boss.flip) {
            if(!bossFrame.isFlipX()) {
                bossFrame.flip(true, false);
            }
        } else {
            if(bossFrame.isFlipX()) {
                bossFrame.flip(true, false);
            }
        }

        if(!Boss.attack) {
            ableHit = false;
        }

        if (Boss.cleaveAnimation.isAnimationFinished(Boss.stateTime + 2*(Boss.attackSpeed)) && Boss.attack && !ableHit) {
            System.out.println(flipVal);
            // mainChar.getHit(boss.bossXPos, groundLevel);
            ableHit = true;
        }

        boss.generateHealthBar();
        
        currentFrame = (TextureRegion) MainChar.animation.getKeyFrame(MainChar.timePassed, true); 

        // Pixmap map = new Pixmap((int) boss.player.width, (int) boss.player.height, Pixmap.Format.RGBA8888);
        // map.setColor(Color.BLUE);
        // map.fillRectangle(0, 0, (int) boss.player.width, (int) boss.player.height);

        // Pixmap map2 = new Pixmap((int) boss.boss.width, (int) boss.boss.height, Pixmap.Format.RGBA8888);
        // map2.setColor(Color.RED);
        // map2.fillRectangle(0, 0, (int) boss.boss.width, 150);

        batch.begin();
        batch.draw(background1 , backgroundX , 0 , screenWidth , screenHeight);
		batch.draw(background2 , backgroundX + screenWidth, 0 , screenWidth , screenHeight);
		if(boss.health > 0) {
			batch.draw(bossFrame, boss.bossXPos, groundLevel, boss.bossWidth, boss.bossHeight);
		} else {
			items.spawnCoins(boss.bossXPos, batch, MainChar.charX, groundLevel, (currentFrame.getRegionWidth())*(5/2), (currentFrame.getRegionHeight())*(5/2));
		}
        batch.draw(currentFrame, MainChar.charX, MainChar.charY, ((flipVal)*(currentFrame.getRegionWidth())*(5/2)), (currentFrame.getRegionHeight())*(5/2));
        batch.draw(boss.healthTexture(boss.backgroundBar), boss.healthCoordinates("backgroundX"), boss.healthCoordinates("backgroundY"));
		batch.draw(boss.healthTexture(boss.healthBar), boss.healthCoordinates("healthX"), boss.healthCoordinates("healthY"));
		// batch.draw(new Texture(map), MainChar.charX, MainChar.charY);
        // batch.draw(new Texture(map2), boss.bossXPos, groundLevel);

        items.displayHearts(batch);
		items.displayCoinsCollected(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        boss.dispose();
		background1.dispose();
		background2.dispose();
    }
    
}