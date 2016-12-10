package com.jumanjicraft.BungeeChatClient;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BungeeChatClient extends JavaPlugin {

    private String prefixSymbol;
    private BungeeChatListener bungeeChatListener;
    public VaultHook vaultHelpers;
    static final Logger LOG = Logger.getLogger("Minecraft");
    public String LOG_HEADER;
    public String LOG_HEADER_F;
    private boolean debugEnabled;
    private File pluginFolder;
    private File configFile;

    @Override
    public void onEnable() {
        LOG_HEADER = "[" + this.getName() + "]";
        LOG_HEADER_F = ChatColor.LIGHT_PURPLE + "[" + this.getName() + "]" + ChatColor.RESET;
        pluginFolder = getDataFolder();
        configFile = new File(pluginFolder, "config.yml");
        createConfigDirs();
        createConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        loadConfig();
        setupVault();
        bungeeChatListener = new BungeeChatListener(this);
        getServer().getPluginManager().registerEvents(new BungeeHeroListener(this), this);
        getCommand("bungeechatreload").setExecutor(new BungeeChatReloadCommand(this));
    }

    public void loadConfig() {
        prefixSymbol = getConfig().getString("prefix-symbol", "");
        debugEnabled = getConfig().getBoolean("debug", false);
        logDebug("Debug enabled.");
    }
    
    public String getPrefixSymbol() {
        return prefixSymbol;
    }
    
    public BungeeChatListener getBungeeChatListener() {
        return bungeeChatListener;
    }
    
    /**
     *
     */
    public void setupVault() {
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            vaultHelpers = new VaultHook(this);            
        } 
    }
    
    /**
     *
     * @param player
     * @return
     */
    public String getPlayerPrefix(Player player) {
        String prefix = "";
        if (vaultHelpers != null) {
            if (vaultHelpers.chat != null) {
                prefix = vaultHelpers.chat.getPlayerPrefix(player);
            }
        }
        if (prefix == null) {
            prefix = "";
        }
        return ChatColor.translateAlternateColorCodes('&', prefix);
    }

    /**
     *
     * @param player
     * @return
     */
    public String getPlayerSuffix(Player player) {
        String suffix = "";
        if (vaultHelpers != null) {
            if (vaultHelpers.chat != null) {
                suffix = vaultHelpers.chat.getPlayerSuffix(player);
            }
        }
        if (suffix == null) {
            suffix = "";
        }
        return ChatColor.translateAlternateColorCodes('&', suffix);
    }
    
    /**
     *
     * @param player
     * @return
     */
    public String getGroupPrefix(Player player) {
        String prefix = "";
        if (vaultHelpers != null) {
            if (vaultHelpers.chat != null) {
                String group = "";
                try {
                    group = vaultHelpers.permission.getPrimaryGroup(player);
                } catch (Exception ex) {
                }
                if (group == null) {
                    group = "";
                }
                prefix = vaultHelpers.chat.getGroupPrefix(player.getLocation().getWorld(), group);
            }
        }
        if (prefix == null) {
            prefix = "";
        }
        return ChatColor.translateAlternateColorCodes('&', prefix);
    }
    
    /**
     *
     * @param player
     * @return
     */
    public String getGroupSuffix(Player player) {
        String suffix = "";
        if (vaultHelpers != null) {
            if (vaultHelpers.chat != null) {
                String group = "";
                try {
                    group = vaultHelpers.permission.getPrimaryGroup(player);
                } catch (Exception ex) {
                }
                if (group == null) {
                    group = "";
                }
                suffix = vaultHelpers.chat.getGroupSuffix(player.getLocation().getWorld(), group);
            }
        }
        if (suffix == null) {
            suffix = "";
        }
        return ChatColor.translateAlternateColorCodes('&', suffix);
    }
    
    /**
     *
     * @param player
     * @return
     */
    public String getPlayerGroup(Player player) {
        String groupName = "";
        if (vaultHelpers != null) {
            if (vaultHelpers.permission != null) {
                try {
                    groupName = vaultHelpers.permission.getPrimaryGroup(player);
                } catch (Exception ex) {
                }
            }
        }
        if (groupName == null) {
            groupName = "";
        }
        return ChatColor.translateAlternateColorCodes('&', groupName);
    }
    
    /**
     *
     * @param message
     */
    public void logInfo(String message) {
        LOG.log(Level.INFO, String.format("%s %s", LOG_HEADER, message));
    }

    /**
     *
     * @param message
     */
    public void logError(String message) {
        LOG.log(Level.SEVERE, String.format("%s %s", LOG_HEADER, message));
    }

    /**
     *
     * @param message
     */
    public void logDebug(String message) {
        if (debugEnabled) {
            LOG.log(Level.INFO, String.format("%s [DEBUG] %s", LOG_HEADER, message));
        }
    }
    
    /**
     *
     * @param sender
     */
    public void reloadMainConfig(CommandSender sender) {
        sender.sendMessage(LOG_HEADER_F + " Reloading config.yml ...");
        reloadConfig();
        getConfig().options().copyDefaults(false);
        loadConfig();
        sender.sendMessage(LOG_HEADER_F + " Done.");
    }

    private void createConfigDirs() {
        if (!pluginFolder.exists()) {
            try {
                logInfo("Creating " + pluginFolder.getAbsolutePath());
                pluginFolder.mkdir();
            } catch (Exception e) {
                logError(e.getMessage());
            }
        }
    }

    private void createConfig() {
        if (!configFile.exists()) {
            try {
                logInfo("Creating config.yml");
                configFile.createNewFile();
            } catch (IOException e) {
                logError(e.getMessage());
            }
        }

    }

}
