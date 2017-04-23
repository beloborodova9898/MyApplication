package com.example.vava.myapplication.Algorithms;

public class GlassSolution {
    private int [][] path;

    GlassSolution(int [][] mass) {
        path = mass;
    }

    public String toString() {
        String result="";
        result += "Вот как надо переливать:\n";

        int counter = 1;
        for (int i=0; i<path.length; i++) {
            result += counter+") "+path[i][0] +" -> "+path[i][1]+'\n';
            counter++;
        }

        return result;
    }
}
