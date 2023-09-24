package me.jsinco.oneannouncer.discord;

import github.scarsz.discordsrv.DiscordSRV;

import java.util.UUID;

public class DiscordSRVUtil {

    DiscordSRV discordSRV = DiscordSRV.getPlugin();


    public String getLinkedAccountID(String minecraftUUID) {
        return discordSRV.getAccountLinkManager().getDiscordId(UUID.fromString(minecraftUUID));
    }


    public boolean sendMessageFromMinecraft(String minecraftUUID, String msg) {
        String discordID = getLinkedAccountID(minecraftUUID);
        if (discordID != null) {
            JDAMethods.sendMessageDiscordUser(discordID, msg);
        }
        return discordID != null;
    }
}
