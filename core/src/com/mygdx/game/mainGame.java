// Importing everything that is needed.
package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

public class mainGame extends ApplicationAdapter {

    // Pratham Code
    private int menunumber = 1;
    private SpriteBatch batch;

    // Vaibhav code

    /**
     * This is an instance of the mainChar class
     */
    private MainChar mainChar;

    /**
     * int used to determine if main character is flipped or not
     */
    private int flipVal = 1;

    /**
     * This is the ground level
     */
    private int groundLevel = 86;

    /**
     * Variable to check if character is able to get hit
     */
    private boolean ableHit = false;

    /**
     * To check if frames have to be reset
     */
    private boolean frameReset = false;

    /**
     * Offset for x value after flip
     */
    int xOffset = 0;

    // Aaryon code
    /**
     * This is an instance of the Boss class used to display the boss
     */
    private Boss boss;

    /**
     * This is used to display coins collected and hearts of the player
     */
    private Items items;

    /**
     * This is used to make sure the hero doesn't deal too much damage to the boss
     * at once
     */
    private boolean attackStart = true;

    // Hashir code
    // List Variable used for creating new enemy at any set interval.
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    // Variable which holds/updates the different animations for the enemy.
    TextureRegion enemyFrame;
    // Random Variable to randomly spawn the enemy at unexpected times.
    Random randNum1 = new Random();

    @Override
    public void create() {

        // Pratham Code
        /**
         * Creating everything and initilzing it.
         */
        batch = new SpriteBatch();
        Background.font = new BitmapFont();
        Background.font.setColor(Color.WHITE);
        Background.screenHeight = Gdx.graphics.getHeight();
        Background.screenWidth = Gdx.graphics.getWidth();
        Background.background1 = new Texture("backgrounds/Background.png");
        Background.background2 = new Texture("backgrounds/Background.png");
        Background.background3 = new Texture("backgrounds/Background2.png");
        Background.bossFightBackground = new Texture("backgrounds/BossFightBackground.jpg");
        Background.startBackground = new Texture("backgrounds/startBackground.jpg");
        Background.winScreenBackground = new Texture("backgrounds/winScreenBackground.jpg");
        Background.leaderboard = new Leaderboard();
        Background.generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/CoinFont.ttf"));
        Background.parameter = new FreeTypeFontParameter();
        changeFontSize(25);
        Background.layout = new GlyphLayout();
        Background.leaderboard.writePoints(1000);
        Background.leaderboard.writePoints(301);
        Background.leaderboard.writePoints(1256);
        Background.buttonUp = new Texture("button/keyboard_down.png");
        Background.buttonDown = new Texture("button/keyboard_up.png");
        Background.playButton = new Texture("Buttons/playButton.png");
        Background.optionsButton = new Texture("Buttons/optionsButton.png");
        Background.backButton = new Texture("Buttons/backButton.png");
        Background.helptext = new Texture("helpText.png");
        Background.quitButton = new Texture("Buttons/quitButton.png");
        InputProcess.setButtons(Background.button1);
        InputProcess.setButtons(Background.button2);
        InputProcess.setButtons(Background.button3);

        // Vaibhav code
        mainChar = new MainChar();

        // Aaryon code
        boss = new Boss();
        items = new Items();
    }

