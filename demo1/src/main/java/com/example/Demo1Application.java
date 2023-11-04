package com.example;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class Demo1Application {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Demo1Application.class, args);
        draw("sample.csv");
    }

    private static void draw(String filename) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            int num=0;
            while ((line = br.readLine()) != null) {
                if(num==0){
                    num++;
                    continue;
                }
                Task task = new Task(line);
                executorService.execute(task);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
}

class Task implements Runnable{

    String line;
    Set<String> set;
    static CSVPrinter printer;
    static {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("task_id", "storeId", "storeName");
        try {
            printer = new CSVPrinter(new FileWriter("data1.csv"), format);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static synchronized void writeCsv(List<List<String>> data) throws IOException {
        for (List<String> datum : data) {
            printer.printRecord(datum);
        }
        printer.flush();
    }

    public Task(String line){
        this.line=line;
        set=new HashSet<>();
    }

    @Override
    public void run() {
        List<String> split=parseLine(line);
        String data=split.get(4);
        String task_id=split.get(1);
        List<List<String>> newList = getNewList(task_id, data);
        try {
            writeCsv(newList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<List<String>> getNewList(String task_id,String data){
        List<List<String>> res=new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(data);
        Map<String, Object> innerMap = jsonObject.getInnerMap();
        if(!innerMap.getOrDefault("msg",null).equals("成功(0)"))
            return res;
        jsonObject=(JSONObject)innerMap.getOrDefault("result", null);
        innerMap=jsonObject.getInnerMap();
        JSONArray jsonArray=(JSONArray)innerMap.getOrDefault("data",null);
        for (Object o : jsonArray) {
            Map<String, Object> unit = ((JSONObject) ((JSONObject) o).getInnerMap().get("data")).getInnerMap();
            String storeId=(String) unit.getOrDefault("storeId",null);
            String storeName=(String) unit.getOrDefault("storeName",null);
            if(storeName==null&&storeId==null) continue;
            if(set.contains(storeId+storeName)){
                continue;
            }
            set.add(storeId+storeName);
            List<String> strings = new ArrayList<>();
            strings.add(task_id);
            strings.add(storeId);
            strings.add(storeName);
            res.add(strings);
        }
        return res;
    }

    private List<String> parseLine(String line){
        List<String> words=new ArrayList<>();

        String[] split = line.split(",");
        for(int i=0;i<4;i++){
            words.add(split[i]);
        }
        StringJoiner stringJoiner = new StringJoiner(",");
        for(int i=4;i<split.length-6;i++){
            stringJoiner.add(split[i]);
        }
        String data=stringJoiner.toString();
        data=data.replace("\"\"","\"");
        data=data.replace("\\","");
        data=data.replace("\"{","{");
        data=data.replace("}\"","}");
        data=data.replace("\"[","[");
        data=data.replace("]\"","]");
        words.add(data);
        for(int i=split.length-6;i<split.length;i++){
            words.add(split[i]);
        }
        return words;
    }
}
