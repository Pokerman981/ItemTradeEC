package me.pokerman99.ItemTradeEC.Commands;

import com.google.common.collect.Lists;
import me.pokerman99.ItemTradeEC.ConfigVariables;
import me.pokerman99.ItemTradeEC.Main;
import me.pokerman99.ItemTradeEC.Utils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.List;
import java.util.Random;

public class ZCrystalCommand implements CommandExecutor {
    public static List<String> ZCRYSTALITEMNAMES = Lists.newArrayList();

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        Player player = (Player) src;

        if (!player.hasPermission("itemtradeec.bypass") && Utils.isOnCooldown(player)) return CommandResult.success();

        ConfigVariables configVariables = Main.getInstance().configVariables;

        if (!player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
            Utils.sendMessage(src, configVariables.getNotHoldingItemZCrystal());
            return CommandResult.empty();
        }

        ItemStack heldItem = player.getItemInHand(HandTypes.MAIN_HAND).get();
        String heldItemName = heldItem.getType().getName();

        if (!heldItemName.contains("_z")) {
            Utils.sendMessage(src, configVariables.getNotHoldingItemZCrystal());
            return CommandResult.empty();
        }

        if (heldItem.getQuantity() > 1) {
            Utils.sendMessage(src, configVariables.getOnlyOneItemZCrystal());
            return CommandResult.empty();
        }

        Random random = new Random();
        int num = random.nextInt(ZCRYSTALITEMNAMES.size());
        String item = "pixelmon:" + ZCRYSTALITEMNAMES.get(num);
        ItemStack stack = ItemStack.builder().itemType(Sponge.getRegistry().getType(ItemType.class, item).get()).build();

        Utils.sendMessage(player, "&aSuccessfully traded your &l"
                + heldItem.getTranslation().get()
                + "&a for a &l"
                + stack.getTranslation().get() + "&a!");

        heldItem.setQuantity(0);
//        player.getInventory().offer(heldItem);
        player.getInventory().offer(stack);
        Utils.setCooldown(player, 3);

        return CommandResult.success();
    }
}