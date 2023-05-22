// Importing all required 
package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;

/**
 * This is the class used to store all the variables for the different screens in mainGame.
 * @author Pratham Bhalla
 */
public class Background {
    /**
	 * These are all the images used in the mainGame.
	 */
    public static Texture background1, background2, background3, bossFightBackground, buttonUp,
    buttonDown, startBackground, playButton, optionsButton, backButton, helptext, winScreenBackground,
    quitButton;
    
    /**
	 * This is the main animation used for the boss
	 */
    static float backgroundVelocity = 4;
    static float backgroundX = 0;
    static float screenHeight;
    static float screenWidth;
    static BitmapFont font;
    static final int HEIGHT = 580;
    static final int WIDTH = 1000;

    /**
	 * This are all initilizing variables I am using in the game.
	 */
    static Leaderboard leaderboard;
    static FreeTypeFontGenerator generator;
    static FreeTypeFontParameter parameter;
    static GlyphLayout layout;
    static boolean buttonClick = false;
    static InputProcess input = new InputProcess();
    static Rectangle button1 = new Rectangle(600, 240, 350, 180);
    static Rectangle button2 = new Rectangle(600, 40, 350, 180);
    static Rectangle button3 = new Rectangle(50 , 40, 350, 180);

    
}
