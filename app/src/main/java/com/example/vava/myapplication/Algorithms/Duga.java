package com.example.vava.myapplication.Algorithms;

public class Duga {
    private final int[] otIdo;

    Duga(int[] values) {
        otIdo = new int[2];
        System.arraycopy( values, 0, otIdo, 0, values.length );
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
        Duga e = (Duga) o;
        // В рамках графа сравнение размеров массивов не имеет смысла
        for (int i = 0; i < 2; i++)
            if (otIdo[i] != e.getValue(i)) return false;
        return true;
    }

    public int getValue(int i) {
        // Не хочется проверять принадлежность индекса
        return otIdo[i];
    }

    public boolean startsAt (int index){
        return (otIdo[0]==index);
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + otIdo[0];
        result = prime * result + otIdo[1];
        return result;
    }
}
