package com.bot2shop.model;

public class Topic {

    private static int nextId = 0; // autoincrement next identifier of Phrase for Hashtable
    public Integer id = nextId++; // autoincrement identifier of Phrase for Hashtable

    public String shortName;
    public String name;

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return id == ((Phrase) o).id;
    } // for Hashtable

    public int hashCode() { // for Hashtable
        return id;
    } // for Hashtable

}
