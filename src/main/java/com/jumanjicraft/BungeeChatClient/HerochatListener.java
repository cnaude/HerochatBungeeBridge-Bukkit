package com.jumanjicraft.BungeeChatClient;

import com.dthielke.api.event.ChannelChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class HerochatListener implements Listener {

    private final BungeeChatClient plugin;

    public HerochatListener(BungeeChatClient plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChannelChatEvent(ChannelChatEvent event) {
        Player player = event.getChatter().getPlayer();
        ChatMessage cm = new ChatMessage();
        
        cm.setSubChannel("PurpleBungeeIRC");

        cm.setMessage(event.getMessage());
        cm.setChannelName(event.getChannel().getName());
        cm.setSenderName(player.getName());
        cm.setHeroColor(event.getChannel().getColor().toString());
        cm.setHeroNick(event.getChannel().getNick());

        cm.setPlayerPrefix(plugin.getPlayerPrefix(player));
        cm.setPlayerSuffix(plugin.getPlayerSuffix(player));
        cm.setGroupPrefix(plugin.getGroupPrefix(player));
        cm.setGroupSuffix(plugin.getGroupSuffix(player));
        cm.setGroup(plugin.getPlayerGroup(player));

        plugin.logDebug("Transmitting message to BungeeCord: <" + cm.getChannel() + "><" + cm.getSender() + "> " + cm.getMessage());
        plugin.getBungeeChatListener().TransmitChatMessage(cm);
    }

}
