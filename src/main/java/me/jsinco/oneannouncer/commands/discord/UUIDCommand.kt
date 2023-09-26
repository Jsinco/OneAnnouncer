package me.jsinco.oneannouncer.commands.discord

import me.jsinco.oneannouncer.DiscordSRVUtil
import me.jsinco.oneannouncer.api.CommandOption
import me.jsinco.oneannouncer.api.DiscordCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.util.*

class UUIDCommand : DiscordCommand {
    override fun name(): String {
        return "uuid"
    }

    override fun description(): String {
        return "Get your minecraft UUID"
    }

    override fun execute(event: SlashCommandInteractionEvent) {
        val discordID: UUID? = DiscordSRVUtil.getUUIDFromDiscordID(event.user.id)
        if (discordID == null) {
            event.reply("You are not linked to a minecraft account!").queue()
        } else {
            event.reply("Your UUID is: **$discordID**").queue()
        }
    }

    override fun options(): List<CommandOption>? {
        return null
    }

    override fun permission(): Permission? {
        return null
    }
}