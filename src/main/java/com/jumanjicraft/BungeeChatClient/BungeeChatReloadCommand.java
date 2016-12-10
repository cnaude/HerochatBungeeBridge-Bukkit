/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumanjicraft.BungeeChatClient;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author cnaude
 */
public class BungeeChatReloadCommand implements CommandExecutor {

    final BungeeChatClient plugin;

    public BungeeChatReloadCommand(BungeeChatClient plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
        if (sender.hasPermission("BungeeChat.reload")) {
            plugin.loadConfig();
            sender.sendMessage("BungeeChat configuration reloaded.");
        } else {
            sender.sendMessage(ChatColor.RED + "No permission to run this.");
        }
        return true;
    }

}
