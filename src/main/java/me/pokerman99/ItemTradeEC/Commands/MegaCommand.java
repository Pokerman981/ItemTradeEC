package me.pokerman99.ItemTradeEC.Commands;

import com.google.common.collect.Lists;
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

import java.util.List;
import java.util.Random;

public class MegaCommand implements CommandExecutor {
    public static List<String> MEGASTONEITEMNAMES = Lists.newArrayList();

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
        String heldItemName = heldItem.getType().getId();

        if (!MEGASTONEITEMNAMES.toString().contains(heldItemName)) {
            Utils.sendMessage(src, configVariables.getNotHoldingItemMEGA());
            return CommandResult.empty();
        }

        if (heldItem.getQuantity() > 1) {
            Utils.sendMessage(src, configVariables.getOnlyOneItemMEGA());
            return CommandResult.empty();
        }

        heldItem.setQuantity(0);
        player.getInventory().offer(heldItem);

        Random random = new Random();
        int num = random.nextInt(MEGASTONEITEMNAMES.size());
        String item = MEGASTONEITEMNAMES.get(num);
        ItemStack stack = ItemStack.builder().itemType(Sponge.getRegistry().getType(ItemType.class, item).get()).build();

        player.getInventory().offer(stack);
        Utils.sendMessage(player, "&aSuccessfully traded your &l" + Utils.getFormattedItemName(heldItemName) + "&a for a &l" + Utils.getFormattedItemName(item) + "&a!");
        Utils.setCooldown(player, 3);

        return CommandResult.empty();
    }
}
