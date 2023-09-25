package me.jsinco.oneannouncer;

import me.jsinco.oneannouncer.api.DiscordCommandManager;
import me.jsinco.oneannouncer.commands.Announce;
import me.jsinco.oneannouncer.commands.Say;
import me.jsinco.oneannouncer.discord.JDAListeners;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class OneAnnouncer extends JavaPlugin implements CommandExecutor {

    private static OneAnnouncer instance;
    private static JDA jda;

    public boolean setupJDA() throws InterruptedException {


        String botToken = OneAnnouncer.plugin().getConfig().getString("bot-token");
        if (botToken == null || botToken.equals("YOUR_BOT_TOKEN")) {
            this.getLogger().info("Bot token is invalid or not set. Not enabling discord features.");
            return false;
        }

        jda = JDABuilder.createDefault(botToken)
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new JDAListeners())
                .addEventListeners(DiscordCommandManager.INSTANCE)
                .build().awaitReady();
        TextChannel channel;
        String stringChannel = getConfig().getString("announce-cmd.default-channel-id");

        if (stringChannel.equals("CHANNEL_ID")) {
            this.getLogger().warning("Default channel is invalid. Cannot invoke /announce command.");
            return true;
        }


        try {
            for (Guild guild : jda.getGuilds()) {
                guild.upsertCommand("announce", "Announce a message to Minecraft")
                        .addOption(OptionType.STRING, "msg", "The message to announce", true)
                        .queue();
            }
        } catch (Exception e) {
            this.getLogger().warning("Debug");
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        reloadConfig();
        try {
            if (setupJDA()) {
                this.getLogger().info("Discord features enabled.");
            } else {
                this.getLogger().info("Discord features not enabled.");
            }
            sender.sendMessage("Reloaded config.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        instance = this;

        try {
            setupJDA();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Say();
        new AutoAnnouncer();
        new Announce();
        getCommand("onereload").setExecutor(this);


    }

    @Override
    public void onDisable() {
    }

    public static OneAnnouncer plugin() {
        return instance;
    }

    public static JDA getJDA() {
        return jda;
    }
}
