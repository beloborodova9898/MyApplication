package com.example.vava.myapplication.Algorithms;

import java.util.Arrays;

public class Vert {
    private final int mass[];

    public Vert(int[] values) {
        mass = new int[values.length];
        System.arraycopy( values, 0, mass, 0, values.length );
    }

    public int getSumm() {
        int otvet = 0;
        for (int i : mass) otvet += i;
        return otvet;
    }

    public int getValue(int i) {
        // Не хочется проверять принадлежность индекса
        return mass[i];
    }

    public String toString() {
        String otvet = "";
        for (int i : mass) otvet += (i + "-");
        return otvet.substring(0, otvet.length() - 1);
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        Vert e = (Vert) o;
        // В рамках графа сравнение размеров массивов не имеет смысла
        for (int i = 0; i < mass.length; i++)
            if (mass[i] != e.getValue(i)) return false;
        return true;
    }

    public int hashCode() {
       return Arrays.hashCode(mass);
    }
}
