package com.zjt9363.service.utils;

import java.util.HashMap;

/**
 * @author Jiantong Zhang
 */
public class HashMapUtil {
    static HashMap<String,String> hashMap = new HashMap<>();

    public HashMapUtil() {
        hashMap.put("Convolution2D","Conv2D");
        hashMap.put("MaxPooling2D","MaxPooling2D");
        hashMap.put("Dropout","Dropout");
        hashMap.put("Dense","Dense");
        hashMap.put("Flatten","Flatten");
        hashMap.put("BatchNormalization","BatchNormalization");
        hashMap.put("GlobalAveragePooling2D","GlobalAveragePooling2D");
        hashMap.put("kernelSize","kernel_size");
        hashMap.put("rate","rate");
        hashMap.put("poolSize","pool_size");
        hashMap.put("units","units");
        hashMap.put("activation","activation");
        hashMap.put("filter","filters");
        hashMap.put("padding","padding");
    }



    public String getHashName(String name){
        return hashMap.get(name);
    }
}
