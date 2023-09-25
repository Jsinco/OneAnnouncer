package me.jsinco.oneannouncer.api

import me.jsinco.oneannouncer.api.CommandOption
import me.jsinco.oneannouncer.api.DiscordCommand
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import org.bukkit.Bukkit
import org.jetbrains.annotations.NotNull

class AnnounceCommand : DiscordCommand {
    override fun name(): String {
        return "announcediscord"
    }

    override fun description(): String {
        return "Announce a message to Minecraft"
    }

    override fun execute(@NotNull event: SlashCommandInteractionEvent) {
        event.reply("Hello World!").queue()
        Bukkit.broadcastMessage("Hello World!")
    }

    override fun options(): List<CommandOption?> {
        val option = CommandOption(OptionType.STRING, "msg", "The message to send", true)
        return listOf(option)
    }
}