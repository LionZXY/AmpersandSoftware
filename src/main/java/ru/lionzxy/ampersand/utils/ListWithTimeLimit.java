package ru.lionzxy.ampersand.utils;

import com.sun.tools.javac.util.Pair;

import java.util.LinkedList;

public class ListWithTimeLimit<T extends Comparable> extends LinkedList<Pair<Long, T>> {
    private final long timeLimit;

    public ListWithTimeLimit(long timeLimit) {
        super();
        this.timeLimit = timeLimit;
    }

    public void add(T element, long timestamp) {
        if (isEmpty()) {
            add(new Pair<>(timestamp, element));
            return;
        }

        long diff = timestamp - getFirst().fst;
        while (diff > timeLimit) {
            removeFirst();
            if (isEmpty()) {
                break;
            }
            diff = timestamp - getFirst().fst;
        }

        add(new Pair<>(timestamp, element));
    }
}
