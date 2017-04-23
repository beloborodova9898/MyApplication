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

    Game3Stakana(int[] max, int[] nach) {
        glasses = new Glass[max.length];
        for (int i = 0; i < glasses.length; i++)
            glasses[i] = new Glass(max[i], nach[i]);
        defaultValues = nach;
    }

    public void transfuse(int iz, int v) {
        if ((glasses[iz].isEmpty()) || (glasses[v].isFull())) return;

        int nadoVv = glasses[v].getFreeVolume();

        if (glasses[iz].getCurrentValue() <= nadoVv) {
            glasses[v].fill(glasses[iz].getCurrentValue());
            glasses[iz].makeEmpty();
        } else {
            glasses[v].makeFull();
            glasses[iz].take(nadoVv);
        }
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
            GlassGraphSolver tempSolver = new GlassGraphSolver(a);
            return tempSolver.breadthFirstSearch(finish);
        } catch (OutOfMemoryError e) {
            return null;
        } catch (NoSuchElementException n) {
            return null;
        }
    }

}
