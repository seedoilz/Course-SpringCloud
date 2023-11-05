package com.segment.client.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URL;
import java.util.*;

public class FileUtils {

    public static String getSpringPath(String path) throws FileNotFoundException {
        return ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX).getAbsolutePath().replace("\\", "/")
                + "/" + path;
    }

    public static List<String> readLines(String path) {
        List<String> lines = new ArrayList<>();
        String line = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(path)));
            while ((line = reader.readLine()) != null) {
                if(line.trim().length() > 0){
                    lines.add(line.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return lines;
    }

    public static <T> void dumpList(List<T> objects, String path) {
        List<String> lines = new ArrayList<>();
        for (T obj : objects) {
            lines.add(JSON.toJSONString(obj));
        }
        writeLines(lines, path);
    }

    public static void writeLines(List<String> lines, String path) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(path)));
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Map<String, Integer> strMapToId(List<String> lines) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            map.put(lines.get(i), i);
        }
        return map;
    }

    public static Map<Integer, String> idMapToStr(List<String> lines) {
        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            map.put(i, lines.get(i));
        }
        return map;
    }

    /**
     * 返回目录下所有文件
     */
    public static List<String> getFiles(String path) {
        List<String> files = new ArrayList<>();

        // 使用类加载器获取资源文件夹的URL
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        Enumeration<URL> resources = null;
        try {
            resources = classLoader.getResources(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (resources.hasMoreElements()) {
            URL resourceURL = resources.nextElement();
            File folder = new File(resourceURL.getFile());
            File[] fileList = folder.listFiles();

            if (fileList != null) {
                for (File file : fileList) {
                    if (file.isFile()) {
                        files.add(file.getName());
                    }
                }
            }
        }

        return files;
    }

    /**
     * 返回文件名称
     */
    public static String getFileName(String path) {
        String[] names = path.replace("\\", "/").split("/");
        return names[names.length - 1].split("\\.")[0];
    }


}
