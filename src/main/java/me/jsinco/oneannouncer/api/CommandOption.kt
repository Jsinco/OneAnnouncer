package me.jsinco.oneannouncer.api

import net.dv8tion.jda.api.interactions.commands.OptionType

class CommandOption (
    val optioneType: OptionType,
    val name: String,
    val description: String,
    val required: Boolean
)