package com.example.vava.myapplication;// Алгоритм в общем виде.
// Может решить любую решаемую задачу со стаканами,
// при этом можно отменить решение, если задача
// оказалась сложнее допустимой (задаётся пользователем).

// Тестами к классу могут являться примеры с ответами,
// но это ненадёжно, пытаюсь придумать что-то лучше.
// (Классические задачи с решениями проверяла).

import java.util.*;

public class GlassGraph {
    private ArrayList<Vert> verts;
    private ArrayList<Duga> dugi;
    private ArrayList<Integer> dugiVertInd;
    // Чтобы был понятен смысл использования:
    private int dugiDim = 2;
    private int nothing = 0;
    private int perviy = 0;
    private int vtoroy = 1;
    // -------------------------------------
    private int dim;
    private boolean maxExists;
    private int maxNumberOfD;

    GlassGraph(int[] maxV, Vert nachVert, int maxNoD) {
        dim = maxV.length;
        if (maxNoD <= 0) maxExists = false;
        else {
            maxNumberOfD = maxNoD;
            maxExists = true;
        }

        dugi = new ArrayList<>();
        verts = new ArrayList<>();
        createGraphOtTochki(maxV, nachVert);
    }

    private void createGraphOtTochki(int[] maxV, Vert nachVert) {
        dugiVertInd = new ArrayList<>();
        Queue<Vert> qu = new LinkedList<>();
        qu.add(nachVert);
        verts.add(nachVert);
        int[] tempMaxy = new int[dugiDim];
        int[] sost = new int[dim];
        for (int i = 0; i < dim; i++)
            sost[i] = nachVert.getValue(i);
        int[] tempSost = new int[dugiDim];
        int counter = 0;

        do {
            Vert firstInQu = qu.poll();
            vertToMass(firstInQu, sost);
            int izSost = verts.indexOf(firstInQu);
            dugiVertInd.add(dugi.size());

            for (int iz = 0; iz < dim; iz++)
            {
                for (int v = 0; v < dim; v++)
                    if (iz != v) {
                        tempMaxy[perviy] = maxV[iz];
                        tempMaxy[vtoroy] = maxV[v];

                        tempSost[perviy] = sost[iz];
                        tempSost[vtoroy] = sost[v];

                        if (mojnoPerelit(tempMaxy, tempSost)) {
                            sost[iz] = tempSost[perviy];
                            sost[v] = tempSost[vtoroy];
                            Vert temp = new Vert(sost);
                            vertToMass(firstInQu, sost);

                            if (!verts.contains(temp)) {
                                verts.add(temp);
                                qu.add(temp);
                            }

                            int vSost = verts.indexOf(temp);
                            Duga tempDuga = new Duga(new int[]{izSost, vSost});

                            if (!dugi.contains(tempDuga)) {
                                dugi.add(tempDuga);
                                counter++;
                            }


                        }
                        if (maxExists)
                            if (counter == maxNumberOfD) throw new OutOfMemoryError("Max number reached!");
                    }
            }
        } while (!qu.isEmpty());
        System.out.println("Вышло дуг: " + counter);
    }

    private boolean mojnoPerelit(int[] maxy, int[] sost) {
        if ((sost[perviy] == nothing) || (sost[vtoroy] == maxy[vtoroy])) return false;

        int nadoVv = maxy[vtoroy] - sost[vtoroy];

        if (sost[perviy] <= nadoVv) {
            sost[vtoroy] += sost[perviy];
            sost[perviy] = nothing;
        } else {
            sost[vtoroy] = maxy[vtoroy];
            sost[perviy] -= nadoVv;
        }
        return true;
    }

    private void vertToMass(Vert ve, int[] mass) {
        for (int i = 0; i < mass.length; i++)
            mass[i] = ve.getValue(i);
    }

    public void printGraph() {
        int dlyaChitabelnosti = 0;
        for (Duga i : dugi) {
            String v0 = verts.get(i.getValue(0)).vertToString();
            String v1 = verts.get(i.getValue(1)).vertToString();
            System.out.println(dlyaChitabelnosti + ")" + v0 + ' ' + v1);
            dlyaChitabelnosti++;
        }
    }

    public GlassSolution breadthFirstSearch(Vert destination) {
        if (verts.get(perviy).getSumm()!=destination.getSumm())
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
        int[][] soln = new int[path.length-1][dugiDim];

        for (int i = 0; i < (path.length - 1); i ++) {
            int[] temp = kakEtoVyshlo(verts.get(path[i]), verts.get(path[i + 1]));
            for (int j = 0; j < dugiDim; j++)
                soln[i][j] = temp[j];
        }

        return new GlassSolution(soln);
    }

    private int[] kakEtoVyshlo(Vert iz, Vert v) {
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
            otvArr[index++] = dugi.get(i).getValue(vtoroy);

        return otvArr;
    }
}
/*
        Пример использования:

            GlassGraph a = new GlassGraph(new int[]{7,13,20}, new Vert(new int[]{0,0,20}), 200);
            GlassSolution sol = a.breadthFirstSearch(new Vert(new int[]{7,8,5}));
            sol.printPath();
 */
