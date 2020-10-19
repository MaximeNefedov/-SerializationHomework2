package Serialization.homework2;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        GameProgress gameProgress = new GameProgress(100, 2, 2, 5.5);
        GameProgress gameProgress2 = new GameProgress(76, 4, 12, 35.2);
        GameProgress gameProgress3 = new GameProgress(88, 8, 45, 65.7);

        Map<String, GameProgress> map = new HashMap<>();
        map.put("/Users/maksim/Games/savegames/save1.txt", gameProgress);
        map.put("/Users/maksim/Games/savegames/save2.txt", gameProgress2);
        map.put("/Users/maksim/Games/savegames/save3.txt", gameProgress3);

        saveGame(map);
        zipFiles("/Users/maksim/Games/savegames/saves.zip", map);
        deleteExcessFiles();
    }

    public static void saveGame(Map<String, GameProgress> map) {
        for (Map.Entry<String, GameProgress> entries : map.entrySet()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(entries.getKey()))) {
                oos.writeObject(entries.getValue());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public static void zipFiles(String path, Map<String, GameProgress> map) {
        Set<String> paths = map.keySet();
        int counter = 1;
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(path))){
            for (String s : paths) {
                FileInputStream fis = new FileInputStream(s);
                ZipEntry zipEntry = new ZipEntry(path + "/packedTest" + counter + ".txt");
                counter++;
                zos.putNextEntry(zipEntry);
                byte[] bytes = new byte[fis.available()];
                fis.read(bytes);
                fis.close();
                zos.write(bytes);
                zos.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteExcessFiles() {
        File files = new File("/Users/maksim/Games/savegames");
        if (files.isDirectory()) {
            for (File file : files.listFiles()) {
                if (!file.getName().contains(".zip")) {
                    file.delete();
                }
            }
        }

    }
}
