package me.jsinco.oneannouncer.commands.discord

import me.jsinco.oneannouncer.OneAnnouncer
import me.jsinco.oneannouncer.api.CommandOption
import me.jsinco.oneannouncer.api.DiscordCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

class ExecuteCommand : DiscordCommand {
    override fun name(): String {
        return "execute"
    }

    override fun description(): String {
        return "Execute a command from the console"
    }

    override fun execute(event: SlashCommandInteractionEvent) {
        val command = event.getOption("command")!!
        val commandString = command.asString
        event.reply("Executed from console: **$commandString**").queue()

        Bukkit.getScheduler().runTask(OneAnnouncer.plugin(), Runnable {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandString)
        })
    }

    override fun options(): List<CommandOption>? {
        val option = CommandOption(OptionType.STRING,"command", "The command to execute", true)
        return listOf(option)
    }

    override fun permission(): Permission {
        return Permission.MANAGE_SERVER
    }

    override fun plugin(): Plugin? {
        return OneAnnouncer.plugin()
    }
}