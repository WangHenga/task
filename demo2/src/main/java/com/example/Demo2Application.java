package com.example;

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
public class Demo2Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo2Application.class, args);
        // 读取标签
        List<String []> matchList=new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader("标签词库1026.csv"))) {
            int num=0;
            while ((line = br.readLine()) != null) {
                if(num==0){
                    num++;
                    continue;
                }
                String[] split = line.split(",");
                matchList.add(split);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Task.matchList=matchList;
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        try (BufferedReader br = new BufferedReader(new FileReader("data1.csv"))) {
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
    static CSVPrinter printer;
    static List<String[]> matchList;
    static {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("task_id", "storeId", "storeName","tag");
        try {
            printer = new CSVPrinter(new FileWriter("data2.csv"), format);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeCsv(List<String> data) throws IOException {
        printer.printRecord(data);
        printer.flush();
    }

    public Task(String line){
        this.line=line;
    }

    @Override
    public void run() {
        String[] split = line.split(",");
        String data=split[2];
        List<String> res = new ArrayList<>(Arrays.asList(split));
        for (String[] strings : matchList) {
            if(data.contains(strings[0])&&data.contains(strings[1])&&data.contains(strings[2])){
                res.add(strings[4]);
                break;
            }
        }
        try {
            writeCsv(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}