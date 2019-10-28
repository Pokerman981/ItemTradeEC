package me.pokerman99.ItemTradeEC.Commands;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.enums.items.EnumZCrystals;
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
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
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
        String heldItemId = heldItem.getType().getId().replaceAll("pixelmon:", "");

        if (!heldItemId.contains("_z")) {
            Utils.sendMessage(src, configVariables.getNotHoldingItemZCrystal());
            return CommandResult.empty();
        }

        if (heldItem.getQuantity() > 1) {
            Utils.sendMessage(src, configVariables.getOnlyOneItemZCrystal());
            return CommandResult.empty();
        }

        String winner = null;
        int roll = new Random().nextInt(100);
        List<String> regs = new ArrayList<>();
        List<String> valuables = new ArrayList<>(Arrays.asList("decidium_z", "incinium_z", "lunalium_z", "lycanium_z", "marshadium_z", "mewnium_z", "pikanium_z", "pikashunium_z", "primanium_z", "snorlium_z", "solganium_z"));
        List<String> uber_valuables = new ArrayList<>(Arrays.asList("aloraichium_z", "eevium_z", "kommonium_z", "mimikium_z", "tapunium_z", "ultranecrozium_z"));
        EnumZCrystals[] enumZCrystals = EnumZCrystals.values();

        for (EnumZCrystals enumZ : enumZCrystals) if (!uber_valuables.contains(enumZ.getFileName()) && !valuables.contains(enumZ.getFileName())) regs.add(enumZ.getFileName());

        valuables.remove(heldItemId);
        uber_valuables.remove(heldItemId);
        regs.remove(heldItemId);

        if (roll <= 1) { //0 to 1, 2%
            winner = uber_valuables.get(new Random().nextInt(uber_valuables.size()));
        } else if (roll <= 10) { //2 to 10, 8%
            winner = valuables.get(new Random().nextInt(valuables.size()));
        } else {
            winner = regs.get(new Random().nextInt(regs.size()));
        }

        ItemStack stack = ItemStack.builder().itemType(Sponge.getRegistry().getType(ItemType.class, "pixelmon:" + winner).get()).build();

        Utils.sendMessage(player, "&aSuccessfully traded your &l"
                + heldItem.getTranslation().get()
                + "&a for a &l"
                + stack.getTranslation().get() + "&a!");

        heldItem.setQuantity(0);
        player.getInventory().offer(stack);
        Utils.setCooldown(player, 3);

        return CommandResult.success();
    }
}