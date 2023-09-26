package me.jsinco.oneannouncer.api

import me.jsinco.oneannouncer.OneAnnouncer
import me.jsinco.oneannouncer.commands.discord.AnnounceCommand
import me.jsinco.oneannouncer.commands.discord.ExecuteCommand
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction
import net.dv8tion.jda.internal.utils.tuple.MutablePair
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Color

class DiscordCommandManager : ListenerAdapter(){

    init {
        registerCommand(AnnounceCommand())
        registerCommand(ExecuteCommand())
    }



    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val command = commands[event.name] ?: return
        command.execute(event)
    }


    companion object {
        val plugin: OneAnnouncer = OneAnnouncer.plugin()
        val commands: MutableMap<String, DiscordCommand> = mutableMapOf()
        private var registeredCommands: MutablePair<Int, Int> = MutablePair(0, 0)
        /**
         * Register Discord Commands
         * @param command The command to register
         */
        @JvmStatic
        fun registerCommand(command: DiscordCommand) {
            commands[command.name()] = command

            val jda = OneAnnouncer.getJDA()
            Bukkit.getScheduler().runTaskLater(plugin, Runnable { // Delay is necessary for server start up
                var registered = false
                for (guild in jda.guilds) {
                    val cmd: CommandCreateAction = guild.upsertCommand(command.name(), command.description())
                    if (command.options() != null && command.options()!!.isNotEmpty()) {
                        for (option in command.options()!!) {
                            cmd.addOption(option.optionType, option.name, option.description, option.required)
                        }
                    }

                    cmd.queue()

                    registeredCommands.right += 1
                    if (!registered) {
                        registeredCommands.left += 1
                        registered = true
                    }
                }

                plugin.logger.info("${ChatColor.GREEN}Registered command \"${command.name()}\" | Total Commands: ${registeredCommands.left} | Total Guilds: ${registeredCommands.right}")
            }, 100L)

            plugin.logger.info("${ChatColor.GREEN}Got a command for: ${command.name()}, registering...")
        }

        @JvmStatic
        fun getCommand(commandName: String): DiscordCommand? {
            return commands[commandName]
        }
    }
}



