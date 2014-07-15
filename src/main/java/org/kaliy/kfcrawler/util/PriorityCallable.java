package org.kaliy.kfcrawler.util;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class PriorityCallable<T> implements Callable<T>, Comparable<PriorityCallable> {

    public static final Integer MIN_PRIORITY = Integer.MIN_VALUE;
    public static final Integer NORMAL_PRIORITY = 0;
    public static final Integer MAX_PRIORITY = Integer.MAX_VALUE;

    public PriorityCallable(Callable<T> callable) {
        this(callable, MIN_PRIORITY);
    }

    public PriorityCallable(Callable<T> callable, Integer priority) {
        this.priority = priority;
        this.callable = callable;
    }

    private Integer priority;
    private Callable<T> callable;

    @Override
    public T call() throws Exception {
        return callable.call();
    }

    @Override
    public int compareTo(PriorityCallable o) {
        return ComparisonChain.start()
            .compare(priority, o.priority, Ordering.natural().reverse().nullsLast())
            .result();
    }

}
