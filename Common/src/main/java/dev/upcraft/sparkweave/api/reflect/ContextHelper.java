package dev.upcraft.sparkweave.api.reflect;

import dev.upcraft.sparkweave.api.annotation.CallerSensitive;
import dev.upcraft.sparkweave.api.annotation.Mod;
import dev.upcraft.sparkweave.api.logging.SparkweaveLoggerFactory;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.api.platform.Services;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Vector;

public class ContextHelper {

    private ContextHelper() {
        throw new UnsupportedOperationException();
    }

	private static final Logger LOGGER = SparkweaveLoggerFactory.getLogger("Sparkweave Engine/ContextHelper");
    private static final Map<Class<?>, ModContainer> CONTEXT_CACHE = new Object2ObjectOpenHashMap<>();
    private static final Map<String, String> PACKAGE_CONTEXT_CACHE = new Object2ObjectOpenHashMap<>();

    static {
        // Vanilla Minecraft
        PACKAGE_CONTEXT_CACHE.put("net.minecraft", "minecraft");
        PACKAGE_CONTEXT_CACHE.put("com.mojang", "minecraft");

        // Fabric
        PACKAGE_CONTEXT_CACHE.put("net.fabricmc", "fabricloader");
        PACKAGE_CONTEXT_CACHE.put("net.fabricmc.loader", "fabricloader");
        PACKAGE_CONTEXT_CACHE.put("net.fabricmc.fabric", "fabric"); // catch-all for Fabric API

	    // MixinExtras
	    PACKAGE_CONTEXT_CACHE.put("io.github.llamalad7", "mixinextras");

		// NeoForge
		PACKAGE_CONTEXT_CACHE.put("net.neoforged", "neoforge");
		// ::bootstraplauncher
		PACKAGE_CONTEXT_CACHE.put("cpw.mods.bootstraplauncher", "neoforge");
		// ::modlauncher
		PACKAGE_CONTEXT_CACHE.put("cpw.mods.modlauncher", "neoforge");
		// ::secure jar handler
		PACKAGE_CONTEXT_CACHE.put("cpw.mods.cl", "neoforge");
		PACKAGE_CONTEXT_CACHE.put("cpw.mods.jarhandling", "neoforge");
		PACKAGE_CONTEXT_CACHE.put("cpw.mods.niofs", "neoforge");
		PACKAGE_CONTEXT_CACHE.put("cpw.mods.util", "neoforge");

	    // Quilt
	    PACKAGE_CONTEXT_CACHE.put("org.quiltmc", "quilt_loader");
	    PACKAGE_CONTEXT_CACHE.put("org.quiltmc.loader", "quilt_loader");
	    PACKAGE_CONTEXT_CACHE.put("org.quiltmc.qsl", "qsl"); // catch-all for QSL

		// Sinytra Connector
		PACKAGE_CONTEXT_CACHE.put("org.sinytra.connector", "connector");

        // JVM packages as of Microsoft JDK 17.0.4.101-hotspot
        // @formatter:off
		List.of(
				"java",
				"jdk",
				"javax",
				"com.sun",
				"sun",
				"org.ietf.jgss",
				"org.w3c.dom",
				"org.xml.sax",
				"org.jcp.xml.dsig.internal",
				"netscape.javascript"
		).forEach(it -> PACKAGE_CONTEXT_CACHE.put(it, "java"));
		// @formatter:on
    }

    public static ModContainer getContext(Class<?> clazz) {
        return CONTEXT_CACHE.computeIfAbsent(clazz, node -> {
            var annotation = clazz.getDeclaredAnnotation(Mod.Context.class);
            if (annotation == null) {
				var packageName = clazz.getPackage().getName();
				var packages = ClassLoader.getSystemClassLoader().getUnnamedModule().getPackages();
				if(packages.contains(packageName)) {
					annotation = ClassLoader.getSystemClassLoader().getUnnamedModule().getClassLoader().getDefinedPackage(packageName).getAnnotation(Mod.Context.class);
				}
            }

            if (annotation != null) {
                return getModContainer(annotation.value());
            }

            var pkgName = clazz.getPackageName();
            var packages = new Vector<String>();
            String contextModID;
            while (true) {
                packages.add(pkgName);

                // cache hit
                if ((contextModID = PACKAGE_CONTEXT_CACHE.get(pkgName)) != null) {
                    break;
                }

                // on cache miss try for package-info file
                try {
                    // force-load package-info if it exists to ensure the package exists in the classloader
                    // if none is found, there is no point trying to load annotations from it so we use the exception to skip this package
                    Class.forName(pkgName + ".package-info");

                    var pkg = ContextHelper.class.getClassLoader().getDefinedPackage(pkgName);
                    if (pkg != null) {
                        annotation = pkg.getAnnotation(Mod.Context.class);
                        if (annotation != null) {
                            contextModID = annotation.value();
                            break;
                        }
                    }
                } catch (ClassNotFoundException ignore) { }
                if (!pkgName.contains(".")) {
                    break;
                }

                pkgName = pkgName.substring(0, pkgName.lastIndexOf('.'));
            }

            if (contextModID == null) {
                LOGGER.error("Could not determine mod context for class {}, assuming Minecraft!", clazz.getCanonicalName());
                contextModID = "minecraft";
            }

            for (String pkg : packages) {
                PACKAGE_CONTEXT_CACHE.put(pkg, contextModID);
            }

            return getModContainer(contextModID);
        });
    }

	private static ModContainer getModContainer(String modid) {
		return Services.PLATFORM.getModContainer(modid).orElseThrow(() -> new NoSuchElementException("No mod loaded with ID " + modid));
	}

	@ApiStatus.Internal
	public static void setServiceInitDone() {

	}

    @CallerSensitive
    public static ModContainer getCurrentContext() {
        return getCurrentContext(1);
    }

    @CallerSensitive
    public static ModContainer getCallerContext() {
        return getCurrentContext(2);
    }

    @CallerSensitive
    public static ModContainer getCurrentContext(int above) {
        var caller = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).walk(s -> s.map(StackWalker.StackFrame::getDeclaringClass).skip(above + 1).findFirst().orElseThrow(() -> new IllegalStateException("Could not determine class from call context")));
        return getContext(caller);
    }
}
