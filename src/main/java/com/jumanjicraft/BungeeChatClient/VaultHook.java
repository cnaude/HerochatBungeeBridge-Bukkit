/*
 * Copyright (C) 2014 cnaude
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jumanjicraft.BungeeChatClient;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 *
 * @author cnaude
 */
public class VaultHook {
    
    final BungeeChatClient plugin;
    
    /**
     *
     */
    public Permission permission;    

    /**
     *
     */
    public Chat chat;
    
    /**
     *
     * @param plugin
     */
    public VaultHook(BungeeChatClient plugin) {
        this.plugin = plugin;
        
        setupPermissions();
        setupChat();
    }
    
    private void setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }        
    }

    private void setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }        
    }   
}
