package me.jsinco.oneannouncer.listeners

import me.jsinco.oneannouncer.OneAnnouncer
import me.jsinco.oneannouncer.discord.JDAMethods
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.BroadcastMessageEvent

class BukkitEvents(val plugin: OneAnnouncer) : Listener {

    @EventHandler
    fun onBroadcastEvent(event: BroadcastMessageEvent) {
        if (!plugin.config.getBoolean("relay.relay-broadcasts.enabled")) {
            return
        }

        val message: String = event.message
        val channel: String? = plugin.config.getString("relay.relay-broadcasts.channel-id")

        if (channel == null || channel.equals("CHANNEL_ID")) {
            return
        }

        JDAMethods.sendMessageDiscordChannel(channel, message, false)

    }
}