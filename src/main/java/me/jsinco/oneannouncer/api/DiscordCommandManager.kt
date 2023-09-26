package me.jsinco.oneannouncer.api

import me.jsinco.oneannouncer.OneAnnouncer
import me.jsinco.oneannouncer.Util
import me.jsinco.oneannouncer.commands.discord.AnnounceCommand
import me.jsinco.oneannouncer.commands.discord.ExecuteCommand
import me.jsinco.oneannouncer.commands.discord.UUIDCommand
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.User
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
        registerGlobalCommand(UUIDCommand())
    }



    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val command = commands[event.name] ?: return

        if (command.permission() != null && !event.member!!.hasPermission(command.permission()!!)) {
            event.reply("You do not have permission to use this command! Permission: ${command.permission()!!.name}").queue()
            return
        }
        command.execute(event)
    }


    companion object {
        val plugin: OneAnnouncer = OneAnnouncer.plugin()
        val jda: JDA = OneAnnouncer.getJDA()
        val commands: MutableMap<String, DiscordCommand> = mutableMapOf()
        private var registeredCommands: MutablePair<Int, Int> = MutablePair(0, 0)
        /**
         * Register Discord Commands
         * @param command The command to register
         */
        @JvmStatic
        fun registerCommand(command: DiscordCommand) {
            if (commands.containsKey(command.name())) {
                plugin.logger.warning("Command \"${command.name()}\" already exists!")
                return
            }
            commands[command.name()] = command

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


                    if (!registered) {
                        registeredCommands.left += 1
                        registered = true
                    }
                }

                plugin.server.consoleSender.sendMessage(Util.colorcode("${plugin.config.getString("prefix")} &aRegistered command: &d\"${command.name()}\" &a| Total Commands: &d${registeredCommands.left} &a| Total Guilds: &d${registeredCommands.right}"))
            }, 100L)

            registeredCommands.right = jda.guilds.size
            plugin.server.consoleSender.sendMessage(Util.colorcode("${plugin.config.getString("prefix")} &aGot a command for: &d\"${command.name()}\"&a, registering..."))
        }


        @JvmStatic
        fun registerGlobalCommand(command: DiscordCommand) {
            if (commands.containsKey(command.name())) {
                plugin.logger.warning("Command \"${command.name()}\" already exists!")
                return
            }
            commands[command.name()] = command


            Bukkit.getScheduler().runTaskLater(plugin, Runnable { // Delay is necessary for server start up
                val cmd: CommandCreateAction = jda.upsertCommand(command.name(), command.description())
                if (command.options() != null && command.options()!!.isNotEmpty()) {
                    for (option in command.options()!!) {
                        cmd.addOption(option.optionType, option.name, option.description, option.required)
                    }
                }

                cmd.queue()
                plugin.server.consoleSender.sendMessage(Util.colorcode("${plugin.config.getString("prefix")} &aRegistered &dglobal&a command &d\"${command.name()}\" &a| Total Commands: &d${registeredCommands.left} &a| &dGlobal Command"))
            }, 100L)

            plugin.server.consoleSender.sendMessage(Util.colorcode("${plugin.config.getString("prefix")} &aGot a &dglobal&a command for: &d\"${command.name()}\"&a, registering..."))
        }


        @JvmStatic
        fun getCommand(commandName: String): DiscordCommand? {
            return commands[commandName]
        }
    }
}



