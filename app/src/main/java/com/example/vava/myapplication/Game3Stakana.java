package com.example.vava.myapplication;

import com.example.vava.myapplication.Algorithms.Glass;
import com.example.vava.myapplication.Algorithms.GlassGraph;
import com.example.vava.myapplication.Algorithms.GlassGraphSolver;
import com.example.vava.myapplication.Algorithms.GlassSolution;
import com.example.vava.myapplication.Algorithms.Vert;

import java.util.NoSuchElementException;

// Интерактивный класс для игры с 3 стаканами

public class Game3Stakana {
    private Glass [] glasses;
    private final int [] defaultValues;
    private Vert finish;

    public Game3Stakana(int[] data) {
        glasses = new Glass[3];
        for (int i = 0; i < 3; i++)
            glasses[i] = new Glass(data[i], data[i+3]);

        defaultValues = new int[3];
        for (int i = 0; i < 3; i++)
            defaultValues[i] = data[i+3];

        finish = new Vert(new int[]{data[6], data[7], data[8]});

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

    public void restartGame()
    {
        for (int i=0; i<glasses.length; i++)
            glasses[i].setCurrentValue(defaultValues[i]);
    }

    public GlassSolution solve(Vert finish, int maxDiff) {
        GlassGraph a = null;
        try {
            a = new GlassGraph(glasses, maxDiff);
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
        int diff = tempC - tempM;
        if (tempC == 0) return 0;
        if (tempC == tempM) return 3;
        if (tempC < tempM/2) return 1;
        return 2;
    }

    public int[] toIntArray() {
        int[] result = new int[9];
        for (int i=0; i<3; i++)
            result [i] = glasses[i].getMaxValue();
        for (int i=3; i<6; i++)
            result[i] = glasses[i-3].getCurrentValue();
        for (int i=6; i<9; i++)
            result[i] = finish.getValue(i-6);

        return result;
    }

}
