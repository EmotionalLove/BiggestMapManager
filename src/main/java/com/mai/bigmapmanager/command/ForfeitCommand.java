package com.mai.bigmapmanager.command;

import com.mai.bigmapmanager.DiscordEmbedBuilder;
import com.mai.bigmapmanager.DiscordEvent;
import com.mai.bigmapmanager.Main;
import com.mai.bigmapmanager.data.user.TrackedUser;
import com.sasha.simplecmdsys.SimpleCommand;
import net.dv8tion.jda.core.entities.User;

import java.util.List;

public class ForfeitCommand extends SimpleCommand {

    public ForfeitCommand() {
        super("forfeit");
    }

    @Override
    public void onCommand() {
        if (!Main.isAdmin(DiscordEvent.lastEvent.getGuild(), DiscordEvent.lastEvent.getAuthor().getIdLong())) return;
        List<User> mentions = DiscordEvent.lastEvent.getMessage().getMentionedUsers();
        if (mentions == null || mentions.size() == 0) {
            DiscordEmbedBuilder.error("Missing Arguments", "Please mention the person/people you would like to remove a task from.").submit();
            return;
        }
        for (User mention : mentions) {
            TrackedUser user = Main.getTrackedUserByDiscordId(mention);
            if (user.sectionInProgress == null) {
                DiscordEmbedBuilder.error("Nothing to do", mention.getAsMention() + " doesn't have any tasks assigned to them.").submit();
                continue;
            }
            user.forfeitSchematic();
            DiscordEmbedBuilder.general("Forfeited :(", mention.getAsMention() + " is no longer assigned a schematic. They can now assign themselves a different schematic.").submit();
        }
    }
}
