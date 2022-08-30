package com.zjt9363;

import com.zjt9363.service.NetConfig;
import com.zjt9363.service.PyGenerator;
import com.zjt9363.service.PyRun;
import com.zjt9363.service.utils.NetText;
import com.zjt9363.service.utils.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

@Component
@SpringBootTest
class DemoApplicationTests {
    @Autowired
    PyGenerator pyGenerator;
    @Autowired
    NetConfig netConfig;



    @Autowired
    PyRun pyRun;

    @Test
    public void testAutoGenerate() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ArrayList<Pair<String, HashMap<String, String>>> arrayList= new ArrayList<>();
        arrayList.add(new Pair<String, HashMap<String, String>>(NetText.conv2D, new HashMap<String, String>()));
        arrayList.get(0).snd.put(NetText.filters,"128");
        arrayList.get(0).snd.put(NetText.kernelSize,"6,6");
        arrayList.get(0).snd.put(NetText.activation,"relu");

        arrayList.add(new Pair<String, HashMap<String, String>>(NetText.conv2D, new HashMap<String, String>()));
        arrayList.get(1).snd.put(NetText.filters,"128");
        arrayList.get(1).snd.put(NetText.kernelSize,"6,6");
        arrayList.get(1).snd.put(NetText.activation,"relu");

        arrayList.add(new Pair<String, HashMap<String, String>>(NetText.maxPooling2D, new HashMap<String, String>()));
        arrayList.get(2).snd.put(NetText.poolSize, "2,2");

        arrayList.add(new Pair<String, HashMap<String, String>>(NetText.dropout, new HashMap<String, String>()));
        arrayList.get(3).snd.put(NetText.rate,"0.5");

        arrayList.add(new Pair<String, HashMap<String, String>>(NetText.flatten,new HashMap<String, String>()));

        arrayList.add(new Pair<String, HashMap<String, String>>(NetText.dense,new HashMap<String, String>()));
        arrayList.get(5).snd.put(NetText.units,"128");

        arrayList.add(new Pair<String, HashMap<String, String>>(NetText.dropout, new HashMap<String, String>()));
        arrayList.get(6).snd.put(NetText.rate,"0.5");

        arrayList.add(new Pair<String, HashMap<String, String>>(NetText.dense, new HashMap<String, String>()));
        arrayList.get(7).snd.put(NetText.units,"10");
        arrayList.get(7).snd.put(NetText.activation,"softmax");

        netConfig.setLayerList(arrayList);

        netConfig.setFileName("mnist.pkl.gz");

        netConfig.setNumClasses(10);

        netConfig.setLossFunction("sparse_categorical_crossentropy");

        pyGenerator.codeGenerator(netConfig);
    }

    @Test
    public void runCode() throws FileNotFoundException {
        Process proc = null;
        pyRun.run("main.py");

    }



    @Test
    public void test() throws IOException {
        System.out.println("hehe");
    }

}
