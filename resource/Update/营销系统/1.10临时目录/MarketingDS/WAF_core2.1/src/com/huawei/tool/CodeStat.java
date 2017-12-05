package com.huawei.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CodeStat {
    private static final int BUFFER_SIZE = 1024 * 1024;
    
    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println(CodeStat.class.getName() + " types pathlist");
            System.exit(0);
        }
        
        String[] types = args[0].split(",");
        int pathNum = args.length;
        Map<String, Integer> result;
        File path;
        
        for(int i = 1; i < pathNum; i++) {
            result = new HashMap<String, Integer>();
            for(int j = 0; j < types.length; j++) {
                result.put(types[j], 0);
            }
            path = new File(args[i]);
            if(!path.exists()) {
                System.out.println("Path " + args[i] + " not exists");
            }
            statPath(path, result);
            System.out.println(Integer.toString(i) + ") Path " + args[i]);
            System.out.println("==Code:" + result.toString());
        }
        System.exit(0);
    }
    
    private static void statPath(File path, Map<String, Integer> result) {
        String name, type;
        int pos;
        byte[] buff = new byte[BUFFER_SIZE];
        File[] ff = path.listFiles();
        FileInputStream fis = null;
        int readLen;
        int lineNum, val;
        
        for(File f : ff) {
            if(f.isDirectory()) {
                statPath(f, result);
                continue;
            }
            name = f.getName();
            pos = name.lastIndexOf('.');
            if(pos <= 0) {
                continue;
            }
            type = name.substring(pos + 1);
            if(!result.containsKey(type)) {
                continue;
            }
            
            try {
                fis = new FileInputStream(f);
                do{
                    readLen = fis.read(buff);
                    lineNum = 0;
                    for(int i = 0; i < readLen; i++) {
                        if(buff[i] == '\n') {
                            lineNum++;
                        }
                    }
                    val = result.get(type) + lineNum;
                    result.put(type, val);
                } while(readLen == BUFFER_SIZE);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
