package com.example.vava.myapplication;

import com.example.vava.myapplication.Algorithms.Glass;
import com.example.vava.myapplication.Algorithms.GlassGraph;
import com.example.vava.myapplication.Algorithms.GlassGraphSolver;
import com.example.vava.myapplication.Algorithms.GlassSolution;
import com.example.vava.myapplication.Algorithms.Vert;

import java.util.NoSuchElementException;
import java.util.Random;

// Интерактивный класс для игры с 3 стаканами

public class Game3Stakana {
    private Glass [] glasses;
    private Vert finish;
    public static int numberOfGlasses = 3;

    public Game3Stakana(int[] data) {
        glasses = new Glass[numberOfGlasses];
        for (int i = 0; i < numberOfGlasses; i++)
            glasses[i] = new Glass(data[i], data[numberOfGlasses + i]);

        int[] tempDataForVert = new int [numberOfGlasses];
        System.arraycopy(data, numberOfGlasses * 2, tempDataForVert, 0, numberOfGlasses);
        finish = new Vert(tempDataForVert);

    }

    public Game3Stakana(boolean isDifficult) {
        int min = 2;
        int max = 14;

        if(isDifficult) {
            min = 3;
            max = 30;
        }

        Random r = new Random();

        glasses = new Glass[numberOfGlasses];
        for (int i = 0; i < numberOfGlasses; i++)
        {
            int tempMax = min + r.nextInt(max);
            if (i==0) tempMax /= 2;
            int tempCurr = r.nextInt(tempMax);
            glasses[i] = new Glass(tempMax, tempCurr);
        }

        GlassGraph temp = new GlassGraph(glasses, 0);
        finish = temp.getRandomVert();
    }

    public boolean transfuse(int iz, int v) {
        if ((glasses[iz].isEmpty()) || (glasses[v].isFull())) return false;

        int nadoVv = glasses[v].getFreeVolume();

        if (glasses[iz].getCurrentValue() <= nadoVv) {
            glasses[v].fill(glasses[iz].getCurrentValue());
            glasses[iz].makeEmpty();
        } else {
            glasses[v].makeFull();
            glasses[iz].take(nadoVv);
        }

        return  (currState().equals(finish));
    }

    private Vert currState() {
        return new Vert(new int[]{glasses[0].getCurrentValue(), glasses[1].getCurrentValue(), glasses[2].getCurrentValue()});
    }

    public String toString()
    {
        String toPrint="";
        for(int i=0; i<glasses.length;i++)
            toPrint+=(glasses[i].getCurrentValue()+"("+glasses[i].getMaxValue()+") ");

        return toPrint;
    }

    public GlassSolution solve(Vert finish, int maxDiff) {
        try {
            GlassGraph a = new GlassGraph(glasses, maxDiff);
            return GlassGraphSolver.breadthFirstSearch(a, finish);
        } catch (OutOfMemoryError e) {
            return null;
        } catch (NoSuchElementException n) {
            return null;
        }
    }

    public String sostStakana(int i) {
        String result="";
        result += Integer.toString(glasses[i].getCurrentValue());
        result += " (";
        result += Integer.toString(glasses[i].getMaxValue());
        result += ")";
        return result;
    }

    public Glass[] getGlasses() {
        return glasses;
    }

    public Vert getFinish() {
        return finish;
    }

    public int getState(int i) {
        int tempC = glasses[i].getCurrentValue();
        int tempM = glasses[i].getMaxValue();
        if (tempC == 0) return 0;
        if (tempC == tempM) return 3;
        if (tempC < tempM/2) return 1;
        return 2;
    }

    public int[] toIntArray() {
        int[] result = new int[numberOfGlasses*3];
        for (int i=0; i<numberOfGlasses; i++)
            result [i] = glasses[i].getMaxValue();
        for (int i=numberOfGlasses; i<numberOfGlasses*2; i++)
            result[i] = glasses[i-numberOfGlasses].getCurrentValue();
        for (int i=numberOfGlasses*2; i<numberOfGlasses*3; i++)
            result[i] = finish.getValue(i-numberOfGlasses*2);

        return result;
    }

}
