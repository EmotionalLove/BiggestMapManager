package com.mai.bigmapmanager.data.schematic;

import com.mai.bigmapmanager.data.schematic.SchematicSection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SchematicStorage {

    public static final String DIR = "schematic";

    private static File dir() {
        File file = new File(DIR);
        if (!file.exists()) file.mkdir();
        if (!file.isDirectory()) {
            file.delete();
            file.mkdir();
        }
        return file;
    }

    public static void fixFiles() {
        File dir = dir();
        for (File file : dir.listFiles()) {
            if (file.getName().startsWith("section.")) {
                file.renameTo(new File(dir, file.getName().replace("section.", "")));
            }
        }
    }

    public static File pullRandomSectionSchem() {
        File file = dir();
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("no schematics in schematic folder");
            return null;
        }
        Random random = new Random();
        return files[random.nextInt(files.length)];
    }

    // x.z.schematic
    public static SchematicSection getSchemSectionForFile(File file) {
        String[] fileName = file.getName().toLowerCase().split("\\.");
        if (fileName.length != 3) throw new IllegalArgumentException("file doesn't follow format x.x.schamtic");
        int x = Integer.parseInt(fileName[0]);
        int z = Integer.parseInt(fileName[1]);
        return new SchematicSection(x, z);
    }

    public static File getFileForSchemSection(SchematicSection schematicSection) {
        File file = new File(dir(), schematicSection.x + "." + schematicSection.z + ".schematic");
        if (!file.exists()) throw new IllegalArgumentException("no file exists for the schemSection");
        return file;
    }

    public static int getSizeAllSchematics() {
        File file = dir();
        File[] files = file.listFiles();
        return files.length;
    }

    public static List<File> getAllSchematics() {
        List<File> schems = new ArrayList<>();
        File file = dir();
        File[] files = file.listFiles();
        for (File file1 : files) {
            String[] fileName = file1.getName().toLowerCase().split("\\.");
            if (fileName.length == 3) schems.add(file1);
        }
        return schems;
    }

}
