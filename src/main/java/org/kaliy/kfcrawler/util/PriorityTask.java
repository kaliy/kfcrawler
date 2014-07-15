package org.kaliy.kfcrawler.util;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class PriorityTask<T> extends FutureTask<T> implements Comparable<PriorityTask> {

    private PriorityCallable<T> callable;

    @SuppressWarnings("unchecked")
    public PriorityTask(Callable<T> callable) {
        super(callable);
        this.callable = (PriorityCallable<T>)callable;
    }

    public PriorityCallable<T> getCallable() {
        return callable;
    }

    @Override
    public int compareTo(PriorityTask o) {
        return callable.compareTo(o.callable);
    }
}
