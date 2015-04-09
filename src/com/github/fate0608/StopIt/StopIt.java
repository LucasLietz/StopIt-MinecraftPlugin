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
    private int _timeLeft;
    private String _reason;
    private boolean _isInProcess = false;
    ConsoleCommandSender ccs = this.getServer().getConsoleSender();

	@Override
	public void onEnable()
    {
        ccs.sendMessage(ChatColor.GOLD + "[StopIt] wurde erfolgreich aktiviert.");
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
                if(args[0].equalsIgnoreCase("cancel"))
                {
                    _server.broadcastMessage(ChatColor.RED + "Server-Restart abgebrochen!");
                    _server.getScheduler().cancelAllTasks();
                    _isInProcess = false;
                    return true;
                }

                if (args.length == 2)
                {
                    if (!_isInProcess)
                    {
                    try{
                        _isInProcess = true;
                        _countdownInMin = Integer.valueOf(args[0]).intValue();
                        _timeLeft = _countdownInMin;
                        _reason = args[1].toString();
                        _server.broadcastMessage(ChatColor.GOLD + "\n\n\n\n\n\n\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~");
                        _server.broadcastMessage(ChatColor.WHITE + " Liebe Spieler, der Server wird ");
                        _server.broadcastMessage(ChatColor.WHITE + " in " + ChatColor.RED + _countdownInMin + ChatColor.WHITE + " Minute(n) wegen " + ChatColor.RED + _reason);
                        _server.broadcastMessage(ChatColor.WHITE + " neugestartet...");
                        _server.broadcastMessage(ChatColor.WHITE + " Wir sind in wenigen Minuten wieder für euch da!");
                        _server.broadcastMessage(ChatColor.WHITE + " Sollten Fragen auftregen: TS3-IP: justminecraft.de");
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
                        _timeLeft=0;

                        return false;
                    }

                        _server.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

                            public void run() {
                                _server.broadcastMessage(ChatColor.GOLD + "\n\n\n\n\n\n\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~");
                                _server.broadcastMessage(ChatColor.WHITE + "Achtung der Server startet in kurzer Zeit neu.");
                                _server.broadcastMessage(ChatColor.WHITE + "Bitte verlasst rechtzeitig den Server.");
                                _server.broadcastMessage(ChatColor.GOLD + "\n\n\n\n\n\n\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~");
                            }
                        }, 100L, 200L);

                        _server.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

                            public void run() {
                                _server.broadcastMessage(ChatColor.RED  + "Der Server wird nun heruntergefahren!");
                                _server.broadcastMessage(ChatColor.GOLD + "Besucht uns solang gern auf unserer Homepage!");
                                _server.broadcastMessage(ChatColor.AQUA + "www.justminecraft.de");
                                _server.broadcastMessage(ChatColor.WHITE + "Bis gleich! Euer JMC Team!");
                                playersShouldBeKicked = true;
                            }
                        }, _countdownInMin*60*20);

                        _server.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

                            public void run() {
                                if(playersShouldBeKicked)
                                {
                                    for (Player p : _server.getOnlinePlayers()) {
                                        p.kickPlayer(ChatColor.GOLD + "Alle wurden wegen eines Restarts ausgeloggt. Bis gleich hoffentlich! :)\n" +
                                                     ChatColor.GREEN + "Besuch uns gern auf www.justminecraft.de\n" +
                                                     ChatColor.AQUA + "Oder auf dem TS3: justminecraft.de");
                                    }
                                    _server.dispatchCommand(ccs, "save-all");
                                    _isInProcess = false;

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
	public void onDisable(){

        ccs.sendMessage(ChatColor.RED + " [StopIt] wurde deaktiviert.");
	}

}
