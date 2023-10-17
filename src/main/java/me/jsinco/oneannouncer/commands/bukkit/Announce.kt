package me.jsinco.oneannouncer.commands.bukkit

import com.iridium.iridiumcolorapi.IridiumColorAPI
import me.jsinco.oneannouncer.OneAnnouncer
import me.jsinco.oneannouncer.discord.JDAMethods
import me.jsinco.oneannouncer.util.Util
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player
import java.util.function.Consumer

class Announce(val plugin: OneAnnouncer) : BukkitCommand(
    "announce", "Announce a message to a channel", "/announce <message>", plugin.config.getStringList("announce-cmd.aliases")
) {

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        var msg = java.lang.String.join(" ", *args)
        val playerSender = if (sender is Player) sender else null
        msg = Util.executeStringCommands(playerSender, Util.addPlaceholders("announce-cmd.placeholders", msg))
        var channel = OneAnnouncer.plugin().config.getString("announce-cmd.default-channel-id")
        if (Util.checkForChannelInString(msg)) {
            val channelMap = Util.getChannelFromString(msg)
            channel = channelMap["channel"]
            msg = channelMap["msg"]
        }
        if (channel == null) {
            sender.sendMessage("Channel is invalid. Check config's default-channel-id or specify a real channel with <CHANNEL:channel-id> in your message.")
        } else {
            JDAMethods.sendMessageDiscordChannel(channel, msg, false)
            sender.sendMessage(IridiumColorAPI.process(Util.colorcode(OneAnnouncer.plugin().config.getString("announce-cmd.default-prefix") + "Announced message")))
        }
        return true
    }
}
