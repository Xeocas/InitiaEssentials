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

public class WitherSkullMachine extends AContainer implements RecipeDisplayItem, Listener {

	private static final int ENERGY_CONSUMPTION = 500;
	private static final int CAPACITY = ENERGY_CONSUMPTION * 1; // Can produce 1 custom skull
	private static final int PROCESSING_TIME = 50; // 50 seconds

	private ItemStack outputItem;
	private JavaPlugin plugin;
	private NamespacedKey key; // For custom NBT-like data

	public WitherSkullMachine(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, JavaPlugin plugin) {
		super(itemGroup, item, recipeType, getMachineRecipe());
		this.plugin = plugin;

		// Set output as a custom Nether Star with a custom name
		outputItem = new ItemStack(Material.WITHER_SKELETON_SKULL);

		// Register the event listener
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}


	@Override
	public String getInventoryTitle() {
		return "Wither Skull Factory";
	}

	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.FLINT_AND_STEEL);
	}

	@Override
	public String getMachineIdentifier() {
		return "WITHERSKULL_FACTORY";
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
				new ItemStack(Material.ANCIENT_DEBRIS),
				new ItemStack(Material.STICKY_PISTON),
				new ItemStack(Material.ANCIENT_DEBRIS),
				new ItemStack(Material.GOLD_BLOCK),
				new ItemStack(Material.BLAST_FURNACE),
				new ItemStack(Material.GOLD_BLOCK),
				SlimefunItems.ADVANCED_CIRCUIT_BOARD,
				new ItemStack(Material.REDSTONE_BLOCK),
				SlimefunItems.SULFATE,
		};
	}

	// Recipe matching for 1 TNT and 8 Iron Ingots
	@Override
	protected MachineRecipe findNextRecipe(BlockMenu inv) {
		// Check if input slots contain 1 TNT and 8 Iron Ingots
		ItemStack coalInSlot = inv.getItemInSlot(this.getInputSlots()[0]);
		ItemStack debInSlot = inv.getItemInSlot(this.getInputSlots()[1]);

		if (coalInSlot != null && coalInSlot.getType() == Material.COAL_BLOCK && coalInSlot.getAmount() >= 5 &&
				debInSlot != null && debInSlot.getType() == Material.ANCIENT_DEBRIS && debInSlot.getAmount() >= 2) {

			// Consume
			inv.consumeItem(this.getInputSlots()[0], 5);
			inv.consumeItem(this.getInputSlots()[1], 2);

			// Return the recipe
			return new MachineRecipe(PROCESSING_TIME, new ItemStack[]{
					new ItemStack(Material.COAL_BLOCK, 5), new ItemStack(Material.ANCIENT_DEBRIS, 2)},
					new ItemStack[]{outputItem});
		}
		return null;
	}

	@Override
	public List<ItemStack> getDisplayRecipes() {
		List<ItemStack> displayRecipes = new ArrayList<>();
		displayRecipes.add(new ItemStack(Material.COAL_BLOCK, 5));
		displayRecipes.add(new ItemStack(Material.ANCIENT_DEBRIS, 2));
		displayRecipes.add(outputItem);
		return displayRecipes;
	}
}
