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
     * @param player
     */
    public void TransmitChatMessage(ChatMessage cm, Player player) {
        ByteArrayDataOutput data = ByteStreams.newDataOutput();

        /* SubChannel */
        data.writeUTF(cm.getSubChannel());

        /* Herochat tokens */
        data.writeUTF(cm.getChannel());
        data.writeUTF(cm.getMessage());
        data.writeUTF(cm.getSender());
        data.writeUTF(cm.getHeroColor());
        data.writeUTF(cm.getHeroNick());

        /* Vault tokens */
        data.writeUTF(cm.getPlayerPrefix());
        data.writeUTF(cm.getPlayerSuffix());
        data.writeUTF(cm.getGroupPrefix());
        data.writeUTF(cm.getGroupSuffix());
        data.writeUTF(cm.getPlayerGroup());

        player.sendPluginMessage(plugin, "BungeeCord", data.toByteArray());

    }

    /**
     *
     * @param pluginChannel
     * @param bytes
     * @param player
     */
    @Override
    public void onPluginMessageReceived(String pluginChannel, Player player, byte[] bytes) {
        if (!pluginChannel.equalsIgnoreCase("BungeeCord")) {
            return;
        }

        String messageType;
        String destination;
        String subChannel;
        ByteArrayDataInput in;

        try {
            in = ByteStreams.newDataInput(bytes);
            messageType = in.readUTF();
            destination = in.readUTF();
            subChannel = in.readUTF();
        } catch (Exception ex) {
            plugin.logDebug(ex.getMessage());
            return;
        }
        plugin.logDebug("Received message: [t: " + messageType + "] [d: "
                + destination + "] [s: " + subChannel + "]");
        if (subChannel.equals("PurpleBungeeIRC")) {
            byte[] msgBytes = new byte[in.readShort()];
            in.readFully(msgBytes);
            processMessage(ByteStreams.newDataInput(msgBytes));
        }

    }

    private void processMessage(ByteArrayDataInput in) {
        String messageType = in.readUTF();
        String channelName = in.readUTF();
        switch (messageType) {
            case "CHAT":
                Channel channel = ChannelManager.getInstance().getChannel(channelName);
                if ((channel == null) || (!channel.isCrossServer())) {
                    return;
                }
                channel.sendRawMessage(in.readUTF());

                break;
            case "PM":
                break;
        }
    }

}
