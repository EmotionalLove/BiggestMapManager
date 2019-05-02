package com.mai.bigmapmanager.command;

import com.mai.bigmapmanager.DiscordEmbedBuilder;
import com.mai.bigmapmanager.DiscordEvent;
import com.mai.bigmapmanager.Main;
import com.mai.bigmapmanager.data.user.TrackedUser;
import com.sasha.simplecmdsys.SimpleCommand;
import net.dv8tion.jda.core.entities.User;

import java.util.List;

public class FinishCommand extends SimpleCommand {

    public FinishCommand() {
        super("finish");
    }

    @Override
    public void onCommand() {
        if (!Main.isAdmin(DiscordEvent.lastEvent.getGuild(), DiscordEvent.lastEvent.getAuthor().getIdLong())) {
            if (this.getArguments() == null || this.getArguments().length != 1 || !this.getArguments()[0].startsWith("http")) {
                DiscordEmbedBuilder.error("Missing Arguments", "Please provide a valid link to an image of the completed build.").submit();
                return;
            }
            String ufo = "<@219229187175743500> ";
            DiscordEmbedBuilder.general("Map is ready", ufo + ", please run !finish " + DiscordEvent.lastEvent.getAuthor().getAsMention() + " to mark this as complete.").submit();
            return;
        }
        List<User> mentions = DiscordEvent.lastEvent.getMessage().getMentionedUsers();
        if (mentions == null || mentions.size() == 0) {
            DiscordEmbedBuilder.error("Missing Arguments", "Please mention the person/people you would like to mark a task as complete from.").submit();
            return;
        }
        for (User mention : mentions) {
            TrackedUser user = Main.getTrackedUserByDiscordId(mention);
            if (user.sectionInProgress == null) {
                DiscordEmbedBuilder.error("Nothing to do", mention.getAsMention() + " doesn't have any tasks assigned to them.").submit();
                continue;
            }
            user.completeSchematic();
            DiscordEmbedBuilder.general("Marked as complete", mention.getAsMention() + "'s schematic is marked as completed, they can now assign themselves a new schematic.").submit();
        }
    }
}
