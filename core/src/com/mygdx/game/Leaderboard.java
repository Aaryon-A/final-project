package com.mygdx.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This is the class that handles the logic of writing the points to a text file and ordering it from greatest to least
 */
public class Leaderboard {
    private BufferedReader br;
    private BufferedWriter bw;

    private ArrayList<Integer> orderedPoints;

    /**
     * This initializes the leaderboard by reading the text file and adding all the points to a list
     */
    public Leaderboard() {
        reset();
        orderedPoints = new ArrayList<>();

        String line;
        try {
            while((line = br.readLine()) != null) {
                // This converts each String into an int
                orderedPoints.add(Integer.parseInt(line));
            }
        } catch (NumberFormatException | IOException e) {
        }

        // This sorts the points from least to greatest
        Collections.sort(orderedPoints);
    }

    /**
     * This resets the BufferedReader so it can read the file from the start
     */
    public void reset() {
        try {
            br = new BufferedReader(new FileReader("points.txt"));
        } catch(FileNotFoundException e) {
        }
    }

    /**
     * This adds a new point value to the list and sorts it from greatest to least
     * It then writes each point into the text file
     * @param point the point value to be added to the leaderboard
     */
    public void writePoints(int point) {
        try {
            bw = new BufferedWriter(new FileWriter("points.txt"));
            orderedPoints.add(point);
            Collections.sort(orderedPoints);

            // This line sorts it from greatest to least by simply reversing the order
            Collections.reverse(orderedPoints);

            for(int i = 0; i < orderedPoints.size(); i++) {
                // This converts each int into a String
                bw.write(Integer.toString(orderedPoints.get(i)));
                if(i < orderedPoints.size()-1) {
                    bw.newLine();
                }
            }
            bw.close();
        } catch (IOException e) {
        }
    }

    /**
     * This reads a line of the file and returns the String
     * @return the String read from the file
     */
    public String readPoints() {
        try {
            return br.readLine();
        } catch(FileNotFoundException e) {
        } catch (IOException e) {
        }

        return "";
    }
}
