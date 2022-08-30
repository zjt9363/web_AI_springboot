package com.zjt9363.service.utils;


import com.zjt9363.service.NetConfig;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Jiantong Zhang
 */

@Component
@PropertySource("application.properties")
public class Util {


    static String propPathForPyCode = "C:/Users/Zarrow/IdeaProjects/SelfStudy/web_AI_springboot/demo/";
    static String filePropPath = "C:/Users/Zarrow/IdeaProjects/SelfStudy/web_AI_springboot/demo/src/main/resources/fileProp/";


    public static String layerParameterToString(HashMap<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : map.keySet()) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(", ");
            }
            String value = map.get(key);
            if ("kernel_size".equals(key) || "pool_size".equals(key)) {
                value = "(" + value + ")";
            } else if ("activation".equals(key) || "padding".equals(key)) {
                value = "\"" + value + "\"";
            }
            stringBuilder.append(key).append("=").append(value);
        }
        return stringBuilder.toString();
    }

    public static void processFile(ArrayList<HashMap<String, Object>> arrayList, NetConfig netConfig) {
        if (arrayList.get(arrayList.size() - 1).containsKey("file")) {
            String fileName = ((HashMap<String, String>) arrayList.get(arrayList.size() - 1).get("file")).get("name");
            netConfig.setFileName(filePropPath + fileName);
            arrayList.remove(arrayList.size() - 1);
        } else {

        }
        System.out.println("File Load Success");
    }

    public static void processBaseParameter(ArrayList<HashMap<String, Object>> arrayList, NetConfig netConfig) {
        HashMap<String, Object> baseParameter = arrayList.get(0);
        arrayList.remove(0);

        if ("categorical_crossentropy".equals(baseParameter.get("lossFunction"))) {
            netConfig.setNumClasses(Integer.parseInt((String) baseParameter.get("numClasses")));
        }
        netConfig.setLossFunction((String) baseParameter.get("lossFunction"));

        HashMap<String, Object> optimizerHashMap = (HashMap<String, Object>) baseParameter.get("optimizer");

        Pair<String, HashMap<String, String>> optimizer = new Pair<>(new String(), new HashMap<>());

        optimizer.fst = (String) optimizerHashMap.get("value");

        ArrayList<HashMap<String, String>> optimizerParameter = (ArrayList<HashMap<String, String>>) optimizerHashMap.get("child");

        for (HashMap<String, String> hashMap : optimizerParameter) {
            optimizer.snd.put(hashMap.get("type"), hashMap.get("value"));
        }

        netConfig.setOptimizer(optimizer);

        netConfig.setBatchSize((String) baseParameter.get("batchSize"));

        String earlyStoppingString = (String) baseParameter.get("earlyStopping");

        netConfig.setEarlyStopping(earlyStoppingString);

        if ("true".equals(earlyStoppingString)) {
            netConfig.setEpochs("9999");
            HashMap<String, String> earlyStoppingOptions = new HashMap<>();
            earlyStoppingOptions.put("monitor", "\'" + ((String) baseParameter.get("monitor")) + "\'");
            earlyStoppingOptions.put("min_delta", (String) baseParameter.get("minDelta"));
            earlyStoppingOptions.put("mode", "\'auto\'");
            earlyStoppingOptions.put("verbose", "0");
            earlyStoppingOptions.put("patience", (String) baseParameter.get("patience"));
            netConfig.setEarlyStoppingOptions(earlyStoppingOptions);
        } else {
            netConfig.setEpochs((String) baseParameter.get("epochs"));
        }
        System.out.println("BaseParameter load success");
    }

    public static void loadDataToNetConfig(ArrayList<HashMap<String, Object>> arrayList, NetConfig netConfig) {

        processFile(arrayList, netConfig);

        processBaseParameter(arrayList, netConfig);

        HashMapUtil layerHashMap = new HashMapUtil();

        ArrayList<Pair<String, HashMap<String, String>>> pairArrayList = new ArrayList<>();
        for (HashMap<String, Object> i : arrayList) {
            String layerName = layerHashMap.getHashName((String) i.get("type"));
            i.remove("id");
            i.remove("type");
            if (i.containsKey("value")) {
                i.remove("value");
            }
            if (i.containsKey("isActive")) {
                i.remove("isActive");
            }
            pairArrayList.add(new Pair<String, HashMap<String, String>>(layerName, new HashMap<String, String>()));
            for (String key : i.keySet()) {
                pairArrayList.get(pairArrayList.size() - 1).snd.put(layerHashMap.getHashName(key), (String) i.get(key));
            }

        }
        netConfig.setLayerList(pairArrayList);


    }

    static public void deleteFile(String s) throws IOException {
        File file = new File(s);
        if (file.exists()) {
            try {
                file.delete();
                System.out.println(s + " deleted");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    static public void cleanFileBySuffix(String s) {
        File file = new File(propPathForPyCode);
        File temp = null;
        File[] filelist = file.listFiles();
        for (int i = 0; i < filelist.length; i++) {
            temp = filelist[i];
            if (temp.getName().endsWith(s)) {
                temp.delete();//删除文件}
                System.out.println("File " + temp.getName() + " deleted");
            }
        }
    }

    static public void cleanFolder(String s) {
        File file = new File(s);
        File temp = null;
        File[] filelist = file.listFiles();
        for (int i = 0; i < filelist.length; i++) {
            temp = filelist[i];
            temp.delete();//删除文件}
            System.out.println("File " + temp.getName() + " deleted");

        }

    }
}
