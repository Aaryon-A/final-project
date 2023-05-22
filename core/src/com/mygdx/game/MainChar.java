package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * This is the class used to make the main character It holds all code used
 * 
 * @author Vaibhav-B
 */
public class MainChar {

    /**
     * This boolean used for checking if damage is being done
     */
    private boolean damageDone;

    /**
     * Initialized boss class
     */
    private Boss boss;

    /**
     * Initialized items class
     */
    private Items item;

    /**
     * This is the texture atlas used for the character
     */
    private TextureAtlas textureAtlas;

    /**
     * Rectangles for player hitbox and enemy hitbox
     */
    public Rectangle player, enemyRect;

    /**
     * This boolean used for checking if theres hit stun
     */
    static boolean hitStun;

    /**
     * This is the animation for the character
     */
    static Animation animation;

    /**
     * This counts time elasped
     */
    static float timePassed;

    /**
     * checking if attack doing an attack or not
     */
    static boolean attack1 = false;

    /**
     * boolean for jump
     */
    static boolean jump = false;

    /**
     * Boolean for falling
     */
    static boolean fall = false;

    /**
     * to check if jump is possible
     */
    static boolean jumpPossible = false;

    /**
     * character x and y
     */
    static int charX = 500;
    static int charY = 290;

    /**
     * Initializes all of the varaibles
     */
    public MainChar() {
        loadIdle();
        timePassed = 0.0f;
        boss = new Boss();
        item = new Items();
        player = new Rectangle(charX, charY, 100, 100);
        enemyRect = new Rectangle(0, 86, 276, 330);
    }

    /**
     * Function for idle animation, after each animation loop ends the time passed
     * gets set back to 0 to reset the animation
     */
    public void loadIdle() {
        textureAtlas = new TextureAtlas(Gdx.files.internal("KnightSprite/idle.atlas"));
        animation = new Animation<TextureRegion>(0.100f, textureAtlas.findRegions("idle"));
        animation.setPlayMode(Animation.PlayMode.LOOP);
        if (animation.isAnimationFinished(timePassed))
            timePassed = 0.0f;
    }

    /**
     * Function for run animation, after each animation loop ends the time passed
     * gets set back to 0 to reset the animation
     */
    public void run() {
        textureAtlas = new TextureAtlas(Gdx.files.internal("KnightSprite/run.atlas"));
        animation = new Animation<TextureRegion>(0.050f, textureAtlas.findRegions("run"));
        animation.setPlayMode(Animation.PlayMode.LOOP);
        if (animation.isAnimationFinished(timePassed))
            timePassed = 0.0f;
    }

    /**
     * Function for attack animation, after each animation loop ends the time passed
     * gets set back to 0 to reset the animation
     */
    public void attack() {
        // Makes sure to only run of variable attack1 is true which happens if spacebar
        // is clicked
        if (attack1) {
            textureAtlas = new TextureAtlas(Gdx.files.internal("KnightSprite/attack1.atlas"));
            animation = new Animation<TextureRegion>(0.10f, textureAtlas.findRegions("attack1"));
            animation.setPlayMode(Animation.PlayMode.LOOP);

            // sets attack1 to false again to only run animation once
            if (animation.isAnimationFinished(timePassed)) {
                timePassed = 0.0f;
                attack1 = false;
            }
        }
    }

    /**
     * Function for checking if character got hit, makes rectangles for hitboxes
     * then sees if they overlap and then runs hitstun aniamtion and makes lives go
     * down 1
     */
    public void getHit(float enemyPos, float groundLevel, int width, int height) {
        player = new Rectangle(charX, charY, 100, 100);
        enemyRect = new Rectangle(enemyPos, groundLevel, width, height);

        if (enemyRect.overlaps(player) && Boss.attack) {
            damageDone = true;
        }
        if (damageDone && !hitStun) {
            damageDone = false;
            Items.lives -= 1;
            hitStun = true;
        }
    }

    /**
	 * Function that runs a hitstun animation if called for which happens if character is overlapping with enemy while they are attacking
	 */
    public void hitAnimation() {
        if (hitStun) {
            textureAtlas = new TextureAtlas(Gdx.files.internal("KnightSprite/hit.atlas"));
            animation = new Animation<TextureRegion>(0.2f, textureAtlas.findRegions("hit"));
            animation.setPlayMode(Animation.PlayMode.LOOP);

            if (animation.isAnimationFinished(timePassed)) {
                hitStun = false;
                timePassed = 0.0f;
            }
        }
    }

    /**
	 * Function for to jump, it only runs if jump is possible and jump button us true
	 */
    public void jump() {
        if (jump && jumpPossible) {
            textureAtlas = new TextureAtlas(Gdx.files.internal("KnightSprite/jump.atlas"));
            animation = new Animation<TextureRegion>(0.07f, textureAtlas.findRegions("jump"));
            animation.setPlayMode(Animation.PlayMode.LOOP);
            charY += 8;

            //once in the air the variables are set to false
            if (animation.isAnimationFinished(timePassed)) {
                timePassed = 0.0f;
                jump = false;
                jumpPossible = false;
            }
        }
    }

    /**
	 * Function for for the fall animation and movement runs if fall is true
	 */
    public void fall() {
        if (fall) {
            textureAtlas = new TextureAtlas(Gdx.files.internal("KnightSprite/fall.atlas"));
            animation = new Animation<TextureRegion>(0.25f, textureAtlas.findRegions("fall"));
            animation.setPlayMode(Animation.PlayMode.LOOP);
            charY -= 8;

            // sets fall back to false so character can go back to idle animation
            if (animation.isAnimationFinished(timePassed)) {
                timePassed = 0.0f;
                fall = false;
            }
        }
    }

    /**
	 * Disposes of the atlas
	 */
    public void dispose() {
        textureAtlas.dispose();
    }
}