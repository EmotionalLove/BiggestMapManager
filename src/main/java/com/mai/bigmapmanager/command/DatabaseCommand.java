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

import static org.apache.commons.io.comparator.NameFileComparator.NAME_COMPARATOR;

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
        files.sort(NAME_COMPARATOR);
        for (File file : files) {
            sections.add(SchematicStorage.getSchemSectionForFile(file));
        }
        int complete = 0;
        files.clear();
        int i = 0;
        int pages = 1;
        for (SchematicSection section : sections) {
            if (i >= 30) {
                DiscordEmbedBuilder.general("Status of known tasks (page " + pages + ")", builder.toString()).submit();
                builder = new StringBuilder();
                i = 0;
                pages++;
            }
            if (!Main.isFileCompletedOrInUse(section)) {
                //builder.append("\n").append(section.toString()).append(" - :red_circle: unclaimed");
                //i++;
                continue;
            }
            TrackedUser user = Main.getUserForFileCompletedOrInUse(section);
            User discorduser = user.getUserFromId();
            String mention = discorduser == null ? "@Unknown User (id " + user.discordUserId + ")" : discorduser.getAsMention();
            if (user.sectionInProgress != null && user.sectionInProgress.equals(section)) {
                builder.append("\n").append(section.toString()).append(" - :large_orange_diamond: in progress by ").append(mention);
                i++;
            } else {
                builder.append("\n").append(section.toString()).append(" - :white_check_mark: completed by ").append(mention);
                i++;
                complete++;
            }
        }
        DiscordEmbedBuilder.general("Status of known tasks (last page)", builder.toString()).submit();
        DiscordEmbedBuilder.general("Statistics", "**Total Maps** " + sections.size() + "\n**Complete Maps** " + complete).submit();
    }
}
