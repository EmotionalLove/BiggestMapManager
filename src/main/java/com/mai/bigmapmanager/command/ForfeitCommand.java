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
        boolean mentionTag = mentions != null && mentions.size() > 0;
        boolean rawId = DiscordEvent.lastEvent.getMessage().getContentDisplay().contains("@") && !mentionTag;
        if (!mentionTag && !rawId) {
            DiscordEmbedBuilder.error("Missing Arguments", "Please mention the person/people you would like to remove a task from. If you can't mention them because they left the server, type @theirid").submit();
            return;
        }
        if (mentionTag) {
            for (User mention : mentions) {
                TrackedUser user = Main.getTrackedUserByDiscordId(mention);
                if (user.sectionInProgress == null) {
                    DiscordEmbedBuilder.error("Nothing to do", mention.getAsMention() + " doesn't have any tasks assigned to them.").submit();
                    continue;
                }
                user.forfeitSchematic();
                DiscordEmbedBuilder.general("Forfeited :(", mention.getAsMention() + " is no longer assigned a schematic. They can now assign themselves a different schematic.").submit();
            }
        } else {
            String id = DiscordEvent.lastEvent.getMessage().getContentDisplay().toLowerCase().replace("!forfeit @", "");
            TrackedUser user = Main.getTrackedUserByDiscordId(id);
            if (user.sectionInProgress == null) {
                DiscordEmbedBuilder.error("Nothing to do", "This user doesn't have any tasks assigned to them.").submit();
                return;
            }
            user.forfeitSchematic();
            DiscordEmbedBuilder.general("Forfeited :(", "This user is no longer assigned a schematic. They can now assign themselves a different schematic.").submit();
        }
    }
}
