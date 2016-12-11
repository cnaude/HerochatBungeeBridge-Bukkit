package com.jumanjicraft.BungeeChatClient;

import com.dthielke.api.event.ChannelChatEvent;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class BungeeHeroListener implements Listener {

    private final BungeeChatClient plugin;
    Hasher hasher = Hashing.md5().newHasher();

    public BungeeHeroListener(BungeeChatClient plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChannelChatEvent(ChannelChatEvent event) {
        Player player = event.getChatter().getPlayer();
        ChatMessage cm = new ChatMessage();

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

        String time = String.valueOf(System.currentTimeMillis());
        String unencodedMessage = time + player.getName() + event.getMessage();

        hasher.putBytes(unencodedMessage.getBytes());
        String md5 = hasher.hash().toString();
        cm.setToken(md5);

        plugin.logDebug("Transmitting message to BungeeCord: <" + cm.getChannel() + "><" + cm.getSender() + "> " + cm.getMessage());
        plugin.getBungeeChatListener().TransmitChatMessage(cm);
    }

}
