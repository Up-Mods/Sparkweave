package dev.upcraft.sparkweave.neoforge.client.renderdoc;

import com.google.auto.service.AutoService;
import dev.upcraft.sparkweave.renderdoc.client.RenderDocHelper;
import net.neoforged.neoforgespi.earlywindow.GraphicsBootstrapper;

/**
 * RenderDoc needs to be loaded before NeoForge initializes their graphics context, so we need to do it via a
 * {@linkplain GraphicsBootstrapper}. However, those must be loaded as a service, from a separate non-mod jar,
 * top-level from the mods directory. Therefore we first extract the jar and then tell the user to relaunch the game.
 */
@AutoService(GraphicsBootstrapper.class)
public class SparkweaveRenderdocLoader implements GraphicsBootstrapper {

	@Override
	public String name() {
		return "Sparkweave Engine/RenderdocLoader";
	}

	@Override
	public void bootstrap(String[] arguments) {
		RenderDocHelper.init();
	}
}
