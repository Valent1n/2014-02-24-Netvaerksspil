package server;

import game.Direction;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameServer implements Closeable {
	
	private static final long cleanupInactivityMs = 1000*20;
	private static final long msBetweenCleanups = 2000;
	private static final String[] defaultMapLayout = new String[] {
		"wwwwwwwwwwwwww",
		"weeeeeeeeeeeew",
		"weeeeeeeeeeeew",
		"weeeeeeeeeeeew",
		"weeeeeeeeeeeew",
		"weeeeeeeeeeeew",
		"weeeeeeeeeeeew",
		"weeeeeeweeeeew",
		"weeeeeeweeeeew",
		"weeeeeeweeeeew",
		"weeeeeeeeeeeew",
		"weeeeeeeeeeeew",
		"weeeeeeeeeeeew",
		"wwwwwwwwwwwwww"
	};
	private static final int defaultSpawnX = 2;
	private static final int defaultSpawnY = 2;
	
	
	private Map<PortIP, ServerPlayer> players = new HashMap<>();
	private int nextPlayerId = 0;
	
	private volatile boolean shutdownInitiated = false;

	private String[] mapLayout;
	private int mapDimX, mapDimY;
	private int spawnX, spawnY;
	
	private Thread cleanupThread;
	
	public GameServer() {
		this(defaultMapLayout, defaultSpawnX, defaultSpawnY);
	}

	public GameServer(String[] mapLayout, int spawnX, int spawnY) {
		this.mapLayout = mapLayout;
		this.spawnX = spawnX;
		this.spawnY = spawnY;
		mapDimX = mapLayout[0].length();
		mapDimY = mapLayout.length;
		cleanupThread = new Thread(new CleanupThread());
		cleanupThread.start();
	}



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
					out = new ServerPlayer(playerName, nextPlayerId, spawnX, spawnY,
							address, port);
					nextPlayerId++;
					players.put(pip, out);
				}
			}
		}
		if (out != null) {
			System.out.println("Player " + out.getName() + " created");
		}
		return out;
	}
	
	public void removePlayer(InetAddress address, int port) {
		PortIP pip = new PortIP(address, port);
		removePlayer(pip);
	}

	public void removePlayer(PortIP pip) {
		synchronized (players) {
			ServerPlayer player = players.remove(pip);
			if (player != null) {
				System.out.println("Player " + player.getName() + " removed");
			}
		}
	}

	public void movePlayer(ServerPlayer player, Direction direction) {
//		player.pauseObservation();
		player.setDirection(direction);
		int newX = 0, newY = 0;
		switch (direction) {
		case UP:
			newY = -1;
			break;
		case DOWN:
			newY += 1;
			break;
		case LEFT:
			newX -= 1;
			break;
		case RIGHT:
			newX += 1;
			break;
		default:
			break;
		}
		newX += player.getXpos();
		newX = (newX % mapDimX + mapDimX) % mapDimX;
		newY += player.getYpos();
		newY = (newY % mapDimY + mapDimY) % mapDimY;
		if (mapLayout[newY].charAt(newX) == 'w') {
			player.subOnePoint();
		} else {
			player.setXpos(newX);
			player.setYpos(newY);
			player.addOnePoint();
		}
//		player.unpauseObservation();
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
	
	
	@Override
	public void close() throws IOException {
		shutdownInitiated = true;
		cleanupThread.interrupt();
		while (cleanupThread.isAlive()) {
			try {
				cleanupThread.join();
			} catch (InterruptedException e) {
				// loop
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
