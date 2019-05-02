package com.mai.bigmapmanager.command;

import com.mai.bigmapmanager.DiscordEmbedBuilder;
import com.mai.bigmapmanager.DiscordEvent;
import com.mai.bigmapmanager.IdentifierTranslator;
import com.mai.bigmapmanager.Main;
import com.mai.bigmapmanager.data.schematic.SchematicSection;
import com.mai.bigmapmanager.data.schematic.SchematicStorage;
import com.mai.bigmapmanager.data.user.TrackedUser;
import com.sasha.simplecmdsys.SimpleCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

import java.io.IOException;
import java.util.List;

public class ManualAssignCommand extends SimpleCommand {

    public ManualAssignCommand() {
        super("manual");
    }

    @Override
    public void onCommand() {
        if (!Main.isAdmin(DiscordEvent.lastEvent.getGuild(), DiscordEvent.lastEvent.getAuthor().getIdLong())) return;
        List<User> mentions = DiscordEvent.lastEvent.getMessage().getMentionedUsers();
        // invalid if not 3 args or if theres no mention, or both :p
        if ((this.getArguments() == null || this.getArguments().length != 3) || (mentions == null || mentions.size() == 0)) {
            DiscordEmbedBuilder.error("Invalid Arguments", "Format should be `.manual @mention x z` (example: `.manual @sasha_who 3 5`)").submit();
            return;
        }
        TrackedUser trackedUser = Main.getTrackedUserByDiscordId(mentions.get(0));
        User discordUser = trackedUser.getUserFromId();
        if (trackedUser.sectionInProgress != null) {
            DiscordEmbedBuilder.warn("User already assigned a task", "To manually assign a task, please forfeit the user's current task first.").submit();
            return;
        }
        try {
            char x = this.getArguments()[1].trim().toCharArray()[0];
            int z = Integer.parseInt(this.getArguments()[2]);
            int[] raw = IdentifierTranslator.getRaw(Character.toUpperCase(x), z);
            SchematicSection section = new SchematicSection(raw[0], raw[1]);
            if (Main.isFileCompletedOrInUse(section)) {
                DiscordEmbedBuilder.error("Section already in-use", "This section was already completed or is currently in progress. It can't be assigned to another user.").submit();
                return;
            }
            trackedUser.sectionInProgress = section;
            //
            EmbedBuilder builder = DiscordEmbedBuilder.generalRaw("Task manually assigned", discordUser.getAsMention() + " has been assigned section **" + section.toString() + "**. Sending the file to the user's DM's...");
            DiscordEvent.lastEvent.getChannel().sendMessage(builder.build()).queue(msg -> {
                discordUser.openPrivateChannel().queue(dm -> {
                    try {
                        dm.sendFile(SchematicStorage.getFileForSchemSection(section)).queue(success -> {
                                    builder.setDescription(discordUser.getAsMention() + " has been assigned section **" + section.toString() + "**. Sent the file to the user's DM's.");
                                    msg.editMessage(builder.build()).submit();
                                },
                                fail -> {
                                    trackedUser.forfeitSchematic();
                                    builder.setTitle("Task assignment failure");
                                    builder.setDescription("~~" + discordUser.getAsMention() + " has been assigned section **[redacted]**~~. The user's privacy settings prevented me from sending them the file, so they have been removed from the manually assigned task.");
                                    builder.setColor(0xa50013);
                                    msg.editMessage(builder.build()).submit();
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                        trackedUser.forfeitSchematic();
                        builder.setTitle("Task assignment failure");
                        builder.setDescription("~~" + discordUser.getAsMention() + " has been assigned section **[redacted]**~~. An internal server error occurred. Please tell an operator to check the stacktrace.");
                        builder.setColor(0xa50013);
                        msg.editMessage(builder.build()).submit();
                    }
                });
            });
            //
        } catch (NumberFormatException xx) {
            DiscordEmbedBuilder.error("Invalid Arguments", "Format should be `.manual @mention x z` (example: `.manual @sasha_who 3 5`)").submit();
        }
    }
}
