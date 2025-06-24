package ru.lionzxy.ampersand.utils;


import kotlin.Pair;
import kotlin.jvm.Synchronized;

import java.lang.reflect.Array;
import java.util.*;

public class ListWithTimeLimit<T> {
    private LinkedList<Pair<Long, T>> delegate = new LinkedList<>();

    private final long timeLimit;

    public ListWithTimeLimit(long timeLimit) {
        super();
        this.timeLimit = timeLimit;
    }

    @Synchronized
    public void add(T element, long timestamp) {
        if (delegate.isEmpty()) {
            delegate.add(new Pair<>(timestamp, element));
            return;
        }

        long diff = timestamp - delegate.getFirst().getFirst();
        while (diff > timeLimit) {
            delegate.removeFirst();
            if (delegate.isEmpty()) {
                break;
            }
            diff = timestamp - delegate.getFirst().getFirst();
        }

        delegate.add(new Pair<>(timestamp, element));
    }

    @Synchronized
    public List<Pair<Long, T>> threadSafeSnapshot() {
        Object[] snapshotArray = delegate.toArray();
        List<Pair<Long, T>> newList = new ArrayList<>(snapshotArray.length);
        for (Object element : snapshotArray) {
            newList.add((Pair<Long, T>) element);
        }
        return newList;
    }
}
