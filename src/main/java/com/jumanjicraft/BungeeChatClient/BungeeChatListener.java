package com.jumanjicraft.BungeeChatClient;

import com.dthielke.Herochat;
import com.dthielke.api.Channel;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class BungeeChatListener implements PluginMessageListener {

    private final BungeeChatClient plugin;

    /**
     *
     * @param plugin
     */
    public BungeeChatListener(BungeeChatClient plugin) {
        this.plugin = plugin;
        register();
    }
    
    private void register() {
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeChat", this);
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeChat");
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
        
        /* HashCode for duplicate message workaround */
        out.writeUTF(cm.getToken());
        
        plugin.getServer().sendPluginMessage(plugin, "BungeeChat", out.toByteArray());
    }

    /**
     *
     * @param tag
     * @param player
     * @param data
     */
    @Override
    public void onPluginMessageReceived(String tag, Player player, byte[] data) {
        if (!plugin.rawMessagesEnabled()) {
            return;
        }
        if (!tag.equalsIgnoreCase("BungeeChat")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(data);
        String chatchannel = in.readUTF();
        String message = in.readUTF();
        Channel channel = Herochat.getChannelManager().getChannel(chatchannel);
        if (channel == null) {
            return;
        }
        if (plugin.getPrefixSymbol().isEmpty()) {
            channel.sendRawMessage(message);
        } else {
            channel.sendRawMessage(plugin.getPrefixSymbol() + message);
        }
    }
}
