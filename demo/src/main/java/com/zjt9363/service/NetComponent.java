package com.zjt9363.service;




import com.zjt9363.service.utils.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;


import java.util.HashMap;

/**
 * @author Jiantong Zhang
 */

@Service
@PropertySource(value = "classpath:pyCodeProp.properties",ignoreResourceNotFound = true)
public class NetComponent {

    @Value("${importPackage}")
    String importPackage;
    @Value("${gpuConfiguration}")
    String gpuConfiguration;
    @Value("${loadFile}")
    String loadFile;
    @Value("${init}")
    String init;
    @Value("${toCategorical}")
    String toCategorical;
    @Value("${addModel}")
    String addModel;
    @Value("${netBuild}")
    String netBuild;
    @Value("${summary}")
    String summary;
    @Value("${earlyStopping}")
    String earlyStopping;
    @Value("${fit}")
    String fit;
    @Value("${evaluate}")
    String evaluate;
    @Value("${print}")
    String print;
    @Value("${compile}")
    String compile;
    @Value("${numClasses}")
    String numClasses;
    @Value("${modelSave}")
    String modelSave;
    @Value("${optimizer}")
    String optimizer;
    @Value("${batchSize}")
    String batchSize;
    @Value("${plot}")
    String plot;
    @Value("${epochs}")
    String epochs;

    public String epochs(String s){
        return epochs + s + "\n";
    }

    public String importPackage(){
        return importPackage;
    }

    public String gpuConfig(){
        return gpuConfiguration;
    }

    public String loadData(String s){

        return String.format(loadFile, s);
    }

    public String init(String s){
        String sReturn = init + batchSize + s + "\n";
        return sReturn;
    }

    public String toCategorical(NetConfig netConfig){

        return  String.format(numClasses,netConfig.getNumClasses()) + toCategorical;
    }

    public String addLayer(String functionName, HashMap<String,String> m){
        return addModel + functionName + "(" + Util.layerParameterToString(m) + "))\n";
    }

    public String modelSave(){
        return modelSave;
    }


    public String optimizer(NetConfig netConfig){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(optimizer);
        HashMap<String ,String > m = netConfig.getOptimizer().snd;
        stringBuilder.append(netConfig.getOptimizer().fst + "(" + Util.layerParameterToString(m) + ")\n");

        return stringBuilder.toString();
    }

    public String plot(){
        return plot;
    }


    public String netBuild(){
        return netBuild;
    }

    public String summary() {
        return summary;
    }

    public String earlyStopping(NetConfig netConfig) {
        return earlyStopping + Util.layerParameterToString(netConfig.getEarlyStoppingOptions()) + ")\n\n";
    }

    public String fit(NetConfig netConfig) {
        StringBuilder sResult = new StringBuilder(fit);
        if ("true".equals(netConfig.getEarlyStopping()) ) {
            sResult.append(",callbacks=[early_stopping]");
        }
        sResult.append(")\n\n");

        return sResult.toString();
    }

    public String evaluate() {
        return evaluate;
    }

    public String print() {
        return print;
    }

    public String compile(String s) {
        StringBuilder stringBuilder = new StringBuilder(compile);
        stringBuilder.insert(20,s);
        return stringBuilder.toString();
    }

    public String getImportPackage() {
        return importPackage;
    }

    public void setImportPackage(String importPackage) {
        this.importPackage = importPackage;
    }

    public String getGpuConfiguration() {
        return gpuConfiguration;
    }

    public void setGpuConfiguration(String gpuConfiguration) {
        this.gpuConfiguration = gpuConfiguration;
    }

    public String getLoadFile() {
        return loadFile;
    }

    public void setLoadFile(String loadFile) {
        this.loadFile = loadFile;
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public String getToCategorical() {
        return toCategorical;
    }

    public void setToCategorical(String toCategorical) {
        this.toCategorical = toCategorical;
    }

    public String getAddModel() {
        return addModel;
    }

    public void setAddModel(String addModel) {
        this.addModel = addModel;
    }

    public String getNetBuild() {
        return netBuild;
    }

    public void setNetBuild(String netBuild) {
        this.netBuild = netBuild;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getEarlyStopping() {
        return earlyStopping;
    }

    public void setEarlyStopping(String earlyStopping) {
        this.earlyStopping = earlyStopping;
    }

    public String getFit() {
        return fit;
    }

    public void setFit(String fit) {
        this.fit = fit;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public String getPrint() {
        return print;
    }

    public void setPrint(String print) {
        this.print = print;
    }

    public String getCompile() {
        return compile;
    }

    public void setCompile(String compile) {
        this.compile = compile;
    }
}
