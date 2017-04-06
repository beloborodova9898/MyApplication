package com.example.vava.myapplication;

import java.util.NoSuchElementException;

// Интерактивный класс для игры с 3 стаканами

public class Game3Stakana {
    private int [] maxV;
    private int [] nachZnach;
    private int [] currentV;

    Game3Stakana(int[] max, int[] nach) {
        maxV = max;
        nachZnach = nach;
        currentV = nachZnach.clone();
    }

    public void perelit(int iz, int v) {
        if ((currentV[iz] == 0) || (currentV[v] == maxV[v])) return;

        int nadoVv = maxV[v] - currentV[iz];

        if (currentV[iz] <= nadoVv) {
            currentV[v] += currentV[iz];
            currentV[iz] = 0;
        } else {
            currentV[v] = maxV[v];
            currentV[iz] -= nadoVv;
        }
    }

    public void printSostoyanie()
    {
        String toPrint="";
        for(int i=0; i<maxV.length;i++)
            toPrint+=(currentV[i]+"("+maxV[i]+") ");

        System.out.println(toPrint);
    }

    public void restartGame()
    {
        for (int i=0; i<maxV.length; i++)
            currentV[i]=nachZnach[i];
    }

    public GlassSolution solve(Vert finish, int maxDiff) {
        GlassGraph a = null;
        try {
            a = new GlassGraph(maxV, new Vert(currentV), maxDiff);
            GlassSolution sol = a.breadthFirstSearch(finish);
            return sol;
        } catch (OutOfMemoryError e) {
            return null;
        } catch (NoSuchElementException n) {
            return null;
        }
    }

}
