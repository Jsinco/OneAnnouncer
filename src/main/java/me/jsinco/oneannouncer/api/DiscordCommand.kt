package me.jsinco.oneannouncer.api

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.bukkit.plugin.Plugin
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

interface DiscordCommand {
    /**
     * The name of the command
     */
    @NotNull fun name(): String

    /**
     * The description of the command
     */
    fun description(): String

    /**
     * Execution code of the command
     * @param event The event of the command
     */
    fun execute(@NotNull event: SlashCommandInteractionEvent)

    /**
     * The options of the command
     */
    fun options(): List<CommandOption>?

    /**
     * The permission of the command, if any
     */
    fun permission(): Permission?

    /**
     * The plugin that registered the command
     */
    @Nullable fun plugin(): Plugin?
}