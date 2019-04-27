package com.mai.bigmapmanager;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

public class DiscordEmbedBuilder {

    public static MessageAction general(String title, String message) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x2b83c6);
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(message);
        return DiscordEvent.lastEvent.getChannel().sendMessage(embedBuilder.build());
    }
    public static MessageAction general(TextChannel channel, String title, String message) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x2b83c6);
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(message);
        return channel.sendMessage(embedBuilder.build());
    }

    public static EmbedBuilder generalRaw(String title, String message) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x2b83c6);
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(message);
        return embedBuilder;
    }

    public static MessageAction error(String title, String message) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0xa50013);
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(message);
        return DiscordEvent.lastEvent.getChannel().sendMessage(embedBuilder.build());
    }
    public static MessageAction error(TextChannel channel, String title, String message) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0xa50013);
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(message);
        return channel.sendMessage(embedBuilder.build());
    }
    public static EmbedBuilder errorRaw(String title, String message) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0xa50013);
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(message);
        return embedBuilder;
    }
    public static MessageAction warn(String title, String message) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0xffc700);
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(message);
        return DiscordEvent.lastEvent.getChannel().sendMessage(embedBuilder.build());
    }
    public static MessageAction warn(TextChannel channel, String title, String message) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0xffc700);
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(message);
        return channel.sendMessage(embedBuilder.build());
    }
    public static EmbedBuilder warnRaw(String title, String message) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0xffc700);
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(message);
        return embedBuilder;
    }

}
