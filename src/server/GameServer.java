package server;

import game.Direction;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameServer {
	
	private static final long cleanupInactivityMs = 20000;
	private static final long msBetweenCleanups = 2000;
	
	private Map<PortIP, ServerPlayer> players = new HashMap<>();
	private int nextPlayerId = 0;
	private volatile boolean shutdownInitiated = false;
	private String[] mapLayout = new String[] {
			"wwwewwwwwwwwwwwwww",
			"weeeeeeeeeeeeeeeew",
			"wewweeewweeeeeeeew",
			"weeeeeeeweeeewwwew",
			"weeeeeewweeeeweeew",
			"wwwweeeeeeeeeweeew",
			"weeeeeewweeeeeeeew",
			"wwwewwwwwwwwwwwwww"
	};
	
	//TODO  constructor
	
	
	
	
	public Set<ServerPlayer> getPlayers() {
		Set<ServerPlayer> copy;
		synchronized (players) {
			copy = new HashSet<>(players.values());
		}
		return copy;
	}
	
	public ServerPlayer getPlayer(InetAddress address, int port) {
		PortIP soc = new PortIP(address, port);
		ServerPlayer out;
		synchronized (players) {
			out = players.get(soc);
		}
		return out;
	}
	
	public String[] getMapLayout() {
		return Arrays.copyOf(mapLayout, mapLayout.length);
	}

	
	public ServerPlayer createPlayer(String playerName, InetAddress address, int port) {
		ServerPlayer out = null;
		synchronized (players) {
			boolean playerExists = false;
			for (ServerPlayer sp : players.values()) {
				if (sp.getName().equals(playerName)) {
					playerExists = true;
					break;
				}
			}
			if (!playerExists) {
				PortIP pip = new PortIP(address, port);
				if (players.containsKey(pip)) {
					out = players.get(pip);
				} else {
					out = new ServerPlayer(playerName, nextPlayerId, address,
							port);
					nextPlayerId++;
					players.put(pip, out);
				}
			}
		}
		return out;
	}
	
	public void removePlayer(InetAddress address, int port) {
		PortIP pip = new PortIP(address, port);
		removePlayer(pip);
	}

	public void removePlayer(PortIP pip) {
		synchronized (players) {
			@SuppressWarnings("unused")
			ServerPlayer player = players.remove(pip);
		}
	}

	public void movePlayer(ServerPlayer player, Direction direction) {
		//TODO
		
	}
	
	public void shoot(ServerPlayer player) {
		// TODO Auto-generated method stub
		
	}
	
	private void cleanupDeadPlayers() {
		Map<PortIP, ServerPlayer> copy;
		synchronized (players) {
			copy = new HashMap<>(players);
		}
		long currentTime = System.currentTimeMillis();
		long threshold = currentTime - cleanupInactivityMs;
		for (Map.Entry<PortIP, ServerPlayer> pEntry: copy.entrySet()) {
			if (pEntry.getValue().getLastLifeSign() < threshold) {
				removePlayer(pEntry.getKey());
			}
		}
	}
	
	
	private class CleanupThread implements Runnable {

		@Override
		public void run() {
			while (!shutdownInitiated) {
				try {
					cleanupDeadPlayers();
					synchronized (this) {
						wait(msBetweenCleanups);
					}
				} catch (Exception e) {
					//Do Nothing
				}
			}
		}
		
	}
}
