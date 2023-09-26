package me.jsinco.oneannouncer

import github.scarsz.discordsrv.DiscordSRV
import java.util.UUID

object DiscordSRVUtil {

    fun getDiscordIDFromUUID(uuid: UUID): String? {
        return DiscordSRV.getPlugin().accountLinkManager.getDiscordId(uuid)
    }

    fun getUUIDFromDiscordID(discordID: String): UUID? {
        return DiscordSRV.getPlugin().accountLinkManager.getUuid(discordID)
    }
}
