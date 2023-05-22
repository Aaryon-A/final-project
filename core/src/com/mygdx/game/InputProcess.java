package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;

public class InputProcess implements InputProcessor {

    static boolean buttonClick = false;
    boolean inButton = false;
    static int xPos = 0;
    static int yPos = 0;
    static int width = 0;
    static int height = 0;

    static ArrayList<Rectangle> buttons = new ArrayList<>();

    static int buttonNumber = 0;

    public static void setButtons(Rectangle rect) {
        buttons.add(rect);
    }

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
        if(button == Input.Buttons.LEFT && inButton) {
            buttonClick = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(button == Input.Buttons.LEFT && inButton) {
            buttonClick = false;
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
        for(int i = 0; i < buttons.size(); i++) {
            xPos = (int) buttons.get(i).x;
            yPos = (int) buttons.get(i).y;
            width = (int) buttons.get(i).width;
            height = (int) buttons.get(i).height;
            if(screenX >= xPos && screenX <= xPos + width && screenY <= (Gdx.graphics.getHeight() - yPos) && screenY >= (Gdx.graphics.getHeight() - yPos - height)) {
                inButton = true;
                buttonNumber = i+1;
            } else {
                if((buttonNumber == 1 && i == 0) || (buttonNumber == 2 && i == 1)) {
                    inButton = false;
                }
            }
        }
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // TODO Auto-generated method stub
        return false;
    }

}