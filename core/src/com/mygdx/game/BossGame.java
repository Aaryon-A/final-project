package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class BossGame extends ApplicationAdapter {
    public static final int HEIGHT = 580;
    public static final int WIDTH = 1000;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Boss boss;


    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        batch = new SpriteBatch();
        boss = new Boss();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        boss.stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = boss.getFrame(boss.stateTime);
        if(boss.flip) {
            if(!currentFrame.isFlipX()) {
                currentFrame.flip(true, false);
                //boss.bossXPos += boss.bossWidth;
            }
        } else {
            if(currentFrame.isFlipX()) {
                currentFrame.flip(true, false);
                //boss.bossXPos -= boss.bossWidth;
            }
        }

        batch.begin();
        batch.draw(currentFrame, boss.bossXPos, boss.bossYPos, boss.bossWidth, boss.bossHeight);
        batch.end();
        
        //boss.move(Gdx.input.getX());
    }

    @Override
    public void dispose() {
        batch.dispose();
        boss.dispose();
    }
}
