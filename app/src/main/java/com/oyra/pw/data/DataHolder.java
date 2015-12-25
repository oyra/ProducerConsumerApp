package com.oyra.pw.data;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by oyra on 24/12/15.
 */
public class DataHolder {

    public static PriorityBlockingQueue<Unit> sQueue = new PriorityBlockingQueue<>();

    //TODO In real situation we’ll need to clear the queue time to time in some way
    // (removing the low-priority data, dumping data to db) otherwise it may grow and grow
    // (the velocity of growing depends on services’ timeouts and the errors frequency)

    //some queue-cleaning procedures could be here


}
