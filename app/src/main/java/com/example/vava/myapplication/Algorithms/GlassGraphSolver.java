package com.example.vava.myapplication.Algorithms;

import com.example.vava.myapplication.Algorithms.Duga;
import com.example.vava.myapplication.Algorithms.GlassGraph;
import com.example.vava.myapplication.Algorithms.GlassSolution;
import com.example.vava.myapplication.Algorithms.Vert;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Stack;

public class GlassGraphSolver {
    private final List<Vert> verts;
    private final int dim;
    private final List<Integer> dugiVertInd;
    private final List<Duga> dugi;

    public GlassGraphSolver(GlassGraph g) {
        verts = g.getVerts();
        dim = g.getDim();
        dugiVertInd = g.getDugiVertInd();
        dugi = g.getDugi();
    }

    public GlassSolution breadthFirstSearch(Vert destination) {
        if (verts.get(0).getSumm()!=destination.getSumm())
            throw new NoSuchElementException("Sums are different!");

        int destNumber = verts.indexOf(destination);
        if (destNumber < 0) throw new NoSuchElementException("The puzzle can't be solved.");

        int start = 0;
        boolean[] visited = new boolean[verts.size()];
        int[] prev = new int[verts.size()];
        Queue<Integer> qu = new LinkedList<>();
        int[] tempNeighs = getNeighsOf(start);

        for (int i : tempNeighs) {
            prev[i] = start;
            qu.add(i);
        }

        do {
            int tempVert = qu.poll();
            if (!visited[tempVert]) {
                if (tempVert == destNumber) break;
                tempNeighs = getNeighsOf(tempVert);
                for (int i : tempNeighs)
                    if ((!qu.contains(i)) && (!visited[i])) {
                        prev[i] = tempVert;
                        qu.add(i);
                    }
                visited[tempVert] = true;
            }
        } while (!qu.isEmpty());

        // Ответ получен, надо его оформить

        Stack<Integer> pathStack = new Stack<>();
        int retVert = destNumber;
        pathStack.add(destNumber);
        do {
            retVert = prev[retVert];
            pathStack.push(retVert);
        } while (retVert != start);

        int[] vertPath = new int[pathStack.size()];
        for (int i = 0; i < vertPath.length; i++)
            vertPath[i] = pathStack.pop();

        return vertPathToSolution(vertPath);
    }

    private GlassSolution vertPathToSolution(int[] path) {
        int[][] soln = new int[path.length-1][2];

        for (int i = 0; i < (path.length - 1); i ++) {
            int[] temp = whatHappened(verts.get(path[i]), verts.get(path[i + 1]));
            for (int j = 0; j < 2; j++)
                soln[i][j] = temp[j];
        }

        return new GlassSolution(soln);
    }

    private int[] whatHappened(Vert iz, Vert v) {
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                if (i != j) {
                    int firstI = iz.getValue(i);
                    int secondI = v.getValue(i);
                    int firstJ = iz.getValue(j);
                    int secondJ = v.getValue(j);
                    boolean iChanged = (firstI != secondI);
                    boolean jChanged = (firstJ != secondJ);
                    boolean summsOk = ((firstI + firstJ) == (secondI + secondJ));
                    if (iChanged && jChanged && summsOk) {
                        if (firstI > secondI)
                            return new int[]{i, j};
                        else
                            return new int[]{j, i};

                    }
                }
        // Не отстаёт:
        return null;
    }

    private int[] getNeighsOf(int vertIndex) {
        // По сути формирования Array List'а все пути из
        // вершины хранятся рядом, не разбросаны по листу
        int nachIndex = dugiVertInd.get(vertIndex);
        int konechnInd;
        if (vertIndex==(verts.size()-1)) konechnInd = dugi.size();
        else konechnInd = dugiVertInd.get(vertIndex+1);

        int[] otvArr = new int[konechnInd - nachIndex];
        int index=0;

        for (int i = nachIndex; i < konechnInd; i++)
            otvArr[index++] = dugi.get(i).getValue(1);

        return otvArr;
    }
}
