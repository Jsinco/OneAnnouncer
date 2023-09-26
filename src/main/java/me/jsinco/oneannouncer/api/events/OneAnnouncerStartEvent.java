package me.jsinco.oneannouncer.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class OneAnnouncerStartEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled;

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public OneAnnouncerStartEvent() {
        isCancelled = false;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }
}
