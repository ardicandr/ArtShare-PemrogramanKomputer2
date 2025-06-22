/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package artshare;

/**
 *
 * @author ACER
 */
import java.util.ArrayList;
import java.util.List;

public class GenericList<T> {
    private List<T> items;

    public GenericList() {
        items = new ArrayList<>();
    }

    public void add(T item) {
        items.add(item);
    }

    public List<T> getAll() {
        return items;
    }

    public T get(int index) {
        return items.get(index);
    }

    public int size() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }
} 
