package com.example.vava.myapplication;

public class Vert {
    private int mass[];

    Vert(int[] values) {
        mass = new int[values.length];
        for (int i = 0; i < values.length; i++)
            mass[i] = values[i];
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

    public String vertToString() {
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
}
