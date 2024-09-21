package Xeocas.Machine;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class BasaltMachine extends AContainer implements RecipeDisplayItem, Listener {

	private static final int ENERGY_CONSUMPTION = 64;
	private static final int CAPACITY = ENERGY_CONSUMPTION * 5; // Can produce 5 custom basalt
	private static final int PROCESSING_TIME = 10; // 10 seconds

	private ItemStack outputItem;
	private JavaPlugin plugin;
	private NamespacedKey key; // For custom NBT-like data

	public BasaltMachine(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, JavaPlugin plugin) {
		super(itemGroup, item, recipeType, getMachineRecipe());
		this.plugin = plugin;

		// Set output as a custom Nether Star with a custom name
		outputItem = new ItemStack(Material.BASALT);

		// Register the event listener
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}


	@Override
	public String getInventoryTitle() {
		return "Basalt Factory";
	}

	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.FLINT_AND_STEEL);
	}

	@Override
	public String getMachineIdentifier() {
		return "BASALT_FACTORY";
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
				new ItemStack(Material.ICE),
				new ItemStack(Material.PISTON),
				new ItemStack(Material.ICE),
				new ItemStack(Material.GOLD_BLOCK),
				new ItemStack(Material.FURNACE),
				new ItemStack(Material.DIAMOND_BLOCK),
				new ItemStack(Material.IRON_BLOCK),
				new ItemStack(Material.REDSTONE_BLOCK),
				new ItemStack(Material.IRON_BLOCK)
		};
	}

	// Recipe matching for 1 TNT and 8 Iron Ingots
	@Override
	protected MachineRecipe findNextRecipe(BlockMenu inv) {
		// Check if input slots contain 1 TNT and 8 Iron Ingots
		ItemStack gravelInSlot = inv.getItemInSlot(this.getInputSlots()[0]);
		ItemStack redsandInSlot = inv.getItemInSlot(this.getInputSlots()[1]);

		if (gravelInSlot != null && gravelInSlot.getType() == Material.GRAVEL && gravelInSlot.getAmount() >= 3 &&
				redsandInSlot != null && redsandInSlot.getType() == Material.RED_SAND && redsandInSlot.getAmount() >= 2) {

			// Consume 3 Iron Ingots
			inv.consumeItem(this.getInputSlots()[0], 3);
			inv.consumeItem(this.getInputSlots()[1], 2);

			// Return the recipe
			return new MachineRecipe(PROCESSING_TIME, new ItemStack[]{
					new ItemStack(Material.GRAVEL, 3), new ItemStack(Material.RED_SAND, 2)},
					new ItemStack[]{outputItem});
		}
		return null;
	}

	@Override
	public List<ItemStack> getDisplayRecipes() {
		List<ItemStack> displayRecipes = new ArrayList<>();
		displayRecipes.add(new ItemStack(Material.GRAVEL, 3));
		displayRecipes.add(new ItemStack(Material.RED_SAND, 2));
		displayRecipes.add(outputItem);
		return displayRecipes;
	}
}

