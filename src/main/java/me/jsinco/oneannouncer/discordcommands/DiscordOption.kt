package me.jsinco.oneannouncer.discordcommands

import net.dv8tion.jda.api.interactions.commands.OptionType

class DiscordOption (
    val type: OptionType,
    val name: String,
    val description: String,
    val required: Boolean
)
