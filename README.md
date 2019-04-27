# BiggestMapManager
a discord bot for automating team map art cooperation.

## usage
if you are cooperatively building a very large map composed of multiple schematics, this is the discord bot for you. it makes it easy for users to assign tasks to themselves, and mark them complete when they're done. simply create a folder called `schematic` in the same directory as the JAR file for this bot, and drop all the schematics in. then, run the bot, and drop the discord token into the config file, and relaunch. the bot needs to be able to embed links and send messages.

> **hey!** schematic files must be named `x.y.schematic` (where x and y are section coords), or `section.x.y.schematic`

## commands
```
.assign - assign yourself a schematic to build.
.manual <@mention> <x> <y> - [admin only] assign a user a specific section
.complete <@mention(s)...> - [admin only] mark user(s) sections as complete.
.forfeit <@mention(s)... - [admin only] remove a user from a section that they are not intending to complete
.status - view the status of each section. (unassigned, in progress, complete)
```

## ty's
ufocrossing for making me make this (and then throwing it out as soon as it was done :sob:)
