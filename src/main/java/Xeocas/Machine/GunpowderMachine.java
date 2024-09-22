package Xeocas.Machine;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
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

public class GunpowderMachine extends AContainer implements RecipeDisplayItem, Listener {

	private static final int ENERGY_CONSUMPTION = 50;
	private static final int CAPACITY = ENERGY_CONSUMPTION * 10; // Can produce 10 powder
	private static final int PROCESSING_TIME = 20; // 20 seconds

	private ItemStack outputItem;
	private JavaPlugin plugin;
	private NamespacedKey key; // For custom NBT-like data

	public GunpowderMachine(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, JavaPlugin plugin) {
		super(itemGroup, item, recipeType, getMachineRecipe());
		this.plugin = plugin;

		// Set output as a custom Nether Star with a custom name
		outputItem = new ItemStack(Material.GUNPOWDER);

		// Register the event listener
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}


	@Override
	public String getInventoryTitle() {
		return "Gunpowder Factory";
	}

	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.FLINT_AND_STEEL);
	}

	@Override
	public String getMachineIdentifier() {
		return "GUNPOWDER_FACTORY";
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
				new ItemStack(Material.GUNPOWDER),
				new ItemStack(Material.STICKY_PISTON),
				new ItemStack(Material.GUNPOWDER),
				new ItemStack(Material.GOLD_BLOCK),
				new ItemStack(Material.BLAST_FURNACE),
				new ItemStack(Material.GOLD_BLOCK),
				SlimefunItems.ADVANCED_CIRCUIT_BOARD,
				new ItemStack(Material.REDSTONE_BLOCK),
				SlimefunItems.ADVANCED_CIRCUIT_BOARD,
		};
	}

	// Recipe matching for 1 TNT and 8 Iron Ingots
	@Override
	protected MachineRecipe findNextRecipe(BlockMenu inv) {
		ItemStack sulpInSlot = inv.getItemInSlot(this.getInputSlots()[0]);
		ItemStack charInSlot = inv.getItemInSlot(this.getInputSlots()[1]);

		// Check if item is similar to the Sulfate
		if (sulpInSlot != null && sulpInSlot.hasItemMeta() && sulpInSlot.getItemMeta().hasDisplayName()) {
			String itemName = sulpInSlot.getItemMeta().getDisplayName();

			// Check for the gold name of the sulfate (you can adjust this if it's not exactly "§6Sulfate")
			if (itemName.equals(SlimefunItems.SULFATE.getItemMeta().getDisplayName()) &&
					charInSlot != null && charInSlot.getType() == Material.CHARCOAL && charInSlot.getAmount() >= 1) {

				// Consume the items
				inv.consumeItem(this.getInputSlots()[0], 1);
				inv.consumeItem(this.getInputSlots()[1], 1);

				// Return the recipe
				return new MachineRecipe(PROCESSING_TIME,
						new ItemStack[]{SlimefunItems.SULFATE, new ItemStack(Material.CHARCOAL, 1)},
						new ItemStack[]{outputItem});
			}
		}
		return null;
	}


	@Override
	public List<ItemStack> getDisplayRecipes() {
		List<ItemStack> displayRecipes = new ArrayList<>();
		displayRecipes.add(SlimefunItems.SULFATE);
		displayRecipes.add(new ItemStack(Material.CHARCOAL, 1));
		displayRecipes.add(outputItem);
		return displayRecipes;
	}
}
