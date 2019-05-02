package com.mai.bigmapmanager;

import com.mai.bigmapmanager.data.user.TrackedUserLoader;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

import java.io.IOException;

public class DiscordEvent {

    public static GuildMessageReceivedEvent lastEvent;

    @SubscribeEvent
    public void onMsgRx(GuildMessageReceivedEvent e) {
        if (e.getAuthor().isBot()) return;
        String s = e.getMessage().getContentDisplay().toLowerCase();
        if (s.startsWith("!")) {
            e.getChannel().sendTyping().queue(q -> {
                System.out.println("processing command for " + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator() + " in channel " + e.getChannel().getName());
                lastEvent = e;
                Main.COMMAND_PROCESSOR.processCommand(s);
                try {
                    TrackedUserLoader.updateTrackedUsers();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

}
