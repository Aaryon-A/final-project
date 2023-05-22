package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/**
 * This class displays the hearts of the player and the coins
 * It is also used to call the Coins class to display coins when an enemy dies
 */
public class Items {
    /**
     * The animation used to display the coins collected
     */
    public Animation<TextureRegion> flashCoin;

    /**
     * The image to display a life
     */
    public Texture fullHeart;
    
    /**
     * The image used to display a lost life
     */
    public Texture blankHeart;

    /**
     * The TextureAtlas used for the coin display animation
     */
    private TextureAtlas flashAtlas;

    /**
     * Used to keep track of the lives
     */
    static int lives = 5;

    /**
     * Used to generate a font from a file
     */
    private FreeTypeFontGenerator generator;

    /**
     * Used to specify the size of the font
     */
    private FreeTypeFontParameter parameter;

    /**
     * Used to store the font in a variable
     */
    private BitmapFont font;

    /**
     * Used to keep track of the number of coins collected
     */
    public int coinsCollected = 0;

    /**
     * Used to keep track of the time in the animation
     */
    public float coinFlashTime = 0.0f;

    /**
     * Used to create a delay in the coin flash animation
     */
    public boolean flash = true;

    /**
     * Used to keep track of the delay in the animation
     */
    private float flashTimer = 0.0f;

    /**
     * Used to create a list of coins spawned from a dead enemy
     */
    private ArrayList<Coins> spinCoins;

    /**
     * Used to keep track of the y-coordinate which is the ground level
     */
    public int groundLevel = 86;

    /**
     * This is the number of coins spawned from an enemy
     */
    private int coinCount = 20;

    /**
     * Initializes all variables including text and images
     */
    public Items() {
        flashAtlas = new TextureAtlas(Gdx.files.internal("items/flash.atlas"));

        flashCoin = new Animation<TextureRegion>(0.100f, flashAtlas.findRegions("flash"));
        blankHeart = new Texture("items/HeartContainer.png");
        fullHeart = new Texture("items/HeartWithContainer.png");
        spinCoins = new ArrayList<>();
        
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/CoinFont.ttf"));
        parameter = new FreeTypeFontParameter();
        parameter.size = 25;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    /**
     * Initializes a list of Coins with new coordinates to make sure it is updated to when the enemy dies
     * @param xPos the x-coordinate of the enemy
     * @param flip shows which direction the enemy is facing
     */
    public void initCoins(float xPos, boolean flip) {
        for(int i = 0; i < coinCount; i++) {
            spinCoins.add(new Coins(xPos, groundLevel, flip));
        }
    }

    /**
     * Sets coinCount to a specific value
     * @param count the number to be set to coinCount
     */
    public void setCoinCount(int count) {
        coinCount = count;
    }
    
    /**
     * Displays the hearts of the player
     * @param batch the SpriteBatch used to display images on the screen
     */
    public void displayHearts(SpriteBatch batch) {
        int increment = 0;
        // Displays 1 heart for every life
        for(int i = 0; i < lives; i++) {
            batch.draw(fullHeart, 50 + increment, 550, 25, 25);
            increment += 35;
        }

        // Displays a blank heart for every lost life
        for(int i = 0; i < (5-lives); i++) {
            batch.draw(blankHeart, 50 + increment, 550, 25, 25);
            increment += 35;
        }
    }

    /**
     * Shows the amount of coins collected along with an animation
     * @param batch the SpriteBatch used to display images on the screen
     */
    public void displayCoinsCollected(SpriteBatch batch) {
        String displayText = coinsCollected + "x";

        // Displays the text on the screen
        font.draw(batch, displayText, 930, 550);

        TextureRegion currentFrame = flashCoin.getKeyFrame(coinFlashTime, flash);
        if(flash) {
            coinFlashTime += Gdx.graphics.getDeltaTime();
        }
        
        batch.draw(currentFrame, 970, 530, 25, 25);

        if(flashCoin.isAnimationFinished(coinFlashTime)) {
            coinFlashTime = 0.0f;
            flash = false;
        }

        if(!flash) {
            // Once a certain time has been reached then start the animation again by resetting the variables
            flashTimer += Gdx.graphics.getDeltaTime();
            if(flashTimer >= 0.6f) {
                flashTimer = 0.0f;
                flash = true;
            }
        }
    }

    /**
     * This class is used to spawn coins whenever an enemy dies
     * @param xPos the x-coordinate of the enemy
     * @param batch the SpriteBatch used to display images on the screen
     * @param heroXPos the x-coordinate of the player
     * @param heroYPos the y-coordinate of the player
     * @param heroWidth the width of the player
     * @param heroHeight the height of the player
     */
    public void spawnCoins(float xPos, SpriteBatch batch, float heroXPos, float heroYPos, float heroWidth, float heroHeight) {
        for(int i = 0; i < coinCount; i++) {
            spinCoins.get(i).currentFrame = Coins.spinAnimation.getKeyFrame(Coins.coinSpinTime, true);
            batch.draw(spinCoins.get(i).currentFrame, spinCoins.get(i).xPos, spinCoins.get(i).yPos, spinCoins.get(i).size, spinCoins.get(i).size);
            spinCoins.get(i).updateHitBox(heroXPos, heroYPos, heroWidth, heroHeight);
            spinCoins.get(i).projMotion();
            if(spinCoins.get(i).checkCollision()) {
                // If the coin has been touched by the player remove it from the list and decrease the amount of coins to iterate by
                coinCount -= 1;
                spinCoins.remove(i);
                coinsCollected += 1;
            }
        }

        Coins.coinSpinTime += Gdx.graphics.getDeltaTime();
    }
}
