package Xeocas.Machine;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class EndRodMachine extends AContainer implements RecipeDisplayItem, Listener {

	private static final int ENERGY_CONSUMPTION = 200;
	private static final int CAPACITY = ENERGY_CONSUMPTION * 1; // Can produce 1 custom endrod
	private static final int PROCESSING_TIME = 40; // 20 seconds

	private ItemStack outputItem;
	private JavaPlugin plugin;
	private NamespacedKey key; // For custom NBT-like data

	public EndRodMachine(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, JavaPlugin plugin) {
		super(itemGroup, item, recipeType, getMachineRecipe());
		this.plugin = plugin;

		// Set output as a custom Nether Star with a custom name
		outputItem = new ItemStack(Material.END_ROD);

		// Register the event listener
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}


	@Override
	public String getInventoryTitle() {
		return "End Rod Factory";
	}

	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.FLINT_AND_STEEL);
	}

	@Override
	public String getMachineIdentifier() {
		return "ENDROD_FACTORY";
	}

	@Override
	public int getCapacity() {
		return CAPACITY;
	}

	@Override
	public int getEnergyConsumption() {
		return ENERGY_CONSUMPTION;
	}

	@Override
	public int getSpeed() {
		return PROCESSING_TIME;
	}

	public static ItemStack[] getMachineRecipe() {
		return new ItemStack[]{
				new ItemStack(Material.BLAZE_ROD),
				new ItemStack(Material.STICKY_PISTON),
				new ItemStack(Material.BLAZE_ROD),
				new ItemStack(Material.GOLD_BLOCK),
				new ItemStack(Material.BLAST_FURNACE),
				new ItemStack(Material.GOLD_BLOCK),
				SlimefunItems.SMALL_CAPACITOR,
				new ItemStack(Material.REDSTONE_BLOCK),
				SlimefunItems.SMALL_CAPACITOR,
		};
	}

	// Recipe matching for 1 TNT and 8 Iron Ingots
	@Override
	protected MachineRecipe findNextRecipe(BlockMenu inv) {
		// Check if input slots contain 1 TNT and 8 Iron Ingots
		ItemStack blazerodInSlot = inv.getItemInSlot(this.getInputSlots()[0]);
		ItemStack quartzInSlot = inv.getItemInSlot(this.getInputSlots()[1]);

		if (blazerodInSlot != null && blazerodInSlot.getType() == Material.BLAZE_ROD && blazerodInSlot.getAmount() >= 1 &&
				quartzInSlot != null && quartzInSlot.getType() == Material.QUARTZ && quartzInSlot.getAmount() >= 4) {

			// Consume 3 Iron Ingots
			inv.consumeItem(this.getInputSlots()[0], 1);
			inv.consumeItem(this.getInputSlots()[1], 4);

			// Return the recipe
			return new MachineRecipe(PROCESSING_TIME, new ItemStack[]{
					new ItemStack(Material.BLAZE_ROD, 1), new ItemStack(Material.QUARTZ, 4)},
					new ItemStack[]{outputItem});
		}
		return null;
	}

	@Override
	public List<ItemStack> getDisplayRecipes() {
		List<ItemStack> displayRecipes = new ArrayList<>();
		displayRecipes.add(new ItemStack(Material.BLAZE_ROD, 1));
		displayRecipes.add(new ItemStack(Material.QUARTZ, 4));
		displayRecipes.add(outputItem);
		return displayRecipes;
	}
}
