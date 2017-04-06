package com.example.vava.myapplication;

public class GlassSolution {
    private int [][] path;

    GlassSolution(int [][] mass) {
        path = mass;
    }

    public void printPath() {
        System.out.println("Вот как надо переливать: ");
        int counter = 1;
        for (int i=0; i<path.length; i++) {
            System.out.println(counter+") "+path[i][0] +" -> "+path[i][1]);
            counter++;
        }
    }
}
