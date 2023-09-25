package me.jsinco.oneannouncer.commands.discord

import me.jsinco.oneannouncer.api.DiscordCommandManager

class Manager {
    init {
        DiscordCommandManager.registerCommand(AnnounceCommand())
    }
}
