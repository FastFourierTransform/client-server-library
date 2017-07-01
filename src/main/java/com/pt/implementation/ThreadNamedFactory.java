/*
MIT License

Copyright (c) 2017 Tiago Almeida

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.pt.implementation;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class ThreadNamedFactory implements ThreadFactory{

    private final AtomicInteger couter;
    private final String namePattern;
    
    /**
     * Name pattern must have "%d" in the string
     * Example: "NewThreadName - %d" 
     * 
     * @param namePattern 
     */
    public ThreadNamedFactory(String namePattern){
        this.couter = new AtomicInteger(1);
        this.namePattern = namePattern;
    }
    
    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, String.format(namePattern, couter.getAndIncrement()));
    }
    
}