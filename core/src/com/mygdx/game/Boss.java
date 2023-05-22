package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * This is the class used to make the Boss. It holds all of the logic and graphics used
 * @author Aaryon-A
 */
public class Boss {
	/**
	 * This is the main animation used for the boss
	 */
    public Animation<TextureRegion> animation;

	/**
	 * This is the main TextureAtlas used for the boss animation
	 */
	private TextureAtlas atlas;

	/**
	 * This is used to keep track of the time in the animation
	 */
	static float stateTime;

	/**
	 * This is the width of the boss
	 */
	public int bossWidth;

	/**
	 * This is the height of the boss
	 */
	public int bossHeight;

	/**
	 * This is the x-coordinate of the boss
	 */
	public int bossXPos;

	/**
	 * This is the y-coordinate of the boss
	 */
	public int bossYPos;

	/**
	 * This is the constant multiplier that scales up the boss
	 */
	private int multiplier = 3;

	/**
	 * This is the width of the boss for the idle animation
	 */
	private int idleWidth = 92 * multiplier;

	/**
	 * This is the height of the boss for the idle animation
	 */
	private int idleHeight = 111 * multiplier;

	/**
	 * This is the width of the boss for the walk animation
	 */
	private int walkWidth = 77 * multiplier;

	/**
	 * This is the height of the boss for the walk animation
	 */
	private int walkHeight = 104 * multiplier;

	/**
	 * This is the height of the boss for the cleave animation
	 */
	private int cleaveHeight = 104 * multiplier;

	/**
	 * This is the width of the boss for the cleave animation
	 */
	private int cleaveWidth = 108 * multiplier;

	/**
	 * This is the width of the boss for the hit animation
	 */
	private int hitWidth = 102 * multiplier;

	/**
	 * This is the height of the boss for the hit animation
	 */
	private int hitHeight = 105 * multiplier;

	/**
	 * This is the width of the boss for the death animation
	 */
	private int deathWidth = 128 * multiplier;

	/**
	 * This is the height of the boss for the death animation
	 */
	private int deathHeight = 121 * multiplier;

	/**
	 * This is a variable used to keep track if the boss is able to move
	 */
	private boolean move = false;

	/**
	 * This is how fast the boss moves
	 */
	private int moveSpeed = 3;

	/**
	 * This is how fast the boss attacks
	 */
	static float attackSpeed = 0.150f;

	/**
	 * This is the animation used for the idle animation
	 */
	public Animation<TextureRegion> idleAnimation;

	/**
	 * This is the animation used for the walk animation
	 */
	public Animation<TextureRegion> walkAnimation;

	/**
	 * This is the animation used for the cleave animation
	 */
	public static Animation<TextureRegion> cleaveAnimation;

	/**
	 * This is the animation used for the hit animation
	 */
	public Animation<TextureRegion> hitAnimation;

	/**
	 * This is the animation used for the death animation
	 */
	public Animation<TextureRegion> deathAnimation;

	/**
	 * This is used to keep track if the boss is able to attack or not
	 */
	static boolean attack = false;

	/**
	 * This is the default position of the player
	 */
	public float heroPos = 200;

	/**
	 * This variable is used to flip the image of the boss depending on which way it's facing
	 */
	public boolean flip = false;

	/**
	 * This variable is used to keep track of which animation the boss is currently in
	 */
	public String atlasPath = "";

	/**
	 * This Rectangle is used for the player hitbox
	 */
	Rectangle player;

	/**
	 * This Rectangle is used for the boss hitbox
	 */
	Rectangle boss;

	/**
	 * This is the health of the boss
	 */
	public int health = 100;

	/**
	 * This is used to display a shader for the boss health bar
	 */
	public Pixmap backgroundBar;

	/**
	 * This displays the actual health bar of the boss
	 */
	public Pixmap healthBar;

	/**
	 * This variable becomes true when the health reaches 0
	 */
	public boolean death = false;

    /**
	 * Initializes all of the varaibles
	 */
	public Boss() {
		atlas = changeImage("idle");
		bossXPos = 200;
		bossYPos = 200;
		bossHeight = idleHeight;
		bossWidth = idleWidth;
		newAnimation();
		
        stateTime = 0.0f;

		animation = idleAnimation;

		player = new Rectangle(heroPos, 89, 39, 1);
		boss = new Rectangle(bossXPos, 89, idleWidth, idleHeight);
    }

	/**
	 * Resets all of the animations and the state time back to 0 so it can be used to change animations
	 */
	public void newAnimation() {
		idleAnimation = new Animation<TextureRegion>(0.100f, atlas.findRegions("idle"));
		walkAnimation = new Animation<TextureRegion>(0.100f, atlas.findRegions("walk"));
		cleaveAnimation = new Animation<TextureRegion>(attackSpeed, atlas.findRegions("cleave"));
		hitAnimation = new Animation<TextureRegion>(0.080f, atlas.findRegions("hit"));
		deathAnimation = new Animation<TextureRegion>(0.100f, atlas.findRegions("death"));
		stateTime = 0.0f;
	}

	/**
	 * Draws the health bar plus a shaded background
	 */
	public void generateHealthBar() {
		backgroundBar = new Pixmap(300, 20, Pixmap.Format.RGBA8888);
		backgroundBar.setColor(Color.GRAY);
		backgroundBar.fillRectangle(0, 0, backgroundBar.getWidth(), backgroundBar.getHeight());

		healthBar = new Pixmap((health) * 3, 18, Pixmap.Format.RGBA8888);
		healthBar.setColor(Color.RED);
		healthBar.fillRectangle(0, 0, healthBar.getWidth(), healthBar.getHeight());
	}

