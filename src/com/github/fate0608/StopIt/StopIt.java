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
    private boolean _shouldWhiteListBeEnabled = this.getConfig().getBoolean("StopIt.commands.WhitelistOnProcess.messages.WhitelistOnProcess");
    ConsoleCommandSender ccs = this.getServer().getConsoleSender();

	@Override
	public void onEnable()
    {
        _server = getServer();
        ccs.sendMessage(ChatColor.GOLD + "[StopIt] wurde erfolgreich aktiviert.");
        ccs.sendMessage("[StopIt] by f3rd");
        _server.setWhitelist(false);
        this.reloadConfig();
        this.getConfig().options().header("Stop-It by f3rd" +
                "Servername = Euer Servername" +
                "TS3-IP: Hier könnt ihr eure TS3-IP eintragen" +
                "Homepage: Hier könnt ihr eure Homepage eintragen" +
                "WhitelistOnProcess: true:Aktiviert die Whitelist, wenn das Plugin ausgeführt wird. false:Deaktiviert sie." +
                "autostopper: true: Stoppt den Server nach erreichen des Countdowns.");
        this.getConfig().addDefault("StopIt.commands.servername.messages.servername","SERVERNAME HIER");
        this.getConfig().addDefault("StopIt.commands.ts3ip.messages.ts3ip","ts3ip");
        this.getConfig().addDefault("StopIt.commands.homepage.messages.homepage","HOMEPAGE HIER");
        this.getConfig().addDefault("StopIt.commands.WhitelistOnProcess.messages.WhitelistOnProcess",true);
        this.getConfig().addDefault("StopIt.commands.autostopper.messages.autostopper",false);
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

if(sender instanceof Player){
        Player player = null;
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
                    _server.setWhitelist(false);
                    _isInProcess = false;
                    return true;
                }

                if(args.length > 0 && args[0].equalsIgnoreCase("now"))
                {
                    AnnounceShutdown();

                    if(_shouldWhiteListBeEnabled)
                    {
                        SetWhitelist();
                    }
                    _server.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                        public void run() {
                                for (Player p : _server.getOnlinePlayers())
                                {
                                    KickPlayer(p);
                                }
                                SaveWorldsAndShutdown();
                        }
                    }, 200);

                    return true;
                }

                if(args.length == 1 && args[0].equalsIgnoreCase("help"))
                {
                    player.sendMessage(ChatColor.RED + "Server-Restart Plugin");
                    player.sendMessage(ChatColor.GOLD + "~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    player.sendMessage(ChatColor.AQUA + " | Syntax: /stopit [Zeit] [Grund]");
                    player.sendMessage(ChatColor.AQUA + " | Plugin-Dev: f3rd");
                    player.sendMessage(ChatColor.GOLD + "~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    return true;
                }

                if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                    ccs.sendMessage(ChatColor.RED + "[StopIt] Reloading Plugin");
                    this.getPluginLoader().disablePlugin(this);
                    this.getPluginLoader().enablePlugin(this);
                    ccs.sendMessage(ChatColor.RED + "[StopIt] Reload complete");
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
                        PostAnnouncementOne();
                        if(_shouldWhiteListBeEnabled)
                        {
                            SetWhitelist();
                        }
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
                                PostRepeatingAnnouncement();
                            }
                        }, (_countdownInMin-1)*60*20+100, 400L);

                        _server.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

                            public void run() {
                                AnnounceShutdown();
                                playersShouldBeKicked = true;
                            }
                        }, _countdownInMin*60*20-60);

                        _server.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

                            public void run() {

                                AnnounceShutdown();

                                if (playersShouldBeKicked) {

                                    for (Player p : _server.getOnlinePlayers()) {
                                        KickPlayer(p);
                                    }

                                    SaveWorldsAndShutdown();
                                }
                            }
                        }, _countdownInMin * 60 * 20 + 60);

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

    private void SetWhitelist()
    {
        _server.setWhitelist(true);
    }

    private void AnnounceShutdown() {
        _server.broadcastMessage(ChatColor.RED + "Der Server wird nun heruntergefahren!");
        _server.broadcastMessage(ChatColor.GOLD + "Besucht uns solang gern auf unserer Homepage!");
        _server.broadcastMessage(ChatColor.AQUA + getConfig().getString("StopIt.commands.homepage.messages.homepage"));
        _server.broadcastMessage(ChatColor.WHITE + "Bis gleich! Euer " + getConfig().getString("StopIt.commands.servername.messages.servername") + " Team!");
    }

    private void PostRepeatingAnnouncement() {
        _server.broadcastMessage(ChatColor.GOLD + "\n\n\n\n\n\n\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~");
        _server.broadcastMessage(ChatColor.RED + "Achtung " + ChatColor.WHITE + "der Server startet in kurzer Zeit neu.");
        _server.broadcastMessage(ChatColor.WHITE + "Ihr werdet automatisch gekickt.");
        _server.broadcastMessage(ChatColor.GOLD + "\n\n\n\n\n\n\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    private void PostAnnouncementOne() {
        _server.broadcastMessage(ChatColor.GOLD + "\n\n\n\n\n\n\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~");
        _server.broadcastMessage(ChatColor.WHITE + " Liebe Spieler, der Server wird ");
        _server.broadcastMessage(ChatColor.WHITE + " in " + ChatColor.RED + _countdownInMin + ChatColor.WHITE + " Minute(n) neugestartet... ");
        _server.broadcastMessage(ChatColor.WHITE + " Grund: " + ChatColor.RED + _reason);
        _server.broadcastMessage(ChatColor.WHITE + " Wir sind in wenigen Minuten wieder für euch da!");
        _server.broadcastMessage(ChatColor.WHITE + " Sollten Fragen auftregen: TS3-IP: " + ChatColor.RED + this.getConfig().getString("StopIt.commands.ts3ip.messages.ts3ip"));
        _server.broadcastMessage(ChatColor.GOLD + "~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    private void SaveWorldsAndShutdown() {
        _server.broadcastMessage(ChatColor.RED + "Welten und Inventare werden gespeichert...");
        _server.dispatchCommand(ccs, "save-all");
        _isInProcess = false;

        if(getConfig().getBoolean("StopIt.commands.autostopper.messages.autostopper"))
        {
            _server.shutdown();
        }
    }

    private void KickPlayer(Player p)
    {
        if(p.hasPermission("stopit.ignoreOnKick"))
        {
            p.sendMessage("Du wurdest nicht gekickt.");
        }
        else
        {
            p.kickPlayer(ChatColor.GOLD + "Alle wurden wegen eines Restarts ausgeloggt. Bis gleich hoffentlich! :)\n" +
                    ChatColor.GREEN + "Besuch uns gern auf " + getConfig().getString("StopIt.commands.homepage.messages.homepage") + "\n" +
                    ChatColor.AQUA + "Oder auf dem TS3: " + getConfig().getString("StopIt.commands.ts3ip.messages.ts3ip"));
        }
    }

    @Override
	public void onDisable()
    {
        ccs.sendMessage(ChatColor.RED + " [StopIt] wurde deaktiviert.");
	}

}
