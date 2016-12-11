package com.jumanjicraft.BungeeChatClient;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BungeeChatSender {

    private final BungeeChatClient plugin;

    /**
     *
     * @param plugin
     */
    public BungeeChatSender(BungeeChatClient plugin) {
        this.plugin = plugin;
        register();
    }
    
    private void register() {
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeHeroChat");
    }    
    
    /**
     *
     * @param cm
     */
    public void TransmitChatMessage(ChatMessage cm) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        
        /* Herochat tokens */
        out.writeUTF(cm.getChannel());
        out.writeUTF(cm.getMessage());
        out.writeUTF(cm.getSender());
        out.writeUTF(cm.getHeroColor());
        out.writeUTF(cm.getHeroNick());
        
        /* Vault tokens */
        out.writeUTF(cm.getPlayerPrefix());
        out.writeUTF(cm.getPlayerSuffix());
        out.writeUTF(cm.getGroupPrefix());
        out.writeUTF(cm.getGroupSuffix());
        out.writeUTF(cm.getPlayerGroup());
        
        plugin.getServer().sendPluginMessage(plugin, "BungeeHeroChat", out.toByteArray());
    }

}