	/**
	 * Used to convert a Pixmap to a Texture
	 * @param map the Pixmap that will be converted
	 * @return the Texture from a Pixmap
	 */
	public Texture healthTexture(Pixmap map) {
		return new Texture(map);
	}

	/**
	 * Depending on the string it returns a coordinate, mainly used to keep all the logic inside the Boss Class
	 * @param str the name of the coordinate to be returned
	 * @return the number for the corresponding coordinate
	 */
	public int healthCoordinates(String str) {
		if(str == "backgroundX") {
			return 350;
		} else if (str == "backgroundY") {
			return 550;
		} else if (str == "healthX") {
			return 350;
		} else if(str == "healthY") {
			return 550;
		}

		return 0;
	}

	/**
	 * Used for handling the logic for when the boss gets hit
	 * @param pos the coordinates of the hero
	 */
	public void getHit(float xPos, float yPos) {
		if(!attack && !death) {
			player = new Rectangle(xPos, yPos, 200, 100);
			boss = new Rectangle(bossXPos, 86, idleWidth, idleHeight);

			// If the hitboxes overlap then change the animation to the hit animation
			if(player.overlaps(boss)) {
				atlas = changeImage("hit");
				newAnimation();
				animation = hitAnimation;
				bossWidth = hitWidth;
				bossHeight = hitHeight;
				heroPos = bossXPos;
				// Once the health reaches below 4 then run the death function
				if(health >= 4) {
					health -= 2;
				} else {
					death();
				}
				
			}
		}
	}

	/**
	 * Sets the death variable to true and changes the animation to the death one
	 */
	private void death() {
		death = true;
		atlas = changeImage("death");
		newAnimation();
		animation = deathAnimation;
		bossWidth = deathWidth;
		bossHeight = deathHeight;
	}

	/**
	 * Makes the boss move towards the player at all times and attack
	 */
	public void move(float pos) {
		// This makes the position an easy to work with number and not a decimal
		pos = pos - (pos % 10);

		// Once the attack animation is finished go back to idle
		if(attack && animation.isAnimationFinished(stateTime)) {
			atlas = changeImage("idle");
			newAnimation();
			animation = idleAnimation;
			attack = false;
			bossWidth = idleWidth;
			bossHeight = idleHeight;
		}

		// After the hit animation is done go back to idle
		if(atlasPath == "hit" && animation.isAnimationFinished(stateTime)) {
			atlas = changeImage("idle");
			newAnimation();
			animation = idleAnimation;
			attack = false;
			bossWidth = idleWidth;
			bossHeight = idleHeight;
			move = false;
		}

		// After the death animation is done make the health 0
		if(atlasPath == "death" && animation.isAnimationFinished(stateTime)) {
			health = 0;
		}

		// Once the idle animation is done then set up the varaibles to restart the move process
		if(atlasPath == "idle" && animation.isAnimationFinished(stateTime)) {
			move = false;
			heroPos = pos;
		}

		// If the hero is at a different location then set it to the walking animation
		if((heroPos != bossXPos) && !move) {
			heroPos = pos;
			move = true;
			bossHeight = walkHeight;
			bossWidth = walkWidth;
			atlas = changeImage("walk");
			newAnimation();
			animation = walkAnimation;
			// Changes the move and attack speed to a random number
			if(Math.random() <= 0.5) {
				moveSpeed = 5;
				attackSpeed = 0.100f;
			} else {
				moveSpeed = 3;
				attackSpeed = 0.150f;
			}
		}

		// Updates the hitboxes to the current position
		player = new Rectangle(heroPos, 86, 100, 100);
		boss = new Rectangle(bossXPos, 86, idleWidth, 330);

		// If the boss reaches the player then attack
		if(player.overlaps(boss)) {
			if(atlasPath.equals("walk")) {
				attack();
			}
		} else if(heroPos > bossXPos) {
			// Flips the boss depending on the player position
			// Moves the boss but if it hits the edge then stop there
			flip = true;
			if(bossXPos+cleaveWidth <= BossGame.WIDTH) {
				bossXPos += moveSpeed;
			} else {
				heroPos = bossXPos;
			}
		} else if(heroPos < bossXPos) {
			flip = false;
			if(bossXPos >= 0) {
				bossXPos -= moveSpeed;
			} else {
				heroPos = bossXPos;
			}
		} 
	}

	/**
	 * Changes the animation to the attack animation
	 */
	public void attack() {
		atlas = changeImage("cleave");
		newAnimation();
		animation = cleaveAnimation;
		bossWidth = cleaveWidth;
		bossHeight = cleaveHeight;
		attack = true;
	}

	/**
	 * Returns the current frame of the animation
	 * @param time the current state time of the animation
	 * @return the TextureRegion that corresponds to the time
	 */
	public TextureRegion getFrame(float time) {
		return animation.getKeyFrame(time, true);
	}

	/**
	 * Makes it easier to change the TextureAtlas to look for a different .atlas file
	 * @param filePath the name of the file
	 * @return a new TextureAtlas with the corresponding file name
	 */
	public TextureAtlas changeImage(String filePath) {
		atlasPath = filePath;
		filePath = "bossImages/" + filePath + ".atlas";
		return new TextureAtlas(Gdx.files.internal(filePath));
	} 

	/**
	 * Disposes of the atlas
	 */
	public void dispose() {
		atlas.dispose();
	}
}
