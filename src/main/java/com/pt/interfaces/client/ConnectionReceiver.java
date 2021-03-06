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
package com.pt.interfaces.client;

import com.pt.exceptions.ConflictMessageID;
import com.pt.utils.Pointer;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public abstract class ConnectionReceiver extends Thread {

    private final IResponseCallback defaultHandler;

    private final Map<Integer, IResponseCallback> customHandlers;

    public ConnectionReceiver(IResponseCallback handler) {
        super("ConnectionReceiver");
        this.defaultHandler = handler;
        customHandlers = new HashMap<>();
    }

    public void addCustomHanlder(int messageID, IResponseCallback customHandler) {
        if (!customHandlers.containsKey(messageID)) {
            customHandlers.put(messageID, customHandler);
        } else {
            throw new ConflictMessageID(messageID);
        }
    }

    @Override
    public void run() {
        //System.out.println("Client receiver strat");
        

        while (condition()) {
            
            Pointer<InputStream> pointerResponse = new Pointer<>(); 
            int messageID = waitResponse(pointerResponse);
            System.out.println("INPUT STREAM: " + pointerResponse.value + " id " + messageID);
            if (messageID==-1 && pointerResponse.value==null){
                //log the error TODO
                continue;
            }
            try{
            if (customHandlers.containsKey(messageID)) {
                //remove and use the handler
                System.out.println("custom handler");
                customHandlers.remove(messageID).handlerResponse(pointerResponse.value);
            } else {
                defaultHandler.handlerResponse(pointerResponse.value);
            }
            }catch(Exception e){
                e.printStackTrace();//CHANGE TODO
            }
        }

    }

    public abstract boolean condition();

    /**
     * 
     * @param responseServer "Pointer.value" shlould be equal to the InputStream corresponding to
     * the server response.
     * @return 
     */
    public abstract int waitResponse(Pointer<InputStream> responseServer);

}
