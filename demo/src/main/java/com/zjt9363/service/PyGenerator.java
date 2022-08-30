package com.zjt9363.service;


import com.zjt9363.service.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @author Jiantong Zhang
 */

@Service
public class PyGenerator {

    @Autowired
    NetComponent netComponent;


    void fileGenerator(byte[] bytes) throws IOException {
        String path = ResourceUtils.getURL("classpath:").getPath();
        OutputStream outputStream;
        if (path.contains("SelfStudy")){
            outputStream = new FileOutputStream(path+"../../main.py");
        }
        else {
            outputStream = new FileOutputStream("./main.py");
        }

   /*     OutputStream outputStream = new FileOutputStream("./main.py");*/

        try {
            System.out.println("File Generate Success");
            outputStream.write(bytes);
        } catch (IOException e){
            System.out.println("File Generate False");
            e.printStackTrace();
        } finally {
            outputStream.close();
        }
    }



    public void codeGenerator(NetConfig netConfig) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {



        StringBuilder stringBuilder = new StringBuilder();

        String categoricalCrossEntropy = "categorical_crossentropy";

        stringBuilder.append(netComponent.importPackage());
        stringBuilder.append(netComponent.gpuConfig());
        stringBuilder.append(netComponent.loadData(netConfig.getFileName()));
        stringBuilder.append(netComponent.epochs(netConfig.getEpochs()));
        stringBuilder.append(netComponent.init(netConfig.getBatchSize()));
        if (categoricalCrossEntropy.equals(netConfig.getLossFunction())){
            stringBuilder.append(netComponent.toCategorical(netConfig));
        }
        stringBuilder.append(netComponent.netBuild());

        Boolean flag = true;


        for (Pair<String, HashMap<String,String>> pair : netConfig.getLayerList()){
            if (flag){
                pair.snd.put("input_shape","input_shape");
                flag = false;
            }
            stringBuilder.append(netComponent.addLayer(pair.fst, pair.snd));
        }

        stringBuilder.append(netComponent.summary());
        stringBuilder.append(netComponent.optimizer(netConfig));
        stringBuilder.append(netComponent.compile(netConfig.getLossFunction()));
        stringBuilder.append(netComponent.modelSave());
        if ("true".equals(netConfig.getEarlyStopping()) ) {
            stringBuilder.append(netComponent.earlyStopping(netConfig));
        }
        stringBuilder.append(netComponent.fit(netConfig));
        stringBuilder.append(netComponent.evaluate());
        stringBuilder.append(netComponent.print());
        stringBuilder.append(netComponent.plot());


        String data = stringBuilder.toString();

        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);


        fileGenerator(bytes);
    }
}