    @Override
    /**
     * Code to switch between screens.
     */
    public void render() {
        switch (this.menunumber) {
            case 1:
                this.startScreen();
                break;
            case 2:
                this.mainScreen();
                break;
            case 3:
                this.bossScreen();
                break;
            case 4:
                this.endScreen();
                break;
            case 5:
                this.helpScreen();
                break;
        }

    }
    // All code for the main screen
    public void mainScreen() {
        ScreenUtils.clear(0, 0.2f, 0, 1);

        // Vaibhav code
        /*
         * Runs all my function and they each will only run the code if certain
         * variables are true
         * time passed is also counter here and current frame is determined here
         */
        mainChar.loadIdle();
        mainChar.attack();
        mainChar.fall();
        mainChar.jump();
        mainChar.hitAnimation();
        MainChar.timePassed += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = (TextureRegion) MainChar.animation.getKeyFrame(MainChar.timePassed, true);

        // Hashir code

        /*
         * Calls spawn enemy method which uses the random variable to spawn an enemy
         * once reaching a number less than 1 for the unexpected spawn times.
         */
        spawnEnemy(randNum1.nextInt(350));

        /*
         * Creating a listIterator that iterate elements of a list, returns it and
         * advances
         * the iterator count to the next position/value. I utilize this to run through
         * one enemy
         * then advance and run/create another enemy
         */
        ListIterator<Enemy> enemyListIterator = enemyList.listIterator();

        // Vaibhav code
        /*
         * Checks if character is jumping or fall if not then you can attck using
         * spacebar
         */
        if (!MainChar.jump && !MainChar.fall) {
            if (Gdx.input.isKeyPressed(Keys.SPACE))
                MainChar.attack1 = true;
        }

        /*
         * If attack, jump, fall are not true and jump possible is true you are allowed
         * to jump using up arrow
         */
        if (!MainChar.attack1 && !MainChar.jump && !MainChar.fall && MainChar.jumpPossible) {
            if (Gdx.input.isKeyPressed(Keys.UP))
                MainChar.jump = true;
            if (Gdx.input.isKeyJustPressed(Keys.UP)) {
                if (!frameReset)
                    frameReset = true;
            }
        }

        /*
         * If the main character is not on the ground junp possible is not true
         */
        if (!MainChar.jump) {
            if (MainChar.charY > groundLevel)
                MainChar.fall = true;
            else {
                MainChar.jumpPossible = true;
                MainChar.fall = false;
            }
        }

        /*
         * If not jumping, falling or attacking then you can run left or right using the
         * arrow keys
         */
        if (!MainChar.attack1 && !MainChar.jump && !MainChar.fall) {
            if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
                mainChar.run();
                flipVal = 1;
                // offset is to make sure that the character is aligned always even after a flip
                xOffset = 0;
            } else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
                mainChar.run();
                flipVal = -1;
                xOffset = ((currentFrame.getRegionWidth()) * (5 / 2));
            }
        }

        /*
         * If in the air the run animation will not run but the movement and flipping
         * still does
         */
        if (MainChar.jump || MainChar.fall) {
            if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
                flipVal = 1;
                xOffset = 0;
            } else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
                flipVal = -1;
                xOffset = ((currentFrame.getRegionWidth()) * (5 / 2));
            }
        }

        // resets frame to 0 so new animation can start from 0
        if (frameReset) {
            MainChar.timePassed = 0.0f;
            frameReset = false;
        }
        
        // Update current frame
        currentFrame = (TextureRegion) MainChar.animation.getKeyFrame(MainChar.timePassed, true);

        // Drawing all the assets required for this game.
        batch.begin();
        batch.draw(Background.background1, Background.backgroundX, 0, Background.screenWidth, Background.screenHeight);
        batch.draw(Background.background2, Background.backgroundX + Background.screenWidth, 0, Background.screenWidth,
                Background.screenHeight);
        batch.draw(Background.background2, Background.backgroundX + Background.screenWidth + Background.screenWidth, 0,
                Background.screenWidth, Background.screenHeight);

        /*
         * While loop that runs the first element while checking to see if their is
         * another
         * token/element after the current iterator element as it goes through the list.
         */
        while (enemyListIterator.hasNext()) {

            // Variable that retrieves the next enemy from the list and is saved within the
            // enemy variable.
            Enemy enemy = enemyListIterator.next();

            /*
             * If statement that checks for the enemyCount and runs it only when it is
             * within a
             * certain number of enemies to prevent a infinite spawn of enemies.
             */
            if (enemy.enemyCount <= 4 || (enemy.enemyCount >= 5 && enemy.enemyCount <= 8)) {

                // Block of code that calls and sets all the methods/variabels necessary for
                // running the enemies.

                // Sets intial animation to moving.
                enemy.atlas = enemy.changeImage(enemy.enemyWalk);
                enemy.createImages();
                enemy.hitDetect();
                enemy.movement(MainChar.charX);

                // Code for the updating the time of the enemy animation for updates and
                // changes.
                enemy.elapsedTime += Gdx.graphics.getDeltaTime();
                enemyFrame = enemy.newAnimation.getKeyFrame(enemy.elapsedTime, true);

                // Two If statements that check the direction of the enemy and where the player
                // is
                // and flips the image accordingly.
                if (enemy.direction) {
                    if (!enemyFrame.isFlipX()) {
                        enemyFrame.flip(true, false);
                    }
                } else {
                    if (enemyFrame.isFlipX()) {
                        enemyFrame.flip(true, false);
                    }
                }

                /*
                 * If Statement that checks when the enemy has been killed through a attack and
                 * runs the death process for removing the enemy and giving the player coins. This is also
                 * where the iterator's current value gets removed and goes to the next element.
                 */
                if (enemy.enemyKill == true && Gdx.input.isKeyPressed(Keys.SPACE)
                        && (enemy.enemyCount <= 4 || enemy.enemyCount >= 5 && enemy.enemyCount <= 8)) {
                    // Once an enemy dies the player gains 10 coins
                    items.coinsCollected += 10;
                    enemyListIterator.remove();
                    enemy.enemyCount++;
                    enemy.enemyKill = false;
                }

                // Code for drawing the enemy onto the screen with the updated coordinates and
                // sizing.
                batch.draw(enemyFrame, enemy.enemyX, enemy.enemyY, enemy.enemyWidth, enemy.enemyHeight);
            }
        }

        // This displays the coins collected
        items.displayCoinsCollected(batch);
        // This displays the hearts collected
        items.displayHearts(batch);
        batch.draw(currentFrame, MainChar.charX + xOffset, MainChar.charY,
                ((flipVal) * (currentFrame.getRegionWidth()) * (5 / 2)),
                (currentFrame.getRegionHeight()) * (5 / 2));
        batch.end();


        /*
        * All logic for the scrolling screen and running code. 
        */        
        if (Background.backgroundX > -1000 && MainChar.charX >= 496) {
            if (Gdx.input.isKeyPressed(Keys.RIGHT) && !MainChar.attack1) {
                Background.backgroundX -= Background.backgroundVelocity;
            }
        }
        if (Background.backgroundX < 0 && Background.backgroundX > -1000) {
            if (Gdx.input.isKeyPressed(Keys.LEFT) && !MainChar.attack1) {
                Background.backgroundX += Background.backgroundVelocity;
            }
        }

        if (Background.backgroundX == -1000) {
            if (Gdx.input.isKeyPressed(Keys.RIGHT) && !MainChar.attack1)
                MainChar.charX += 300 * Gdx.graphics.getDeltaTime();
            else if (Gdx.input.isKeyPressed(Keys.LEFT) && !MainChar.attack1)
                MainChar.charX -= 300 * Gdx.graphics.getDeltaTime();

            if (MainChar.charX <= 499) {
                Background.backgroundX += Background.backgroundVelocity;
            }
            if (MainChar.charX > 900) {
                menunumber = 3;
            }
        }

        if (Background.backgroundX == 0 && MainChar.charX >= 10) {
            if (Gdx.input.isKeyPressed(Keys.RIGHT) && !MainChar.attack1)
                MainChar.charX += 300 * Gdx.graphics.getDeltaTime();

            else if (Gdx.input.isKeyPressed(Keys.LEFT) && !MainChar.attack1)
                MainChar.charX -= 300 * Gdx.graphics.getDeltaTime();

            if (MainChar.charX >= 499)
                Background.backgroundX -= Background.backgroundVelocity;
        } else if (Background.backgroundX == 0 && MainChar.charX < 10) {
            MainChar.charX = 11;
        }

        if (Items.lives == 0) {
            menunumber =4;
        }
    }

    // All code for startScreen.
    private void startScreen() {
        Gdx.input.setInputProcessor(Background.input);

        ScreenUtils.clear(0, 0.2f, 0, 1);

        Background.leaderboard.reset();

        batch.begin();

        if (InputProcess.buttonClick && InputProcess.buttonNumber == 1) {
            menunumber = 2;
        }

        if (InputProcess.buttonClick && InputProcess.buttonNumber == 2) {
            menunumber = 5;
        }

        batch.draw(Background.startBackground, Background.backgroundX, 0, Background.screenWidth,
        Background.screenHeight);
        batch.draw(Background.playButton, 600, 240, 350, 180);
        batch.draw(Background.optionsButton, 600, 40, 350, 180);
        writeText("Void Warrior", 75, 500);
        batch.end();
    }

    private void helpScreen() {
        Gdx.input.setInputProcessor(Background.input);

        ScreenUtils.clear(0, 0.2f, 0, 1);

        Background.leaderboard.reset();

        batch.begin();

        if (InputProcess.buttonClick && InputProcess.buttonNumber == 3) {
            menunumber = 2;

        }
        batch.draw(Background.startBackground, Background.backgroundX, 0, Background.screenWidth,
                Background.screenHeight);
        batch.draw(Background.playButton, 50, 40, 350, 180);
        batch.draw(Background.helptext, 15, 230, 1000, 200);
        writeText("Help", 75, 520);
        batch.end();
    }

    /**
     * Method used and called for using a random number generator to spawn enemies
     * randomly once reaching 0, as in one number.
     * @param enemySpawnYes random number generator for utilization in a if statement to spawn enemies.
     */
    private void spawnEnemy(int enemySpawnYes) {
        if (enemySpawnYes < 1) {
            Enemy tempEnemy = new Enemy();
            enemyList.add(tempEnemy);
        }
    }

    // All code from bossScreen
    private void bossScreen() {
        System.out.println(MainChar.charX);
        ScreenUtils.clear(0, 0.2f, 0, 1);


        /*
         * Runs all my function and they each will only run the code if certain
         * variables are true
         * time passed is also counter here and current frame is determined here
         */
        mainChar.loadIdle();
        mainChar.attack();
        mainChar.fall();
        mainChar.jump();
        mainChar.hitAnimation();
        MainChar.timePassed += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = (TextureRegion) MainChar.animation.getKeyFrame(MainChar.timePassed, true);

        if (MainChar.charX <= 2) {
            MainChar.charX = 3;
        } else if (MainChar.charX >= 965) {
            MainChar.charX = 964;
        }

        /*
         * If attack, jump, fall are not true and jump possible is true you are allowed
         * to jump using up arrow
         */
        if (!MainChar.attack1 && !MainChar.jump && !MainChar.fall && MainChar.jumpPossible && !MainChar.hitStun) {
            if (Gdx.input.isKeyPressed(Keys.UP))
                MainChar.jump = true;
            if (Gdx.input.isKeyJustPressed(Keys.UP)) {
                if (!frameReset)
                    frameReset = true;
            }
        }

        /*
         * If the main character is not on the ground junp possible is not true
         */
        if (!MainChar.jump) {
            if (MainChar.charY > groundLevel)
                MainChar.fall = true;
            else {
                MainChar.jumpPossible = true;
                MainChar.fall = false;
            }
        }

         /*
         * If not jumping, falling, hitstun or attacking then you can run left or right
         * using the
         * arrow keys
         */
        if (!MainChar.attack1 && !MainChar.jump && !MainChar.fall && !MainChar.hitStun) {
            if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
                mainChar.run();
                flipVal = 1;
                xOffset = 0;
                // since screen is locked character moves in this
                MainChar.charX += 300 * Gdx.graphics.getDeltaTime();
            } else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
                mainChar.run();
                flipVal = -1;
                xOffset = ((currentFrame.getRegionWidth()) * (5 / 2));
                MainChar.charX -= 300 * Gdx.graphics.getDeltaTime();
            }
        }

         /*
         * Moves the character in the air with out running the run animation
         */
        if (MainChar.jump || MainChar.fall) {
            if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
                flipVal = 1;
                xOffset = 0;
                MainChar.charX += 300 * Gdx.graphics.getDeltaTime();
            } else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
                flipVal = -1;
                xOffset = ((currentFrame.getRegionWidth()) * (5 / 2));
                MainChar.charX -= 300 * Gdx.graphics.getDeltaTime();
            }
        }

        /*
         * Checks if character is jumping or fall if not then you can attck using
         * spacebar
         */
        if (!MainChar.jump && !MainChar.fall) {
            if (Gdx.input.isKeyPressed(Keys.SPACE)) {
                MainChar.attack1 = true;
                if (boss.health == 2) {
                    items.setCoinCount(20);
                    items.initCoins(boss.bossXPos, boss.flip);
                }

                // This ensures the player doesn't deal damage by just holding the spacebar
                if (Gdx.input.isKeyJustPressed(Keys.SPACE) && attackStart) {
                    attackStart = false;
                    // Handles hit detetction for the boss
                    boss.getHit(MainChar.charX, MainChar.charY);
                }

                // Once the player has finished the attack animation it is able to attack again
                if (MainChar.animation.isAnimationFinished(MainChar.timePassed)) {
                    attackStart = true;
                }
            }
        }

        /*
         * If boss is not attacking then able hit is false
         */
        if (!Boss.attack) {
            ableHit = false;
        }
        /*
         * Once the boss finished its attack animation then my chracter is able to be
         * hit and it runs a function to see if character is overlapping the boss
         */
        if (Boss.cleaveAnimation.isAnimationFinished(Boss.stateTime + 2 * (Boss.attackSpeed)) && Boss.attack
                && !ableHit) {
            mainChar.getHit(boss.bossXPos, groundLevel, 276, 333);
            ableHit = true;
            if (!frameReset)
                frameReset = true;
        }

        // Makes sure new animation starts on frame 1
        if (frameReset) {
            MainChar.timePassed = 0.0f;
            frameReset = false;
        }

        // Deals with the boss moving automatically
        boss.move(MainChar.charX);


        // This is the code for the animation of the boss
        Boss.stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion bossFrame = boss.getFrame(Boss.stateTime);

        // Depending on which way the boss is facing it flips the image
        if (boss.flip) {
            if (!bossFrame.isFlipX()) {
                bossFrame.flip(true, false);
            }
        } else {
            if (bossFrame.isFlipX()) {
                bossFrame.flip(true, false);
            }
        }

        // Displays a health bar for the boss
        boss.generateHealthBar();

        currentFrame = (TextureRegion) MainChar.animation.getKeyFrame(MainChar.timePassed, true);


        batch.begin();
        batch.draw(Background.bossFightBackground, Background.backgroundX, 0, Background.screenWidth,
                Background.screenHeight);
        batch.draw(Background.bossFightBackground, Background.backgroundX + Background.screenWidth, 0,
                Background.screenWidth, Background.screenHeight);
        if (boss.health > 0) {
            batch.draw(bossFrame, boss.bossXPos, groundLevel, boss.bossWidth, boss.bossHeight);
        } else {
            items.spawnCoins(boss.bossXPos, batch, MainChar.charX, groundLevel,
                    (currentFrame.getRegionWidth()) * (5 / 2), (currentFrame.getRegionHeight()) * (5 / 2));
        }
        batch.draw(currentFrame, MainChar.charX + xOffset, MainChar.charY,
                ((flipVal) * (currentFrame.getRegionWidth()) * (5 / 2)), (currentFrame.getRegionHeight()) * (5 / 2));
        batch.draw(boss.healthTexture(boss.backgroundBar), boss.healthCoordinates("backgroundX"),
                boss.healthCoordinates("backgroundY"));
        batch.draw(boss.healthTexture(boss.healthBar), boss.healthCoordinates("healthX"),
                boss.healthCoordinates("healthY"));
        items.displayHearts(batch);
        items.displayCoinsCollected(batch);
        batch.end();

        if (MainChar.charX == 964 && boss.death == true) {
            menunumber = 4;
            // Once the player kills the boss then write the coins collected to the text
            // file
            Background.leaderboard.writePoints(items.coinsCollected);
        }
        if (Items.lives == 0) {
            menunumber =  4;
    }
    }

    // All code for endScreen.
    private void endScreen() {

        ScreenUtils.clear(0, 0.2f, 0, 1);

        Background.leaderboard.reset();

        Gdx.input.setInputProcessor(new InputProcessor() {

            
            @Override
            public boolean keyDown(int keycode) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    Background.buttonClick = true;
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    Background.buttonClick = false;
                }
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                // TODO Auto-generated method stub
                return false;
            }

        });


        batch.begin();
        batch.draw(Background.winScreenBackground, Background.backgroundX, 0, Background.screenWidth,
                Background.screenHeight);

        writeText("Game Over!", 75, 530);
        writeText("Leaderboard", 55, 410);

        for (int i = 0; i < 10; i++) {
            String point = Background.leaderboard.readPoints();

            if (point != null) {
                String text = "";
                text += Integer.toString(i + 1);
                text += ". ";
                text += point;
                text += " points";

                writeText(text, 30, 350 - (i * 30));
            } else {
                String text = "";
                text += Integer.toString(i + 1);
                text += ". -------------------------";
                writeText(text, 30, 350 - (i * 30));
            }
        }
        batch.end();


    }

    public void writeText(String text, int size, int y) {
        changeFontSize(size);
        Background.layout.setText(Background.font, text, Color.WHITE, Gdx.graphics.getWidth(), Align.center, true);

        Background.font.draw(batch, text, (Gdx.graphics.getWidth() - Background.layout.width) / 2, y);
    }

    public void changeFontSize(int size) {
        Background.parameter.size = size;
        Background.font = Background.generator.generateFont(Background.parameter);
    }

}
