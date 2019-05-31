package com.mai.bigmapmanager.command;

import com.mai.bigmapmanager.DiscordEmbedBuilder;
import com.mai.bigmapmanager.Main;
import com.mai.bigmapmanager.data.user.TrackedUser;
import com.sasha.simplecmdsys.SimpleCommand;

import java.util.List;
import java.util.stream.Collectors;

public class LeaderboardCommand extends SimpleCommand {

    public LeaderboardCommand() {
        super("leaderboard");
    }

    @Override
    public void onCommand() {
        StringBuilder builder = new StringBuilder();
        List<TrackedUser> sorted = Main.trackedUsers.stream().sorted((o1, o2) -> Integer.compare(o2.pastSections.size(), o1.pastSections.size())).collect(Collectors.toList());
        int i = 0;
        int lastamt = 0;
        for (TrackedUser trackedUser : sorted) {
            if (trackedUser.pastSections.isEmpty()) break;
            if (trackedUser.pastSections.size() != lastamt) i++;
            String mention = trackedUser.getUserFromId() == null ? "@Unknown User (id " + trackedUser.discordUserId + ")" : trackedUser.getUserFromId().getAsMention();
            builder.append(i).append(" - ").append(mention).append(" - ").append(trackedUser.pastSections.size()).append(" completed maps\n");
            lastamt = trackedUser.pastSections.size();
        }
        DiscordEmbedBuilder.general("Leaderboard", builder.toString()).submit();
    }
}
