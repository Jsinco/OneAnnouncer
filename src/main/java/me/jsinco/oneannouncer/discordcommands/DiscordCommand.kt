package me.jsinco.oneannouncer.discordcommands

import net.dv8tion.jda.api.interactions.commands.OptionType

interface DiscordCommand {

    fun command(name: String, description: String)

    fun addOptions(options: List<DiscordOption>)
}