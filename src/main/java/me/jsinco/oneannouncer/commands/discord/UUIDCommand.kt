package me.jsinco.oneannouncer.commands.discord

import me.jsinco.oneannouncer.DiscordSRVUtil
import me.jsinco.oneannouncer.OneAnnouncer
import me.jsinco.oneannouncer.api.CommandOption
import me.jsinco.oneannouncer.api.DiscordCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.bukkit.plugin.Plugin
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
            event.reply("You are not linked to a minecraft account!").setEphemeral(true).queue()
        } else {
            event.reply("Your UUID is: **$discordID**").setEphemeral(true).queue()
        }
    }

    override fun options(): List<CommandOption>? {
        return null
    }

    override fun permission(): Permission? {
        return null
    }

    override fun plugin(): Plugin? {
        return OneAnnouncer.plugin()
    }
}