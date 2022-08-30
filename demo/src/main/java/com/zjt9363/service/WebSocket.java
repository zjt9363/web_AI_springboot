package com.zjt9363.service;

import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Jiantong Zhang
 */

@Component
@ServerEndpoint("/websocket")
public class WebSocket {
    private Session session;

    private  static CopyOnWriteArrayList<WebSocket> webSockets = new CopyOnWriteArrayList<>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSockets.add(this);
        //System.out.println("connect one, all :" + webSockets.size());
    }

    @OnClose
    public void onClose() {
        webSockets.remove(this);

    }

    @OnMessage
    public void onMessage(String msg) {
        System.out.println("message: " + msg);
        for (WebSocket webSocket:webSockets) {
            try {
                webSocket.session.getBasicRemote().sendText(msg+"\n");
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
