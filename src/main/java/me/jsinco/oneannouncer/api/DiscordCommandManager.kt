package me.jsinco.oneannouncer.api

import me.jsinco.oneannouncer.OneAnnouncer
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction

object DiscordCommandManager : ListenerAdapter(){

    val commands: MutableMap<String, DiscordCommand> = mutableMapOf()

    init {
        registerCommand(AnnounceCommand(), "announcediscord")
    }

    /**
     * Register Discord Commands
     * @param command The command to register
     * @param commandName The name of the command
     */
    @JvmStatic
    fun registerCommand(command: DiscordCommand, commandName: String) {
        commands[commandName] = command

        val jda = OneAnnouncer.getJDA().awaitReady()

        for (guild in jda.guilds) {
            val cmd: CommandCreateAction = guild.upsertCommand(command.name()!!, command.description()!!)
            if (command.options() != null && command.options()!!.isNotEmpty()) {
                for (option in command.options()!!) {
                    cmd.addOption(option!!.optioneType, option.name, option.description, option.required)
                }
            }

            cmd.queue()
        }
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val command = commands[event.name] ?: return
        command.execute(event)
    }
}

