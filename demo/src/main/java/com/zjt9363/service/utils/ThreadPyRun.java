package com.zjt9363.service.utils;

import com.zjt9363.service.PyRun;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;

/**
 * @author Jiantong Zhang
 */
public class ThreadPyRun extends Thread {
    private PyRun pyRun;
    private String pyFilePath;

    public ThreadPyRun(PyRun pyRun, String pyFilePath) {
        this.pyRun = pyRun;
        this.pyFilePath = pyFilePath;
    }

    @Override
    public void run() {

        try {
            pyRun.run(pyFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
