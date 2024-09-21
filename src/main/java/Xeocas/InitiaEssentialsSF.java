package Xeocas;

import Xeocas.Machine.BasaltMachine;
import Xeocas.Machine.BlazeRodMachine;
import Xeocas.Machine.EndRodMachine;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class InitiaEssentialsSF extends JavaPlugin implements SlimefunAddon {

	@Override
	public void onEnable() {
		// Plugin startup logic
		getServer().getScheduler().runTaskLater(this, () -> {
			try {
				// Define the ItemGroup for weapon machines
				NamespacedKey eMachineCategoryId = new NamespacedKey(this, "initiia_category");
				CustomItemStack eMachineCategoryItem = new CustomItemStack(Material.IRON_INGOT, "&4Initia's Slimefun Essential Factories!"); // Use Diamond Block as the category icon
				ItemGroup eMachineGroup = new ItemGroup(eMachineCategoryId, eMachineCategoryItem);

				// Define the SlimefunItemStack for the ArtilleryAmmoMachine
				SlimefunItemStack basaltMachineStack = new SlimefunItemStack("BASALT_MACHINE", Material.FURNACE, "&7Basalt Machine");
				SlimefunItemStack blazerodMachineStack = new SlimefunItemStack("BLAZEROD_MACHINE", Material.BLAST_FURNACE, "&7BlazeRod Machine");
				SlimefunItemStack endrodMachineStack = new SlimefunItemStack("ENDROD_MACHINE", Material.BLAST_FURNACE, "&7EndRod Machine");


				// Create the ArtilleryAmmoMachine instance
				BasaltMachine basaltMachine = new BasaltMachine(eMachineGroup, basaltMachineStack, RecipeType.ENHANCED_CRAFTING_TABLE, this);
				BlazeRodMachine blazeRodMachine = new BlazeRodMachine(eMachineGroup, blazerodMachineStack, RecipeType.ENHANCED_CRAFTING_TABLE, this);
				EndRodMachine endRodMachine = new EndRodMachine(eMachineGroup, endrodMachineStack, RecipeType.ENHANCED_CRAFTING_TABLE, this);



				// Register the ArtilleryAmmoMachine
				basaltMachine.register(this);
				blazeRodMachine.register(this);
				endRodMachine.register(this);

			} catch (Exception e) {
				e.printStackTrace(); // Print the error stack trace for debugging
			}
		}, 20L); // Delay by 1 second (20 ticks)

		getLogger().info("Initializing plugin...");
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	@Override
	public JavaPlugin getJavaPlugin() {
		return this;
	}

	@Override
	public String getBugTrackerURL() {
		return null;
	}
}
