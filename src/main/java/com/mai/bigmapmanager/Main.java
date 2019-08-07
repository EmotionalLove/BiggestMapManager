package com.mai.bigmapmanager;

import com.mai.bigmapmanager.command.*;
import com.mai.bigmapmanager.data.ConfigVars;
import com.mai.bigmapmanager.data.schematic.SchematicSection;
import com.mai.bigmapmanager.data.schematic.SchematicStorage;
import com.mai.bigmapmanager.data.user.TrackedUser;
import com.mai.bigmapmanager.data.user.TrackedUserLoader;
import com.sasha.simplecmdsys.SimpleCommandProcessor;
import com.sasha.simplesettings.SettingHandler;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.hooks.AnnotatedEventManager;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static JDA DISCORD;
    public static final SettingHandler SETTING_HANDLER = new SettingHandler("config");
    public static final SimpleCommandProcessor COMMAND_PROCESSOR = new SimpleCommandProcessor("!");
    //vars//
    public static final ConfigVars CONFIG_VARS = new ConfigVars();
    public static List<TrackedUser> trackedUsers = new ArrayList<>();

    public static void main(String[] args) throws LoginException, InterruptedException, IllegalAccessException, InstantiationException, IOException, ClassNotFoundException {
        new Main();
    }

    public Main() throws LoginException, InterruptedException, InstantiationException, IllegalAccessException, IOException, ClassNotFoundException {
        //System.out.println(IdentifierTranslator.getRaw('K', 1)[1]);
        SchematicStorage.fixFiles();
        TrackedUserLoader.loadTrackedUsers();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("saving users before shutdown!!");
            try {
                TrackedUserLoader.updateTrackedUsers();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        COMMAND_PROCESSOR.register(AssignCommand.class);
        COMMAND_PROCESSOR.register(ForfeitCommand.class);
        COMMAND_PROCESSOR.register(DatabaseCommand.class);
        COMMAND_PROCESSOR.register(FinishCommand.class);
        COMMAND_PROCESSOR.register(ManualAssignCommand.class);
        COMMAND_PROCESSOR.register(LeaderboardCommand.class);
        System.out.println("reading config...");
        SETTING_HANDLER.read(CONFIG_VARS);
        String token = CONFIG_VARS.discordToken;
        if (token.equalsIgnoreCase("[no default]")) {
            System.err.println("discord token isn't set in config.yml");
            System.exit(1);
        }
        System.out.println("connecting to discord...");
        DISCORD = new JDABuilder(token).buildBlocking();
        DISCORD.setEventManager(new AnnotatedEventManager());
        DISCORD.addEventListener(new DiscordEvent());
        DISCORD.getPresence().setGame(Game.watching("Lewd Sasha"));
        System.out.println("ready.");
    }

    public static TrackedUser getTrackedUserByDiscordId(long id) {
        for (TrackedUser trackedUser : trackedUsers) {
            if (trackedUser.discordUserId == id) return trackedUser;
        }
        return new TrackedUser(id);
    }

    public static TrackedUser getTrackedUserByDiscordId(String id) {
        for (TrackedUser trackedUser : trackedUsers) {
            if (String.valueOf(trackedUser.discordUserId).equalsIgnoreCase(id)) return trackedUser;
        }
        return new TrackedUser(Long.parseLong(id));
    }

    public static TrackedUser getTrackedUserByDiscordId(User id) {
        for (TrackedUser trackedUser : trackedUsers) {
            if (trackedUser.discordUserId == id.getIdLong()) return trackedUser;
        }
        return new TrackedUser(id.getIdLong());
    }


    public static boolean isFileCompletedOrInUse(SchematicSection section) {
        for (TrackedUser trackedUser : trackedUsers) {
            if (trackedUser.pastSections.contains(section)) return true;
            if (trackedUser.sectionInProgress != null && trackedUser.sectionInProgress.equals(section)) return true;
        }
        return false;
    }

    public static TrackedUser getUserForFileCompletedOrInUse(SchematicSection section) {
        for (TrackedUser trackedUser : trackedUsers) {
            if (trackedUser.pastSections.contains(section)) return trackedUser;
            if (trackedUser.sectionInProgress != null && trackedUser.sectionInProgress.equals(section))
                return trackedUser;
        }
        return null;
    }

    public static boolean isAdmin(Guild guild, long id) {
        return guild.getMemberById(id).isOwner() || guild.getMemberById(id).hasPermission(Permission.ADMINISTRATOR);
    }

}
