package com.mai.bigmapmanager.data.user;

import com.mai.bigmapmanager.Main;

import java.io.*;

public class TrackedUserLoader {

    public static final String DIR = "user";

    private static File dir() {
        File file = new File(DIR);
        if (!file.exists()) file.mkdir();
        if (!file.isDirectory()) {
            file.delete();
            file.mkdir();
        }
        return file;
    }

    public static void loadTrackedUsers() throws IOException, ClassNotFoundException {
        File dir = dir();
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".trackeduser")) {
                ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));
                TrackedUser user = (TrackedUser) stream.readObject();
                Main.trackedUsers.add(user);
                stream.close();
            }
        }
    }

    public static void updateTrackedUsers() throws IOException {
        for (TrackedUser trackedUser : Main.trackedUsers) {
            File file = new File(dir(), trackedUser.discordUserId + ".trackeduser");
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
            stream.writeObject(trackedUser);
        }
    }

}
