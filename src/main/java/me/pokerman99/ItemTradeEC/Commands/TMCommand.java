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
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import java.util.Random;

public class TMCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        Player player = (Player) src;

        if (!player.hasPermission("itemtradeec.bypass") && Utils.isOnCooldown(player)) return CommandResult.success();

        ConfigVariables configVariables = Main.getInstance().configVariables;

        if (!player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
            Utils.sendMessage(src, configVariables.getNotHoldingItemTM());
            return CommandResult.empty();
        }

        ItemStack heldItem = player.getItemInHand(HandTypes.MAIN_HAND).get();
        String heldItemName = heldItem.getType().getName();

        if (!heldItemName.contains("pixelmon:tm") && !heldItemName.contains("pixelmon:hm")) {
            Utils.sendMessage(src, configVariables.getNotHoldingItemTM());
            return CommandResult.empty();
        }

        if (heldItem.getQuantity() > 1) {
            Utils.sendMessage(src, configVariables.getOnlyOneItemTM());
            return CommandResult.empty();
        }

        //heldItem.setQuantity(0);
        //player.getInventory().offer(heldItem);

        Random random = new Random();
        int num = heldItemName.contains("tm") ? random.nextInt(174) + 1 : random.nextInt(10) + 1;
        String type = heldItemName.contains("tm") ? "tm" : "hm";
        ItemStack stack = ItemStack.builder().itemType(Sponge.getRegistry().getType(ItemType.class, "pixelmon:" + type + num).get()).build();
        ItemStackSnapshot itemStackSnapshot = stack.createSnapshot();


        Utils.sendMessage(player, "&aSuccessfully traded your &l"
                + heldItem.getTranslation().get()
                + "&a for a &l"
                + itemStackSnapshot.createStack().getTranslation().get() + "&a!");

        heldItem.setQuantity(0);
        player.getInventory().offer(heldItem);
        player.getInventory().offer(stack);

        Utils.setCooldown(player);

        return CommandResult.success();
    }
}