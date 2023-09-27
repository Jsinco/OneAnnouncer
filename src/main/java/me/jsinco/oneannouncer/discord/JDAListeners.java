package me.jsinco.oneannouncer.discord;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import me.jsinco.oneannouncer.OneAnnouncer;
import me.jsinco.oneannouncer.Util;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class JDAListeners extends ListenerAdapter {

    @Override // Relay
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getMember() == null) return;
        else if (!event.getMember().hasPermission(Permission.MESSAGE_MENTION_EVERYONE)) return;
        else if (OneAnnouncer.plugin().getConfig().getStringList("relay.ignore-guilds").contains(event.getGuild().getId())) return;

        List<String> listenFor = OneAnnouncer.plugin().getConfig().getStringList("relay.listen-for");
        String message = event.getMessage().getContentRaw();
        for (String word : listenFor) {
            if (!message.contains(word)) continue;

            String prefix = OneAnnouncer.plugin().getConfig().getString("relay.default-prefix");
            if (Util.checkForPrefixInString(message)) {
                Map<String, String> prefixMap = Util.getPrefixFromString(message);
                prefix = prefixMap.get("prefix");
                message = prefixMap.get("msg");
            }

            String[] splitMsg = message.split(" ");
            if (splitMsg.length > OneAnnouncer.plugin().getConfig().getInt("relay.max-relay-length")) {
                Bukkit.broadcastMessage(IridiumColorAPI.process(Util.colorcode(prefix + OneAnnouncer.plugin().getConfig().getString("relay.replacement-message"))));
            } else {
                Bukkit.broadcastMessage(IridiumColorAPI.process(Util.colorcode(prefix + message)));
            }
            break;
        }
    }
}
