package com.zjt9363.service;


import com.zjt9363.service.utils.Pair;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Jiantong Zhang
 */

@Service
public class NetConfig {

    private String fileName;
    private String lossFunction;
    private int numClasses;
    private Pair<String,HashMap<String ,String>> optimizer;
    private ArrayList<Pair<String, HashMap<String,String>>> layerList;
    private String batchSize;
    private String earlyStopping;
    private HashMap <String,String> earlyStoppingOptions;
    private String epochs;

    public String getEpochs() {
        return epochs;
    }

    public void setEpochs(String epochs) {
        this.epochs = epochs;
    }

    public String getEarlyStopping() {
        return earlyStopping;
    }

    public void setEarlyStopping(String earlyStopping) {
        this.earlyStopping = earlyStopping;
    }

    public HashMap<String, String> getEarlyStoppingOptions() {
        return earlyStoppingOptions;
    }

    public void setEarlyStoppingOptions(HashMap<String, String> earlyStoppingOptions) {
        this.earlyStoppingOptions = earlyStoppingOptions;
    }

    public Pair<String, HashMap<String, String>> getOptimizer() {
        return optimizer;
    }

    public void setOptimizer(Pair<String, HashMap<String, String>> optimizer) {
        this.optimizer = optimizer;
    }

    public ArrayList<Pair<String, HashMap<String, String>>> getLayerList() {
        return layerList;
    }

    public void setLayerList(ArrayList<Pair<String, HashMap<String, String>>> layerList) {
        this.layerList = layerList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    public void setLossFunction(String lossFunction) {
        this.lossFunction = lossFunction;
    }

    public int getNumClasses() {
        return numClasses;
    }

    public void setNumClasses(int numClasses) {
        this.numClasses = numClasses;
    }

    @Override
    public String toString() {
        return "NetConfig{" +
                "fileName='" + fileName + '\'' +
                ", lossFunction=" + lossFunction +
                ", numClasses=" + numClasses +
                '}';
    }

    public void cleaner(){
        fileName = null;
        lossFunction = null;
        numClasses = 0;
        if (layerList !=null) {
            layerList.clear();
        }
        if (earlyStoppingOptions!=null){
            earlyStoppingOptions.clear();
        }
    }

    public String getLossFunction() {
        return lossFunction;
    }
}
