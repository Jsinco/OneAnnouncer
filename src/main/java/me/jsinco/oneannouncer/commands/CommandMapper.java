package me.jsinco.oneannouncer.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CommandMapper {


    private static final Map<String, BukkitCommand> mappedCommands = new HashMap<>();

    /**
     * Registers a BukkitCommand to the server's command map using reflection, no need to declare in Plugin.yml
     * @param commandName The name of the command to register.
     * @param bukkitCommand The BukkitCommand to register.
     */
    public static void registerBukkitCommand(String commandName, BukkitCommand bukkitCommand) {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());


            commandMap.register(commandName, "solutilities", bukkitCommand);
            mappedCommands.put(commandName, bukkitCommand);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void unRegisterBukkitCommands() {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());


            for (BukkitCommand bukkitCommand : mappedCommands.values()) {
                Command command = commandMap.getCommand(bukkitCommand.getName());
                for (String alias : bukkitCommand.getAliases()) {
                    command.getAliases().remove(alias);
                }
                command.unregister(commandMap);
                Bukkit.getLogger().log(java.util.logging.Level.INFO, "Unregistered command: " + bukkitCommand.getName());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
