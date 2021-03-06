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
package com.pt.implementation.server;

import com.pt.interfaces.server.ThreadRequestServer;
import com.pt.interfaces.server.ThreadConnectionServer;
import com.pt.exceptions.ServerAlreadyUsePort;
import java.util.HashMap;
import java.util.Map;
import com.pt.interfaces.server.IServer;
import com.pt.interfaces.server.IHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class Server implements IServer{
    
    private final Map<Integer,ThreadConnectionServer> connectionThreads;
    private final Map<Integer,ThreadRequestServer> requestThreads;
    private final ExecutorService connectionThreadPool;
    private final ExecutorService requestThreadPool;
    
    public Server(int nThreadsHandleConnectionsMessage,int nThreadsHandleRequestMessage){
        connectionThreads = new HashMap<>();
        requestThreads = new HashMap<>();
        //Maybe only initilizate threadpool if needed
        connectionThreadPool = Executors.newFixedThreadPool(nThreadsHandleConnectionsMessage,new ThreadNamedFactory("ThreadConnectionHandler - %d"));
        requestThreadPool = Executors.newFixedThreadPool(nThreadsHandleRequestMessage,new ThreadNamedFactory("ThreadRequestHandler - %d"));
    }
 
    @Override
    public void startListningConnections(int port, IHandler messageHandler) throws ServerAlreadyUsePort{
            
        if (!connectionThreads.containsKey(port)){
            connectionThreads.put(port, new ThreadConnectionServerTCP(port,messageHandler,connectionThreadPool));
            connectionThreads.get(port).start();
        }else{
            throw new ServerAlreadyUsePort(port);
        }
    }
    
    /**
     * Launch one thread to start listning for clients connections
     * Allow the user to change the default implementation of the server
     * 
     * @param port
     * @param connectionServerImplementation
     * @throws ServerAlreadyUsePort 
     */
    public void startListningConnections(ThreadConnectionServer connectionServerImplementation) throws ServerAlreadyUsePort{
        if (!connectionThreads.containsKey(connectionServerImplementation.getPort())){
            connectionThreads.put(connectionServerImplementation.getPort(), connectionServerImplementation);
            connectionServerImplementation.setPool(connectionThreadPool);
            connectionThreads.get(connectionServerImplementation.getPort()).start();
        }else{
            throw new ServerAlreadyUsePort(connectionServerImplementation.getPort());
        }
    }

    /**
     * Interrupt the running thread, if no thread is running in that port, this method dont do nothing
     * 
     * @param port 
     */
    @Override
    public void stopListningConnections(int port) {
        if (connectionThreads.containsKey(port) && connectionThreads.get(port).isAlive() && !connectionThreads.get(port).isInterrupted())
            connectionThreads.get(port).interrupt();
    }

    @Override
    public void startListningRequests(int port, IHandler messageHandler) throws ServerAlreadyUsePort {
        if (!requestThreads.containsKey(port)){
            requestThreads.put(port, new ThreadRequestServerTCP(port,messageHandler,requestThreadPool));
            requestThreads.get(port).start();
        }else{
            throw new ServerAlreadyUsePort(port);
        }
    }

    @Override
    public void stopListningRequests(int port) {
        if (requestThreads.containsKey(port) && requestThreads.get(port).isAlive() && !requestThreads.get(port).isInterrupted())
            requestThreads.get(port).interrupt();
    }

    @Override
    public void stopAllConnectionsListning() {
        connectionThreads.forEach((k,v)->{if (!v.isInterrupted())v.interrupt();});
    }

    @Override
    public void stopAllRequestListning() {
        requestThreads.forEach((k,v)->{if (!v.isInterrupted())v.interrupt();});
    }

    @Override
    public void shutdown() {
        stopAllConnectionsListning();
        stopAllRequestListning();
    }
    
    
    
}
