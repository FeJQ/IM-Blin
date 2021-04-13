package com.fejq.blin.common;

import java.util.LinkedList;

public class MessageQueue<T>
{
    private LinkedList<T> storage = new LinkedList<T>();

    public synchronized void push(T e)
    {
        storage.addFirst(e);
    }

    public synchronized T next()
    {
        if(storage.size()==0)
        {
            return null;
        }
        T first = storage.getFirst();
        pop();
        return first;
    }

    private void pop()
    {
        storage.removeFirst();
    }

    public boolean empty()
    {
        return storage.isEmpty();
    }
}
