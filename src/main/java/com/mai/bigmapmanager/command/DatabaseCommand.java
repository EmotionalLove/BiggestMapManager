package com.mai.bigmapmanager.command;

import com.mai.bigmapmanager.DiscordEmbedBuilder;
import com.mai.bigmapmanager.DiscordEvent;
import com.mai.bigmapmanager.Main;
import com.mai.bigmapmanager.data.schematic.SchematicSection;
import com.mai.bigmapmanager.data.schematic.SchematicStorage;
import com.mai.bigmapmanager.data.user.TrackedUser;
import com.sasha.simplecmdsys.SimpleCommand;
import net.dv8tion.jda.core.entities.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DatabaseCommand extends SimpleCommand {

    public DatabaseCommand() {
        super("database");
    }

    @Override
    public void onCommand() {
        List<User> mentions = DiscordEvent.lastEvent.getMessage().getMentionedUsers();
        StringBuilder builder = new StringBuilder();
        List<SchematicSection> sections = new ArrayList<>();
        List<File> files = SchematicStorage.getAllSchematics();
        for (File file : files) {
            sections.add(SchematicStorage.getSchemSectionForFile(file));
        }
        files.clear();
        int i = 0;
        int pages = 1;
        for (SchematicSection section : sections) {
            if (i >= 40) {
                DiscordEmbedBuilder.general("Status of known tasks (page " + pages + ")", builder.toString()).submit();
                builder = new StringBuilder();
                i = 0;
                pages++;
            }
            if (!Main.isFileCompletedOrInUse(section)) {
                builder.append("\n").append(section.toString()).append(" - unclaimed");
                i++;
                continue;
            }
            TrackedUser user = Main.getUserForFileCompletedOrInUse(section);
            if (user.sectionInProgress != null && user.sectionInProgress.equals(section)) {
                builder.append("\n").append(section.toString()).append(" - in progress by ").append(user.getUserFromId().getAsMention());
                i++;
            } else {
                builder.append("\n").append(section.toString()).append(" - completed by ").append(user.getUserFromId().getAsMention());
                i++;
            }
        }
        DiscordEmbedBuilder.general("Status of known tasks (last page)", builder.toString()).submit();
    }
}
