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
        int i = 1;
        for (TrackedUser trackedUser : sorted) {
            if (i > 10) break;
            builder.append(i).append(" - ").append(trackedUser.getUserFromId().getAsMention()).append(" - ").append(trackedUser.pastSections.size()).append(" completed maps\n");
            i++;
        }
        DiscordEmbedBuilder.general("Leaderboard", builder.toString()).submit();
    }
}
