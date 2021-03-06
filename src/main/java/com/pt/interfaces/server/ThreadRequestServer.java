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
package com.pt.interfaces.server;

import java.util.concurrent.ExecutorService;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public abstract class ThreadRequestServer extends Thread{
    
    protected IHandler messageHandler;
    protected int port;
    protected ExecutorService pool;
    
    public ThreadRequestServer(int port,IHandler mHandler,ExecutorService pool){
        super("RequestServer on "+port);
        this.messageHandler = mHandler;
        this.port = port;
        this.pool = pool;
    }
    
    public ThreadRequestServer(int port,IHandler mHandler,ExecutorService pool,String name){
        super(name);
        this.messageHandler = mHandler;
        this.port = port;
        this.pool = pool;
    }
    
    public int getPort(){
        return port;
    }
    
}
