package io.github.lasteamlab.raspberryjuice2.cmd;


import io.github.lasteamlab.raspberryjuice2.RaspberryJuice2;
import io.github.lasteamlab.raspberryjuice2.RemoteSession;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CmdEntity {
	private final String preFix = "entity.";
	private RemoteSession session;
	private RaspberryJuice2 plugin;

	public CmdEntity(RemoteSession session) {
		this.session = session;

		this.plugin = session.plugin;
	}

	public void execute(String command, String[] args) {

		//get entity based on id
		Entity entity = plugin.getEntity(Integer.parseInt(args[0]));

		if (entity == null) {
			plugin.getLogger().info("Entity [" + args[0] + "] not found.");
			session.send("Fail,This entity identity not exist");
		}

		// entity.getTile
		if (command.equals("getTile")) {

			session.send(session.blockLocationToRelative(entity.getLocation()));

			// entity.setTile
		} else if (command.equals("setTile")) {
			String x = args[1], y = args[2], z = args[3];
			Location loc = entity.getLocation();

			entity.teleport(session.parseRelativeBlockLocation(x, y, z, loc.getPitch(), loc.getYaw()));

			// entity.getPos
		} else if (command.equals("getPos")) {

			session.send(session.locationToRelative(entity.getLocation()));

			// entity.setPos
		} else if (command.equals("setPos")) {
			String x = args[1], y = args[2], z = args[3];
			Location loc = entity.getLocation();

			entity.teleport(session.parseRelativeLocation(x, y, z, loc.getPitch(), loc.getYaw()));

			// entity.setDirection
		} else if (command.equals("setDirection")) {
			Double x = Double.parseDouble(args[1]);
			Double y = Double.parseDouble(args[2]);
			Double z = Double.parseDouble(args[3]);
			Location loc = entity.getLocation();

			loc.setDirection(new Vector(x, y, z));
			entity.teleport(loc);

			// entity.getDirection
		} else if (command.equals("getDirection")) {

			session.send(entity.getLocation().getDirection().toString());

			// entity.setRotation
		} else if (command.equals("setRotation")) {
			Float yaw = Float.parseFloat(args[1]);
			Location loc = entity.getLocation();

			loc.setYaw(yaw);
			entity.teleport(loc);

			// entity.getRotation
		} else if (command.equals("getRotation")) {

			session.send(entity.getLocation().getYaw());

			// entity.setPitch
		} else if (command.equals("setPitch")) {
			Float pitch = Float.parseFloat(args[1]);
			Location loc = entity.getLocation();

			loc.setPitch(pitch);
			entity.teleport(loc);

			// entity.getPitch
		} else if (command.equals("getPitch")) {
			session.send(entity.getLocation().getPitch());

			// entity.getListName
		} else if (command.equals("getName")) {
			if (entity instanceof Player) {
				Player p = (Player) entity;
				//sending list name because plugin.getNamedPlayer() uses list name
				session.send(p.getPlayerListName());
			} else {
				session.send(entity.getName());
			}
			// entity.getEntities
		} else if (command.equals("getEntities")) {
			int entityId = Integer.parseInt(args[0]);
			int distance = Integer.parseInt(args[1]);
			String entityType = args[2];

			session.send(session.getEntities(session.origin.getWorld(), entityId, distance, entityType));
				
		// entity.removeEntities
		} else if (command.equals("removeEntities")) {
			int entityId = Integer.parseInt(args[0]);
			int distance = Integer.parseInt(args[1]);
			String entityType = "";

			if (args.length == 3) {
				entityType = args[2];
			}				

			session.send(session.removeEntities(session.origin.getWorld(), entityId, distance, entityType));
			// entity.events.clear
		} else if (command.equals("events.clear")) {
			int entityId = Integer.parseInt(args[0]);
			session.clearEntityEvents(entityId);
			
			// entity.events.block.hits
		} else if (command.equals("events.block.hits")) {
			int entityId = Integer.parseInt(args[0]);
			session.send(session.getBlockHits(entityId));

			// entity.events.chat.posts
		} else if (command.equals("events.chat.posts")) {
			int entityId = Integer.parseInt(args[0]);
			session.send(session.getChatPosts(entityId));

			// entity.events.projectile.hits
		} else if(command.equals("events.projectile.hits")) {
			int entityId = Integer.parseInt(args[0]);
			session.send(session.getProjectileHits(entityId));

		} else {
			session.plugin.getLogger().warning(preFix + command + " is not supported.");
			session.send("Fail," + preFix + command + " is not supported.");
		}

	}
}
