package me.pokerman99.ItemTradeEC.Commands;

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

import java.util.Random;

public class TMCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = (Player) src;
        if (!player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
            Utils.sendMessage(src, "&cYou are not holding a TM/HM");
            return CommandResult.empty();
        }

        ItemStack heldItem = player.getItemInHand(HandTypes.MAIN_HAND).get();
        String heldItemName = heldItem.getType().getId();

        if (heldItem.getQuantity() > 1) {
            Utils.sendMessage(src, "&cPlease only have one item in the stack!");
            return CommandResult.empty();
        }

        if (!heldItemName.contains("pixelmon:tm") && !heldItemName.contains("pixelmon:hm")) {
            Utils.sendMessage(src, "&cThe supplied item is not a TM/HM!");
            return CommandResult.empty();
        }

        heldItem.setQuantity(heldItem.getQuantity() - 1);
        player.getInventory().offer(heldItem);


        Random random = new Random();
        int num = heldItemName.contains("tm") ? random.nextInt(173) + 1 : random.nextInt(9) + 1;
        String type = heldItemName.contains("tm") ? "tm" : "hm";

        ItemStack stack = ItemStack.builder()
                .itemType(Sponge.getRegistry().getType(ItemType.class, "pixelmon:" + type + num).get())
                .build();

        player.getInventory().offer(stack);

        return CommandResult.success();
    }
}
