package Xeocas.Machine;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
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

public class BlazeRodMachine extends AContainer implements RecipeDisplayItem, Listener {

	private static final int ENERGY_CONSUMPTION = 200;
	private static final int CAPACITY = ENERGY_CONSUMPTION * 2; // Can produce 2 custom blazerods
	private static final int PROCESSING_TIME = 20; // 20 seconds

	private ItemStack outputItem;
	private JavaPlugin plugin;
	private NamespacedKey key; // For custom NBT-like data

	public BlazeRodMachine(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, JavaPlugin plugin) {
		super(itemGroup, item, recipeType, getMachineRecipe());
		this.plugin = plugin;

		// Set output as a custom Nether Star with a custom name
		outputItem = new ItemStack(Material.BLAZE_ROD);

		// Register the event listener
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}


	@Override
	public String getInventoryTitle() {
		return "Blaze Rod Factory";
	}

	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.FLINT_AND_STEEL);
	}

	@Override
	public String getMachineIdentifier() {
		return "BLAZEROD_FACTORY";
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
				new ItemStack(Material.BLAZE_POWDER),
				new ItemStack(Material.PISTON),
				new ItemStack(Material.BLAZE_POWDER),
				new ItemStack(Material.GOLD_BLOCK),
				new ItemStack(Material.BLAST_FURNACE),
				new ItemStack(Material.EMERALD_BLOCK),
				new ItemStack(Material.IRON_BLOCK),
				new ItemStack(Material.REDSTONE_BLOCK),
				new ItemStack(Material.COPPER_BLOCK)
		};
	}

	// Recipe matching for 1 TNT and 8 Iron Ingots
	@Override
	protected MachineRecipe findNextRecipe(BlockMenu inv) {
		// Check if input slots contain 1 TNT and 8 Iron Ingots
		ItemStack blazepowderInSlot = inv.getItemInSlot(this.getInputSlots()[0]);
		ItemStack stickInSlot = inv.getItemInSlot(this.getInputSlots()[1]);

		if (blazepowderInSlot != null && blazepowderInSlot.getType() == Material.BLAZE_POWDER && blazepowderInSlot.getAmount() >= 4 &&
				stickInSlot != null && stickInSlot.getType() == Material.STICK && stickInSlot.getAmount() >= 2) {

			// Consume 3 Iron Ingots
			inv.consumeItem(this.getInputSlots()[0], 4);
			inv.consumeItem(this.getInputSlots()[1], 2);

			// Return the recipe
			return new MachineRecipe(PROCESSING_TIME, new ItemStack[]{
					new ItemStack(Material.BLAZE_POWDER, 4), new ItemStack(Material.STICK, 2)},
					new ItemStack[]{outputItem});
		}
		return null;
	}

	@Override
	public List<ItemStack> getDisplayRecipes() {
		List<ItemStack> displayRecipes = new ArrayList<>();
		displayRecipes.add(new ItemStack(Material.BLAZE_POWDER, 4));
		displayRecipes.add(new ItemStack(Material.STICK, 2));
		displayRecipes.add(outputItem);
		return displayRecipes;
	}
}
