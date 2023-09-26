package me.jsinco.oneannouncer.commands.discord

import com.iridium.iridiumcolorapi.IridiumColorAPI
import me.jsinco.oneannouncer.OneAnnouncer
import me.jsinco.oneannouncer.Util
import me.jsinco.oneannouncer.api.CommandOption
import me.jsinco.oneannouncer.api.DiscordCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import org.bukkit.Bukkit
import org.jetbrains.annotations.NotNull

class AnnounceCommand : DiscordCommand {

    @NotNull
    override fun name(): String {
        return "announce"
    }

    override fun description(): String {
        return "Announce a message to Minecraft"
    }

    override fun execute(@NotNull event: SlashCommandInteractionEvent) {
        val option = event.getOption("msg")!!
        val prefixOption = event.getOption("prefix")

        var msg: String? = option.asString
        msg = Util.addPlaceholders("announce-cmd.placeholders", msg)

        var prefix = OneAnnouncer.plugin().config.getString("announce-cmd.default-prefix")
        if (Util.checkForPrefixInString(msg)) {
            val prefixMap = Util.getPrefixFromString(msg)
            prefix = prefixMap["prefix"]
            msg = prefixMap["msg"]
        } else if (prefixOption != null) {
            prefix = prefixOption.asString
        }

        Bukkit.broadcastMessage(IridiumColorAPI.process(Util.colorcode("$prefix $msg")))
        event.reply("Announced: **$msg**").queue()
    }

    override fun options(): List<CommandOption> {
        val option = CommandOption(OptionType.STRING, "msg", "The message to send", true)
        val option2 = CommandOption(OptionType.STRING, "prefix", "The prefix to use", false)
        return listOf(option, option2)
    }

    override fun permission(): Permission? {
        return null
    }
}