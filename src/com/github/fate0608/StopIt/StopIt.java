package com.github.fate0608.StopIt;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class StopIt extends JavaPlugin
{
    private Server _server;

    private int _countdownInMin;
    private boolean playersShouldBeKicked = false;
    private String _reason;
    private boolean _isInProcess = false;
    private String[] _excluded;
    ConsoleCommandSender ccs = this.getServer().getConsoleSender();

	@Override
	public void onEnable()
    {
        ccs.sendMessage(ChatColor.GOLD + "[StopIt] wurde erfolgreich aktiviert.");
        ccs.sendMessage("[StopIt] by f3rd");
        this.reloadConfig();
        this.getConfig().options().header("StopIt-Plugin. Hier kannst du das Plugin vollständig konfigurieren.\n Servername, " +
                "Ts3Ip, Homepage sollten selbsterklärend sein. ExcludedPlayers sind solche, die beim " +
                "Kick der Spieler nicht gekickt werden. Der Autostopper stoppt den Server bei true oder nicht bei false.");
        this.getConfig().addDefault("StopIt.commands.servername.messages.servername","SERVERNAME HIER");
        this.getConfig().addDefault("StopIt.commands.ts3ip.messages.ts3ip","ts3ip");
        this.getConfig().addDefault("StopIt.commands.homepage.messages.homepage","HOMEPAGE HIER");
        this.getConfig().addDefault("StopIt.commands.excludeplayer.messages.excludeplayer","");
        this.getConfig().addDefault("StopIt.commands.autostopper.messages.autostopper","false");
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        _server = getServer();

        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("stopit"))
            {
                if (!player.hasPermission("stopit.announce"))
                {
                    return false;
                }


                if(args.length > 0 && args[0].equalsIgnoreCase("cancel"))
                {
                    _server.broadcastMessage(ChatColor.RED + "Server-Restart abgebrochen!");
                    _server.getScheduler().cancelAllTasks();
                    _isInProcess = false;
                    return true;
                }

                if(args.length == 1 && args[0].equalsIgnoreCase("help"))
                {
                    _server.broadcastMessage(ChatColor.RED + "Server-Restart Plugin");
                    _server.broadcastMessage(ChatColor.GOLD + "~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    _server.broadcastMessage(ChatColor.AQUA + " | Syntax: /stopit [Zeit] [Grund]");
                    _server.broadcastMessage(ChatColor.AQUA + " | Plugin-Dev: f3rd");
                    _server.broadcastMessage(ChatColor.AQUA + " | Syntax: /stopit [Zeit] [Grund]");
                    _server.broadcastMessage(ChatColor.GOLD + "~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    return true;
                }

                if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                    _server.broadcastMessage(ChatColor.RED + "[StopIt] Reloading Plugin");
                    this.getPluginLoader().disablePlugin(this);
                    this.getPluginLoader().enablePlugin(this);
                    _server.broadcastMessage(ChatColor.RED + "[StopIt] Reload complete");
                    return true;
                }

                if(args.length < 2 || args == null)
                {
                    player.sendMessage(ChatColor.RED + "Du hast zu wenige Argumente übergeben!");
                    player.sendMessage(ChatColor.RED + "Syntax: /stopit [Zeit bis Restart] [Grund]");
                    return false;
                }

                if (args.length == 2)
                {
                    if (!_isInProcess)
                    {
                    try{
                        _isInProcess = true;
                        _countdownInMin = Integer.valueOf(args[0]).intValue();
                        _reason = args[1].toString();
                        _server.broadcastMessage(ChatColor.GOLD + "\n\n\n\n\n\n\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~");
                        _server.broadcastMessage(ChatColor.WHITE + " Liebe Spieler, der Server wird ");
                        _server.broadcastMessage(ChatColor.WHITE + " in " + ChatColor.RED + _countdownInMin + ChatColor.WHITE + " Minute(n) neugestartet... ");
                        _server.broadcastMessage(ChatColor.WHITE + " Grund: " + ChatColor.RED + _reason);
                        _server.broadcastMessage(ChatColor.WHITE + " Wir sind in wenigen Minuten wieder für euch da!");
                        _server.broadcastMessage(ChatColor.WHITE + " Sollten Fragen auftregen: TS3-IP: " + ChatColor.RED + this.getConfig().getString("StopIt.commands.ts3ip.messages.ts3ip"));
                        _server.broadcastMessage(ChatColor.GOLD + "~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    }
                    catch (Exception ex)
                    {
                        player.sendMessage(ChatColor.RED + "Fehler! Überprüfe die Eingaben!");
                        player.sendMessage(ChatColor.RED + "Verwendung: /stopit " + ChatColor.GREEN + "[Zeit zum Restart in Minuten] [Grund]");
                        _countdownInMin = 0;
                        _isInProcess=false;
                        _reason="";
                        playersShouldBeKicked = false;

                        return false;
                    }

                        _server.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

                            public void run() {
                                _server.broadcastMessage(ChatColor.GOLD + "\n\n\n\n\n\n\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~");
                                _server.broadcastMessage(ChatColor.RED + "Achtung " + ChatColor.WHITE + "der Server startet in kurzer Zeit neu.");
                                _server.broadcastMessage(ChatColor.WHITE + "Ihr werdet automatisch gekickt.");
                                _server.broadcastMessage(ChatColor.GOLD + "\n\n\n\n\n\n\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~");
                            }
                        }, (_countdownInMin-1)*60*20+100, 400L);

                        _server.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

                            public void run() {
                                _server.broadcastMessage(ChatColor.RED + "Der Server wird nun heruntergefahren!");
                                _server.broadcastMessage(ChatColor.GOLD + "Besucht uns solang gern auf unserer Homepage!");
                                _server.broadcastMessage(ChatColor.AQUA + getConfig().getString("StopIt.commands.homepage.messages.homepage"));
                                _server.broadcastMessage(ChatColor.WHITE + "Bis gleich! Euer " + getConfig().getString("StopIt.commands.servername.messages.servername") + " Team!");
                                playersShouldBeKicked = true;
                            }
                        }, _countdownInMin*60*20-60);

                        _server.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

                            public void run() {
                                if(playersShouldBeKicked)
                                {

                                    if(getConfig().getString("StopIt.commands.excludeplayer.messages.excludeplayer").contains(",") ||
                                            getConfig().getString("StopIt.commands.excludeplayer.messages.excludeplayer").length() > 0)
                                    {
                                        String actualName = getConfig().getString("StopIt.commands.excludeplayer.messages.excludeplayer");

                                        String[] split= actualName.split(",");
                                        _excluded = split;
                                    }
                                    else
                                    {
                                        String[] test = {""};
                                        _excluded = test;
                                    }

                                    for (Player onlinePlayer : _server.getOnlinePlayers())
                                    {
                                        if(_excluded[0] != "")
                                        {
                                            int amountExcl = _excluded.length-1;
                                            boolean isPlayerInsideExludes = false;
                                            for(int i = 0; i < amountExcl; i++)
                                            {
                                                if(onlinePlayer.getDisplayName() == _excluded[amountExcl].toString())
                                                {
                                                    ccs.sendMessage(onlinePlayer.getDisplayName() + " wird ignoriert.");
                                                    isPlayerInsideExludes = true;
                                                }
                                            }

                                            if(isPlayerInsideExludes)
                                            {
                                                onlinePlayer.kickPlayer(ChatColor.GOLD + "Alle wurden wegen eines Restarts ausgeloggt. Bis gleich hoffentlich! :)\n" +
                                                        ChatColor.GREEN + "Besuch uns gern auf " + getConfig().getString("StopIt.commands.homepage.messages.homepage") + "\n" +
                                                        ChatColor.AQUA + "Oder auf dem TS3: " + getConfig().getString("StopIt.commands.ts3ip.messages.ts3ip"));
                                            }
                                            else {
                                                ccs.sendMessage(onlinePlayer.getDisplayName() + " ignoriert.");
                                            }
                                        }
                                        else
                                        {
                                            onlinePlayer.kickPlayer(ChatColor.GOLD + "Alle wurden wegen eines Restarts ausgeloggt. Bis gleich hoffentlich! :)\n" +
                                                    ChatColor.GREEN + "Besuch uns gern auf " + getConfig().getString("StopIt.commands.homepage.messages.homepage") + "\n" +
                                                    ChatColor.AQUA + "Oder auf dem TS3: " + getConfig().getString("StopIt.commands.ts3ip.messages.ts3ip"));
                                        }
                                    }
                                    _server.broadcastMessage(ChatColor.RED + "Welten und Inventare werden gespeichert...");
                                    _server.dispatchCommand(ccs, "save-all");
                                    _isInProcess = false;

                                    if(getConfig().getString("StopIt.commands.autostopper.messages.autostopper") == "true") _server.shutdown();

                                }
                            }
                        }, _countdownInMin*60*20 + 60);

                        return true;

                    }
                    else
                    {
                        player.sendMessage(ChatColor.GOLD + "Du hast nicht ausreichend oder zu viele Argumente übergeben.");
                        player.sendMessage(ChatColor.RED + "Verwendung: /stopit " + ChatColor.GREEN + "[Zeit zum Restart in Minuten] [Grund]");
                    }
                }

                return false;
            }

            return false;
        }
        return false;
    }


    @Override
	public void onDisable()
    {
        ccs.sendMessage(ChatColor.RED + " [StopIt] wurde deaktiviert.");
	}

}
