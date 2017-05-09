package com.example.vava.myapplication.Algorithms;
// Алгоритм в общем виде.
// Может решить любую решаемую задачу со стаканами,
// при этом можно отменить решение, если задача
// оказалась сложнее допустимой (задаётся пользователем).

// Каждая вершина - уникальное состояние
// Каждая дуга является направленным переходом из одного состояния в другое

// Тестами к классу могут являться примеры с ответами,
// но это ненадёжно, пытаюсь придумать что-то лучше.
// (Классические задачи с решениями проверяла).

import java.util.*;

public class GlassGraph {
    private List<Vert> verts;
    private List<Duga> dugi;
    private List<Integer> dugiVertInd;
    // -------------------------------------
    private final int dim;
    private final boolean maxExists;
    private final int maxNumberOfD;

    public GlassGraph(int[] maxV, Vert nachVert, int maxNoD) {
        dim = maxV.length;
        maxNumberOfD = maxNoD;

        maxExists = (maxNoD > 0);

        dugi = new ArrayList<>();
        verts = new ArrayList<>();
        createGraphOtTochki(maxV, nachVert);
    }

    public GlassGraph(Glass[] glasses, int maxNoD) {
        dim = glasses.length;
        maxNumberOfD = maxNoD;

        maxExists = (maxNoD > 0);

        dugi = new ArrayList<>();
        verts = new ArrayList<>();

        int [] maxy = new int[dim];
        for (int i = 0; i < dim; i++)
            maxy[i] = glasses[i].getMaxValue();

        int [] nachValues = new int[dim];
        for (int i = 0; i < dim; i++)
            nachValues[i] = glasses[i].getCurrentValue();

        createGraphOtTochki(maxy, new Vert(nachValues));
    }

    private void createGraphOtTochki(int[] maxV, Vert nachVert) {
        dugiVertInd = new ArrayList<>();
        Queue<Vert> qu = new LinkedList<>();
        qu.add(nachVert);
        verts.add(nachVert);
        int[] sost = new int[dim];
        for (int i = 0; i < dim; i++)
            sost[i] = nachVert.getValue(i);
        // Графу по сути не нужен массив стаканов, потому что граф
        // одноразовый, в случае хоть единого перехода из состояния в состояние,
        // придётся строить новый граф с новой начальной точкой.
        Glass tempGl1;
        Glass tempGl2;

        int counter = 0;

        do {
            Vert firstInQu = qu.poll();
            vertToMass(firstInQu, sost);
            int izSost = verts.indexOf(firstInQu);
            dugiVertInd.add(dugi.size());

            for (int iz = 0; iz < dim; iz++)
            {
                for (int v = 0; v < dim; v++) {
                    if (iz != v) {
                        tempGl1 = new Glass(maxV[iz], sost[iz]);
                        tempGl2 = new Glass(maxV[v], sost[v]);

                        if (canBeTransfused(tempGl1, tempGl2)) {
                            sost[iz] = tempGl1.getCurrentValue();
                            sost[v] = tempGl2.getCurrentValue();
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
                            if (counter == maxNumberOfD)
                                throw new OutOfMemoryError("Max number reached!");
                    }
                }
            }
        } while (!qu.isEmpty());
        System.out.println("Вышло дуг: " + counter);
    }

    private boolean canBeTransfused(Glass first, Glass second) {
        if ((first.isEmpty()) || (second.isFull())) return false;

        int nadoVv = second.getFreeVolume();

        if (first.getCurrentValue() <= nadoVv) {
            second.fill(first.getCurrentValue());
            first.makeEmpty();
        } else {
            second.makeFull();
            first.take(nadoVv);
        }
        return true;
    }

    private void vertToMass(Vert ve, int[] mass) {
        for (int i = 0; i < mass.length; i++)
            mass[i] = ve.getValue(i);
    }

    public String toString() {
        String result="";
        int counter = 0;
        for (Duga i : dugi) {
            String v0 = verts.get(i.getValue(0)).toString();
            String v1 = verts.get(i.getValue(1)).toString();
            result += (counter + ")" + v0 + ' ' + v1 +'\n');
            counter++;
        }
        return result;
    }

    public List<Vert> getVerts() {
        return verts;
    }

    public int getDim() {
        return dim;
    }

    public List<Integer> getDugiVertInd() {
        return dugiVertInd;
    }

    public List<Duga> getDugi() {
        return dugi;
    }

    public Vert getRandomVert() {
        int max = verts.size() - 2;
        Random r = new Random();
        return verts.get(max/2 + r.nextInt(max/2));
    }
}
/*
        Пример использования:

        GlassGraph a = new GlassGraph(new int[]{3,5,10}, new Vert(new int[]{0,5,5}), 200);
        GlassSolution aSolution = GlassGraphSolver.breadthFirstSearch(a, new Vert(new int[]{2,5,3}));
        System.out.printf(aSolution.toString());
 */
