package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * This class is used to spawn coins when an enemy dies and is called by the Items class
 */
public class Coins {
    /**
     * The x-coordinate of the coin
     */
    public float xPos;

    /**
     * The y-coordinate of the coin
     */
    public float yPos;

    /**
     * The size of the coin
     */
    public float size;

    /**
     * The y-coordinate of the ground level
     */
    public float groundLevel;

    /**
     * The animation that makes the coin spin
     */
    public static Animation<TextureRegion> spinAnimation;

    /**
     * The TextureAtlas that corresponds with the spinning animation
     */
    private TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("items/spin.atlas"));

    /**
     * The current time of the coin animation
     */
    public static float coinSpinTime;

    /**
     * The current frame of the animation
     */
    public TextureRegion currentFrame;

    /**
     * The speed at which the coin is moving in the y-direction
     */
    public float yVelocity;

    /**
     * The speed at which the coin is moving in the x-direction
     */
    public float xVelocity;
    
    /**
     * The speed at which the y-velocity decreases at
     */
    public float gravity;

    /**
     * Whether or not the projectile motion of the coins have finished
     */
    public boolean motionEnd = false;

    /**
     * The Rectangle used for the coin hitbox
     */
    public Rectangle coinHitBox;

    /**
     * The Rectangle used for the player hitbox
     */
    public Rectangle heroHitBox;

    /**
     * Initializes varaibles
     * @param xPos the x-coordinate of the dead enemy
     * @param groundLevel the y-coordinate of the enemy (In this scenario the y-coordinate is the same for all enemies)
     * @param flip the direction the enemy is facing
     */
    public Coins(float xPos, int groundLevel, boolean flip) {
        spinAnimation = new Animation<TextureRegion>(0.100f, atlas.findRegions("spin"));
        this.xPos = xPos;
        // This ensures that the coins spawn away from the player depending on the orientation
        if(!flip) {
            this.xPos += 100;
        }
        yPos = groundLevel;
        size = 25;
        this.groundLevel = groundLevel;

        coinSpinTime = 0.0f;

        yVelocity = 20 + (float)(Math.random() * 100);
        xVelocity = 5 + (float)(Math.random() * 30);
        gravity = -9.8f;

        // This causes some coins to go left and other coins to go right
        if(Math.random() >= 0.5) {
            xVelocity = -xVelocity;
        }
    }

    /**
     * This updates the hitboxes of the coins and the player
     * @param heroXPos the x-coordinate of the player
     * @param heroYPos the y-coordinate of the player
     * @param heroWidth the width of the player
     * @param heroHeight the height of the player
     */
    public void updateHitBox(float heroXPos, float heroYPos, float heroWidth, float heroHeight) {
        coinHitBox = new Rectangle(this.xPos, this.yPos, this.size, this.size);
        heroHitBox = new Rectangle(heroXPos, heroYPos, heroWidth, heroHeight);
    }

    /**
     * This function causes each coin to experience projectile motion before they reach the ground
     */
    public void projMotion() {
        if(!motionEnd) {
            // This involves some physics concepts
            // Essentially the y-coordinate increases by the y-velocity but the y-veloctiy also decreases by gravity
            this.yPos += yVelocity;
            yVelocity += (gravity);
            
            
            // When the coin hits the edge of the screen then it moves the opposite direction
            if(this.xPos >= (1000-size) || this.xPos <= 0) {
                xVelocity = -xVelocity;
            }

            // The x-coordinate increases at a constant speed
            this.xPos += xVelocity;
        }

        if(this.yPos <= groundLevel) {
            motionEnd = true;
            this.yPos = groundLevel;
            // If any coins are out of bounds then force them back into the screen
            if(this.xPos >= (1000-size)) {
                this.xPos = (1000-size);
            }
        }
    }

    /**
     * When the player hits a coin then it returns true
     * @return returns true when the player touches the coin
     */
    public boolean checkCollision() {
        if(heroHitBox.overlaps(coinHitBox)) {
            return true;
        }
        return false;
    }
    
}
