package me.jsinco.oneannouncer.api

import me.jsinco.oneannouncer.OneAnnouncer
import me.jsinco.oneannouncer.util.Util
import me.jsinco.oneannouncer.commands.discord.AnnounceCommand
import me.jsinco.oneannouncer.commands.discord.ExecuteCommand
import me.jsinco.oneannouncer.commands.discord.UUIDCommand
import me.jsinco.oneannouncer.discord.JDAMethods
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction
import net.dv8tion.jda.internal.utils.tuple.MutablePair
import org.bukkit.Bukkit
import org.bukkit.ChatColor

class DiscordCommandManager : ListenerAdapter(){

    init {
        registerCommand(AnnounceCommand())
        registerCommand(ExecuteCommand())
        registerGlobalCommand(UUIDCommand())
    }



    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val command = commands[event.name] ?: return

        if (command.permission() != null && !event.member!!.hasPermission(command.permission()!!)) {
            event.reply("You do not have permission to use this command! Permission: ${command.permission()!!.name}").setEphemeral(true).queue()
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
                log("&cCommand &d\"${command.name()}\" &calready exists!")
                log("&cRe-registering command &d\"${command.name()}\"")
            }
            commands[command.name()] = command
            val pluginName = command.plugin()?.name ?: "Unknown Plugin"

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
                        registeredCommands.left++
                        registered = true
                    }
                }

                log("&aRegistered command: &d\"${command.name()}\" &a| Total Commands: &d${registeredCommands.left} &a| Total Guilds: &d${registeredCommands.right} &a(&d$pluginName&a)")
            }, 100L)

            registeredCommands.right = jda.guilds.size
            log("&aGot a &dguild&a command from &d$pluginName &afor: &d\"${command.name()}\"&a, registering...")
        }


        @JvmStatic
        fun registerGlobalCommand(command: DiscordCommand) {
            if (commands.containsKey(command.name())) {
                log("&cCommand &d\"${command.name()}\" &calready exists!")
                log("&cRe-registering command &d\"${command.name()}\"")
            }
            commands[command.name()] = command
            val pluginName = command.plugin()?.name ?: "Unknown Plugin"

            Bukkit.getScheduler().runTaskLater(plugin, Runnable { // Delay is necessary for server start up
                val cmd: CommandCreateAction = jda.upsertCommand(command.name(), command.description())
                if (command.options() != null && command.options()!!.isNotEmpty()) {
                    for (option in command.options()!!) {
                        cmd.addOption(option.optionType, option.name, option.description, option.required)
                    }
                }

                cmd.queue()

                log("&aRegistered &dglobal&a command &d\"${command.name()}\" &a| Total Commands: &d${registeredCommands.left} &a| &dGlobal Command &a(&d$pluginName&a)")
            }, 100L)
            registeredCommands.left++
            log("&aGot a &dglobal&a command from &d$pluginName &afor: &d\"${command.name()}\"&a, registering...")
        }


        @JvmStatic
        fun getCommand(commandName: String): DiscordCommand? {
            return commands[commandName]
        }

        private fun log(message: String) {
            plugin.server.consoleSender.sendMessage(Util.colorcode("${plugin.config.getString("prefix")} $message"))
            try {
                if (plugin.config.getBoolean("debug.verbose")) {
                    JDAMethods.sendMessageDiscordChannel(plugin.config.getString("debug.verbose-channel"), ChatColor.stripColor(
                        Util.colorcode(message)), true)
                }
            } catch (e: Exception) {
                plugin.logger.warning("Failed to send verbose message to discord channel: ${e.message}")
            }
        }
    }
}



