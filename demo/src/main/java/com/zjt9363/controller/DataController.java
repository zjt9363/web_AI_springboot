package com.zjt9363.controller;


import com.zjt9363.service.*;
import com.zjt9363.service.utils.ThreadPyRun;
import com.zjt9363.service.utils.Util;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;


import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Jiantong Zhang
 */
@RestController
@PropertySource(value = "classpath:pyCodeProp.properties", ignoreResourceNotFound = true)

public class DataController {

    @Autowired
    PyGenerator pyGenerator;


    @Autowired
    PyRun pyRun;

    @Value("${pyFilePath}")
    String pyFilePath;
    @Value("${propertiesPath}")
    String propertiesPath;
    @Value("${filePropPath}")
    String filePropPath;

    @Autowired
    NetConfig netConfig;

    private ThreadPyRun threadPyRun;

    String fileNameRecorder;

    @PostMapping("/submit")
    public String dataSave(@RequestBody ArrayList<HashMap<String, Object>> arrayList) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        Util.deleteFile(pyFilePath);

        Util.cleanFileBySuffix(".jpg");

        Util.deleteFile(propertiesPath + "model.h5");

        System.out.println("Init Success");

        System.out.println(arrayList.toString());

        Util.loadDataToNetConfig(arrayList, netConfig);

        pyGenerator.codeGenerator(netConfig);

        //pyRun.run(pyFilePath);

        threadPyRun = new ThreadPyRun(pyRun, pyFilePath);

        threadPyRun.start();


        return "200";
    }

    @PostMapping(value = "/upload")
    @ResponseBody
    public String handleFileUpload(MultipartFile file, String name) throws IOException {
        Util.cleanFolder(filePropPath);
        System.out.println("File " + name + " Uploaded");
        InputStream inputStream = null;
        FileOutputStream fileOut = null;
        try {
            fileNameRecorder = filePropPath + name;
            inputStream = file.getInputStream();
            fileOut = new FileOutputStream(fileNameRecorder);
            IOUtils.copy(inputStream, fileOut);
            fileOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileOut != null) {
                    fileOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "200";

    }

    @GetMapping("/stop")
    public String stop(String bool) throws IOException {

        System.out.println("Stop Running");

        try {
            threadPyRun.interrupt();
        } finally {
            try {
                netConfig.cleaner();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "true";
    }


    @PostMapping(value = "/downloadPyFile")
    public ResponseEntity<StreamingResponseBody> pyDownload(@RequestBody String s) throws Exception {
        String downloadPath = s.replaceAll("\\\\\\\\", "\\\\").replaceAll("\\\"", "");

        System.out.println("Download path: " + downloadPath);

        FileSystemResource file = new FileSystemResource(downloadPath);


        InputStream inputStream = file.getInputStream();
        StreamingResponseBody responseBody = outputStream -> {
            int numberOfBytesToWrite;
            byte[] data = new byte[1024];
            while ((numberOfBytesToWrite = inputStream.read(data, 0, data.length)) != -1) {
                outputStream.write(data, 0, numberOfBytesToWrite);
            }
            inputStream.close();
        };
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=%s", file.getFilename()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(responseBody);
    }


    @PostMapping("/getImg")
    public ResponseEntity<byte[]> getImg(@RequestBody String s) throws IOException {

        String downloadPath = s.replaceAll("\\\\\\\\", "\\\\").replaceAll("\\\"", "");

        System.out.println("Download path: " + downloadPath);

        FileSystemResource file = new FileSystemResource(downloadPath);


        InputStream inputStream = file.getInputStream();



        byte[] bytesByStream = getBytesByStream(inputStream);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(bytesByStream, headers, HttpStatus.OK);
    }


    public byte[] getBytesByStream(InputStream inputStream) {
        byte[] bytes = new byte[1024];

        int b;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            while ((b = inputStream.read(bytes)) != -1) {

                byteArrayOutputStream.write(bytes, 0, b);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
