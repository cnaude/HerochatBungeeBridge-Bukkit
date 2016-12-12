package com.jumanjicraft.BungeeChatClient;

import com.dthielke.api.Channel;
import com.dthielke.channel.ChannelManager;
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
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", this);
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
    }

    /**
     *
     * @param cm
     */
    public void TransmitChatMessage(ChatMessage cm) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        /* SubChannel */
        out.writeUTF(cm.getSubChannel());

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

        plugin.getServer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

    /**
     *
     * @param tag
     * @param player
     * @param data
     */
    @Override
    public void onPluginMessageReceived(String tag, Player player, byte[] data) {
        if (!tag.equalsIgnoreCase("BungeeCord")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(data);

        String subChannel = in.readUTF();
        String channelName = in.readUTF();
        String message = in.readUTF();
        plugin.logDebug("SubChannel: " + subChannel);

        if (subChannel.equals("PurpleBungeeIRC")) {
            Channel channel = ChannelManager.getInstance().getChannel(channelName);
            channel.sendRawMessage(message);
        } else {
            plugin.logDebug("Invalid SubChannel");
        }

    }

}
