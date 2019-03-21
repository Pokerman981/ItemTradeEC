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
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = (Player) src;
        String metaDataResult = Utils.getMetaData(player.getUniqueId()).getMeta().getOrDefault("itemtrade-mega", "-1");
        long cooldown = Long.valueOf(metaDataResult);

        if (System.currentTimeMillis() < cooldown) {
            Utils.sendMessage(player, "&cYou need to wait another " + Utils.timeDiffFormat((cooldown - System.currentTimeMillis()) / 1000, true) + " before you can use that command again!");
            return CommandResult.empty();
        }
        ConfigVariables configVariables = Main.getInstance().configVariables;

        if (!player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
            Utils.sendMessage(src, configVariables.getNotHoldingItemMEGA());
            return CommandResult.empty();
        }

        ItemStack heldItem = player.getItemInHand(HandTypes.MAIN_HAND).get();
        String heldItemName = heldItem.getType().getId();

        if (heldItem.getQuantity() > 1) {
            Utils.sendMessage(src, configVariables.getOnlyOneItemMEGA());
            return CommandResult.empty();
        }


        if (!MEGASTONEITEMNAMES.toString().contains(heldItemName)) {
            Utils.sendMessage(src, configVariables.getNotHoldingItemMEGA());
            return CommandResult.empty();
        }

        heldItem.setQuantity(heldItem.getQuantity() - 1);
        player.getInventory().offer(heldItem);


        Random random = new Random();
        int num = random.nextInt(MEGASTONEITEMNAMES.size());

        ItemStack stack = ItemStack.builder()
                .itemType(Sponge.getRegistry().getType(ItemType.class, MEGASTONEITEMNAMES.get(num)).get())
                .build();

        player.getInventory().offer(stack);


        Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + player.getName() + " meta unset itemtrade-mega");

        Task.builder().delayTicks(2).execute(task -> {
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + player.getName() + " meta set itemtrade-mega " + (Main.day + System.currentTimeMillis()));
        }).submit(Main.getInstance());


        return CommandResult.empty();
    }
}
