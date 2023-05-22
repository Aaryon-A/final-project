package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * This is the class used to make the enemies. This class holds all the relevant
 * info and logic for the enemy.
 * 
 * @author Hashir
 */
public class Enemy {

    // Both Coordinates that are subject to updating for movement and spawning.
    public int enemyX = 740;
    public int enemyY = 86;

    // Both sizing variables which hold the sizes when updated. It is being
    // initialised here.
    public int enemyWidth;
    public int enemyHeight;

    // Variable which holds the life count for the first enemy.
    public int enemyLife1 = 2;

    // Variable used as conditional for checking when to switch between enemies.
    public static int enemyCount = 1;

    /*
     * Both variables which is used to hold/set/switch the strings between different
     * enemy animations which is the attack/hit and move/walk animations
     * respectively.
     */
    public static String enemyWalk = "walk";
    public static String enemyHit = "hit";

    /*
     * Boolean variables which hold the conditionals to switch the enemies direction
     * when moving towards character and enemyKill which runs the code for deleting
     * the enemy
     * off the screen.
     */
    public boolean direction = false;
    public boolean enemyKill = false;

    // Main animations/Atlases used for running the various different animations for
    // moving and hitting.
    Animation<TextureRegion> newAnimation;
    TextureAtlas atlas;

    // String Variable which holds the information of which animation is running
    // during any point of the game.
    private String atlasPath;

    //Variable used to track the time of animations for loops etc.
    float elapsedTime;

    //Variables using for conditioning the if statements regarding when enemy moves and attacks 
    // and its respective checking/switching of animations and movement.
    boolean moving = false;
    boolean attack = false;

    //Rectangles created for the enemy and player hitboxes.
    Rectangle enemy = new Rectangle((enemyX + 50), 90, 50, 100);
    Rectangle player1 = new Rectangle((MainChar.charX - 65), MainChar.charY, 170, 65);

    /**
	 * Holds and resets all the animations so it can be looped and switched/interchangable.
	 */
    public void createImages() {
        newAnimation = new Animation<TextureRegion>(1f / 10f, atlas.findRegions(atlasPath));
        newAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    /**
	 * Method that is called and used for checking when enemy comes in contact with player and 
     * what to do when the player kills the enemy.
	 */
    public void hitDetect() {

        //If statement which checks when its being attacked or not and creates the hitboxes accordingly.
        if (!attack) {
            enemy = new Rectangle((enemyX + 50), 90, 50, 100);
            player1 = new Rectangle((MainChar.charX - 65), MainChar.charY, 170, 65);
        }

        //If statement for changing between the enemy animations after 4 enemies have been killed.
        if (enemyCount <= 4) {
            enemyWalk = "walk";
            enemyHit = "hit";
            enemyWidth = 75;
            enemyHeight = 75;
        }
        if (enemyCount >= 5 && enemyCount <= 8) {
            enemyWalk = "move";
            enemyHit = "attack";
            enemyY = 14;
            enemyWidth = 210;
            enemyHeight = 210;
        }

        //If statement for checking when the player attacks the enemy and detects it accordingly,
        //reducing the life or killing the enemy straightaway.
        if (player1.overlaps(enemy) && Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            if (enemyCount <= 4) {
                enemyLife1 -= 1;
            } else {
                enemyKill = true;
            }

            //If statement that checks when the enemy life turns to zero and runs the death for enemy.
            if (enemyLife1 == 0) {
                enemyKill = true;
            }
        }
    }

    /**
	 * Method used and called for the movement of enemy and holds all its respective logic.
     * @param charVal the coordinates of the character, used for checking detection and switching animations.
	 */
    public void movement(int charVal) {

        //Follwing two If statement that checks if the animation is a hit/walk animation and 
        //switches it to moving/attacking respectively.
        if (atlasPath == enemyHit && newAnimation.isAnimationFinished(elapsedTime)) {
            atlas = changeImage(enemyWalk);
            createImages();
            attack = false;
        }

        if (atlasPath == enemyWalk && newAnimation.isAnimationFinished(elapsedTime)) {
            atlas = changeImage(enemyHit);
            attack = true;
        }

        //If statement that checks when enemy is not anywhere close to player or doing anything and sets it to follow player.
        if (enemyX != charVal && !moving && !attack) {
            atlas = changeImage(enemyWalk);
            createImages();
            moving = true;
        }

        //If statement that holds logic for player being close to enemy and runs hit.
        if (Math.abs(charVal - enemyX) <= 10) {
            moving = false;
            atlas = changeImage(enemyHit);
            createImages();
            attack = true;
        } 
        //Two If statements that hold logic for the enemy moving left and right towards player.
        else if (charVal >= enemyX) {
            direction = false;
            enemyX += 2;
        } else if (charVal <= enemyX) {
            direction = true;
            enemyX -= 2;
        }

        //Updates the rectangles to the current position at any given moment.
        enemy = new Rectangle((enemyX + 50), 90, 50, 100);
        player1 = new Rectangle((MainChar.charX - 65), MainChar.charY, 170, 65);

    }

    /**
	 * Method which allows for changing atlas names for reference and changing images easily and more accessible.
	 * @param filePath Name of set file
	 * @return a new TextureAtlas with the updated filepath
	 */
    public TextureAtlas changeImage(String filePath) {
        atlasPath = filePath;
        filePath = "enemyImage/" + filePath + ".atlas";
        return new TextureAtlas(Gdx.files.internal(filePath));
    }

    /**
	 * Method which cleans/removes/disposes the atlas.
	 */
    public void dispose() {
        atlas.dispose();
    }

}
