package com.mai.bigmapmanager.data.user;

import com.mai.bigmapmanager.Main;
import com.mai.bigmapmanager.data.schematic.SchematicSection;
import com.mai.bigmapmanager.data.schematic.SchematicStorage;
import net.dv8tion.jda.core.entities.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TrackedUser implements Serializable {

    public long discordUserId;
    public SchematicSection sectionInProgress = null;
    public List<SchematicSection> pastSections = new ArrayList<>();

    public TrackedUser(long id) {
        this.discordUserId = id;
        Main.trackedUsers.add(this);
    }

    public SchematicSection assignNewSection() {
        if (sectionInProgress != null)
            throw new IllegalStateException("TrackedUser already has a schematic in progress.");
        // this is a little wack
        int c = 0;
        int max = SchematicStorage.getSizeAllSchematics();
        ArrayList<SchematicSection> triedSchems = new ArrayList<>();
        while (true) {
            SchematicSection sect = SchematicStorage.getSchemSectionForFile(SchematicStorage.pullRandomSectionSchem());
            if (c == max) return null;
            if (triedSchems.contains(sect)) continue;
            if (Main.isFileCompletedOrInUse(sect)) {
                triedSchems.add(sect);
                c++;
                continue;
            }
            sectionInProgress = sect;
            return sect;
        }

    }

    public void forfeitSchematic() {
        sectionInProgress = null;
    }

    public void completeSchematic() {
        pastSections.add(sectionInProgress);
        sectionInProgress = null;
    }

    public User getUserFromId() {
        return Main.DISCORD.getUserById(discordUserId);
    }


}
