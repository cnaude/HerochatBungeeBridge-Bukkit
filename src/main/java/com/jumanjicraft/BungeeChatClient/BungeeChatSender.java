package com.jumanjicraft.BungeeChatClient;

import com.dthielke.Herochat;
import com.dthielke.api.Channel;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class BungeeChatSender implements PluginMessageListener {

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
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeChat", this);
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeChat");
    }

    /**
     *
     * @param cm
     */
    public void TransmitChatMessage(ChatMessage cm) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        /* My custom tag */
        out.writeUTF("PurpleBungeeIRC");

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

        plugin.getServer().sendPluginMessage(plugin, "BungeeChat", out.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(String tag, Player player, byte[] data) {
        if (!tag.equalsIgnoreCase("BungeeChat")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(data);
        String chatchannel = in.readUTF();
        String message = in.readUTF();
        plugin.logDebug("chatChannel: " + chatchannel);
        plugin.logDebug("message: " + message);
        Channel channel = Herochat.getChannelManager().getChannel(chatchannel);
        if (channel == null) {
            return;
        }

        channel.sendRawMessage(message);

    }

}
