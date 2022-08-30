package com.zjt9363.service;

/*import socket.WebSocket;*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.io.*;

/**
 * @author Jiantong Zhang
 */

@Component
public class PyRun{
    boolean count;

    @Autowired
    WebSocket webSocket;


     public void run(String fileName) throws FileNotFoundException {

         boolean interrupt = false;

        // TODO Auto-generated method stub

        try {
/*            WebSocket webSocket = new WebSocket();*/
            Process proc = Runtime.getRuntime().exec(("python " + fileName));

            count = true;
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            Boolean flag = true;
            while ((line = in.readLine()) != null) {
                if (Thread.interrupted()){
                    interrupt = true;
                    break;
                }

                webSocket.onMessage(line);
                if (line.contains("Test accuracy")){
                    flag=false;
                }
            }
            if (flag && !interrupt) {
                BufferedReader error = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
                line = null;
                while ((line = error.readLine()) != null) {
                    webSocket.onMessage(line);
                }
            }

            in.close();
            proc.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
     }

}
