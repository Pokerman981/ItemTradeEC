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
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = (Player) src;
        String metaDataResult = Utils.getMetaData(player.getUniqueId()).getMeta().getOrDefault("itemtrade-evo", "-1");
        long cooldown = Long.valueOf(metaDataResult);

        if (System.currentTimeMillis() < cooldown) {
            Utils.sendMessage(player, "&cYou need to wait another " + Utils.timeDiffFormat((cooldown - System.currentTimeMillis()) / 1000, true) + " before you can use that command again!");
            return CommandResult.empty();
        }

        ConfigVariables configVariables = Main.getInstance().configVariables;
        if (!player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
            Utils.sendMessage(src, configVariables.getNotHoldingItemEVO());
            return CommandResult.empty();
        }

        ItemStack heldItem = player.getItemInHand(HandTypes.MAIN_HAND).get();
        String heldItemName = heldItem.getType().getId();

        if (heldItem.getQuantity() > 1) {
            Utils.sendMessage(src, configVariables.getOnlyOneItemEVO());
            return CommandResult.empty();
        }

        if (!heldItemName.matches("(pixelmon:).*?(_stone)")) {
            Utils.sendMessage(src, configVariables.getNotHoldingItemEVO());
            return CommandResult.empty();
        }

        heldItem.setQuantity(heldItem.getQuantity() - 1);
        player.getInventory().offer(heldItem);


        Random random = new Random();

        ItemStack stack = ItemStack.builder()
                .itemType(Sponge.getRegistry().getType(ItemType.class, evoItems[random.nextInt(evoItems.length)+1]).get())
                .build();

        player.getInventory().offer(stack);

        Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + player.getName() + " meta unset itemtrade-evo");

        Task.builder().delayTicks(2).execute(task -> {
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + player.getName() + " meta set itemtrade-evo " + (Main.day + System.currentTimeMillis()));
        }).submit(Main.getInstance());


        return CommandResult.success();
    }
}
