package me.jsinco.oneannouncer.api

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.jetbrains.annotations.NotNull

interface DiscordCommand {

    /**
     * The name of the command
     */
    fun name(): String

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
    fun options(): List<CommandOption?>
}