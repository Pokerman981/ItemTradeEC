package me.pokerman99.ItemTradeEC.Commands;

import me.pokerman99.ItemTradeEC.Utils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
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
        String heldItemName = heldItem.getType().getName();

        if (!heldItemName.contains("pixelmon:tm") || !heldItemName.contains("pixelmon:hm")) {
            Utils.sendMessage(src, "&cThe supplied item is not a TM/HM!");
            return CommandResult.empty();
        }

        Random random = new Random();
        int num = random.nextInt(173) + 1;
        String type = heldItemName.contains("tm") ? "tm" : "hm";

        

        //TODO REMOVE THE TM/HM
        //TODO FIGURE OUT WHICH TYPE THEN LOOP THROUGH THE ITEMS

        return CommandResult.success();
    }
}
