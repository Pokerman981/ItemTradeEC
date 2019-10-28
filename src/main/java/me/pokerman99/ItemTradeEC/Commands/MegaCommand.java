package me.pokerman99.ItemTradeEC.Commands;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.enums.EnumMegaPokemon;
import me.pokerman99.ItemTradeEC.ConfigVariables;
import me.pokerman99.ItemTradeEC.Main;
import me.pokerman99.ItemTradeEC.Utils;
import net.minecraft.item.Item;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MegaCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        Player player = (Player) src;

        if (!player.hasPermission("itemtradeec.bypass") && Utils.isOnCooldown(player)) return CommandResult.success();

        ConfigVariables configVariables = Main.getInstance().configVariables;

        if (!player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
            Utils.sendMessage(src, configVariables.getNotHoldingItemMEGA());
            return CommandResult.empty();
        }

        ItemStack heldItem = player.getItemInHand(HandTypes.MAIN_HAND).get();
        String heldItemId = heldItem.getType().getId();
        List<String> megaItems = new ArrayList<>();

        for (EnumMegaPokemon enumMegaPokemon : EnumMegaPokemon.values()) {
            Item[] items = enumMegaPokemon.getMegaEvoItems();

            for (Item item : items) {
                String itemName = item.getRegistryName().toString();
                if (itemName.contains("air")) continue;
                megaItems.add(itemName);
            }
        }

        if (!megaItems.contains(heldItemId)) {
            Utils.sendMessage(src, configVariables.getNotHoldingItemMEGA());
            return CommandResult.empty();
        }

        if (heldItem.getQuantity() > 1) {
            Utils.sendMessage(src, configVariables.getOnlyOneItemMEGA());
            return CommandResult.empty();
        }

        megaItems.remove(heldItemId);
        String item = megaItems.get(new Random().nextInt(megaItems.size()));
        ItemStack stack = ItemStack.builder().itemType(Sponge.getRegistry().getType(ItemType.class, item).get()).build();

        heldItem.setQuantity(0);
        player.getInventory().offer(stack);
        Utils.sendMessage(player, "&aSuccessfully traded your &l" + Utils.getFormattedItemName(heldItemId) + "&a for a &l" + Utils.getFormattedItemName(item) + "&a!");
        Utils.setCooldown(player, 3);

        return CommandResult.empty();
    }
}
