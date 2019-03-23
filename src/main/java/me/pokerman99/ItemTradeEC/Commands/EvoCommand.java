package me.pokerman99.ItemTradeEC.Commands;

import me.pokerman99.ItemTradeEC.ConfigVariables;
import me.pokerman99.ItemTradeEC.Main;
import me.pokerman99.ItemTradeEC.Utils;
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

import java.util.Random;

public class EvoCommand implements CommandExecutor {
    static String[] evoItems = {"pixelmon:fire_stone", "pixelmon:water_stone", "pixelmon:moon_stone", "pixelmon:thunder_stone", "pixelmon:leaf_stone", "pixelmon:sun_stone", "pixelmon:dawn_stone", "pixelmon:dusk_stone"};

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        Player player = (Player) src;

        if (!player.hasPermission("itemtradeec.bypass") && Utils.isOnCooldown(player)) return CommandResult.success();

        ConfigVariables configVariables = Main.getInstance().configVariables;
        if (!player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
            Utils.sendMessage(src, configVariables.getNotHoldingItemEVO());
            return CommandResult.empty();
        }

        ItemStack heldItem = player.getItemInHand(HandTypes.MAIN_HAND).get();
        String heldItemName = heldItem.getType().getId();

        if (!heldItemName.matches("(pixelmon:).*?(_stone)")) {
            Utils.sendMessage(src, configVariables.getNotHoldingItemEVO());
            return CommandResult.empty();
        }

        if (heldItem.getQuantity() > 1) {
            Utils.sendMessage(src, configVariables.getOnlyOneItemEVO());
            return CommandResult.empty();
        }

        heldItem.setQuantity(0);
        player.getInventory().offer(heldItem);

        Random random = new Random();
        String item = evoItems[random.nextInt(evoItems.length)+1];
        ItemStack stack = ItemStack.builder().itemType(Sponge.getRegistry().getType(ItemType.class, item).get()).build();

        player.getInventory().offer(stack);
        Utils.sendMessage(player, "&aSuccessfully traded your &l" + Utils.getFormattedItemName(heldItemName) + "&a for a &l" + Utils.getFormattedItemName(item) + "&a!");
        Utils.setCooldown(player);

        return CommandResult.success();
    }
}
