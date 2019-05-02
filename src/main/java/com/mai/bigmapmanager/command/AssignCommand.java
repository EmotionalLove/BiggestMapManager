package com.mai.bigmapmanager.command;

import com.mai.bigmapmanager.DiscordEmbedBuilder;
import com.mai.bigmapmanager.DiscordEvent;
import com.mai.bigmapmanager.Main;
import com.mai.bigmapmanager.data.schematic.SchematicSection;
import com.mai.bigmapmanager.data.schematic.SchematicStorage;
import com.mai.bigmapmanager.data.user.TrackedUser;
import com.sasha.simplecmdsys.SimpleCommand;
import net.dv8tion.jda.core.EmbedBuilder;

import java.io.IOException;

public class AssignCommand extends SimpleCommand {

    public AssignCommand() {
        super("assign");
    }

    @Override
    public void onCommand() {
        TrackedUser user = Main.getTrackedUserByDiscordId(DiscordEvent.lastEvent.getAuthor());
        if (user.sectionInProgress != null) {
            SchematicSection section = user.sectionInProgress;
            DiscordEmbedBuilder.error("Task already in-progress", "You're already working on section **" + section.toString() + "**").submit();
            return;
        }
        SchematicSection section = user.assignNewSection();
        if (section == null) {
            DiscordEmbedBuilder.error("Couldn't find an available task", "A task couldn't be assigned to you, they might be all taken up.").submit();
            return;
        }
        EmbedBuilder builder = DiscordEmbedBuilder.generalRaw("Task assigned", "You've been assigned section **" + section.toString() + "**. Sending the file to you in your DM's...");
        DiscordEvent.lastEvent.getChannel().sendMessage(builder.build()).queue(msg -> {
            DiscordEvent.lastEvent.getAuthor().openPrivateChannel().queue(dm -> {
                try {
                    dm.sendFile(SchematicStorage.getFileForSchemSection(section)).queue(success -> {
                                builder.setDescription("You've been assigned section **" + section.toString() + "**. Sent the file to you in your DM's.");
                                msg.editMessage(builder.build()).submit();
                            },
                            fail -> {
                                user.forfeitSchematic();
                                builder.setTitle("Task assignment failure");
                                builder.setDescription("~~You've been assigned section **[redacted]**~~. Your privacy settings prevented me from sending you the file, so you have been removed from your task.");
                                builder.setColor(0xa50013);
                                msg.editMessage(builder.build()).submit();
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                    user.forfeitSchematic();
                    builder.setTitle("Task assignment failure");
                    builder.setDescription("~~You've been assigned section **[redacted]**~~. An internal server error occurred. Please tell an operator to check the stacktrace.");
                    builder.setColor(0xa50013);
                    msg.editMessage(builder.build()).submit();
                }
            });
        });

    }
}
