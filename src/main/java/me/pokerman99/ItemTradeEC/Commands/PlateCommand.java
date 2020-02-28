package me.pokerman99.ItemTradeEC.Commands;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.enums.EnumPlate;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PlateCommand implements CommandExecutor {
    public static List<String> PLATEITEMNAMES = Lists.newArrayList();

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        Player player = (Player) src;

        if (!player.hasPermission("itemtradeec.bypass") && Utils.isOnCooldown(player)) return CommandResult.success();

        ConfigVariables configVariables = Main.getInstance().configVariables;

        if (!player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
            Utils.sendMessage(src, configVariables.getNotHoldingItemPlate());
            return CommandResult.empty();
        }

        ItemStack heldItem = player.getItemInHand(HandTypes.MAIN_HAND).get();
        String heldItemId = heldItem.getType().getId().replaceAll("pixelmon:", "");
        ArrayList<String> plate_types = new ArrayList<>();

        if (heldItem.getQuantity() > 1) {
            Utils.sendMessage(src, configVariables.getOnlyOneItemPlate());
            return CommandResult.empty();
        }

        for (EnumPlate plate : EnumPlate.values()) {
            String plate_id = plate.name().toLowerCase() + "_plate";
            plate_types.add(plate_id);
        }

        if (!plate_types.contains(heldItemId)) {
            Utils.sendMessage(src, configVariables.getNotHoldingItemPlate());
            return CommandResult.empty();
        }

        String pick = "";
        int roll = new Random().nextInt(100) + 1;
        List<String> tier1 = new ArrayList<>(Arrays.asList("pixie_plate", "stone_plate", "earth_plate", "mind_plate", "flame_plate", "dread_plate", "sky_plate"));
        List<String> tier2 = new ArrayList<>(Arrays.asList("draco_plate", "iron_plate", "zap_plate"));
        List<String> tier3 = new ArrayList<>(Arrays.asList("spooky_plate", "toxic_plate", "meadow_plate", "insect_plate", "icicle_plate", "splash_plate" ,"fist_plate")); //rare
        tier1.remove(heldItemId);
        tier2.remove(heldItemId);
        tier3.remove(heldItemId);

        if (roll <= 4) { //[1,4] = 4
            pick = tier3.get(new Random().nextInt(tier3.size()));
        } else if (roll <= 29) { //[5,29] = 25
            pick = tier2.get(new Random().nextInt(tier2.size()));
        } else { // [30,100]
            pick = tier1.get(new Random().nextInt(tier1.size()));
        }

        ItemStack stack = ItemStack.builder().itemType(Sponge.getRegistry().getType(ItemType.class, "pixelmon:" + pick).get()).build();

        Utils.sendMessage(player, "&aSuccessfully traded your &l"
                + heldItem.getTranslation().get()
                + "&a for a &l"
                + stack.getTranslation().get() + "&a!");

        heldItem.setQuantity(0);
        player.getInventory().offer(stack);
        Utils.setCooldown(player, 36);

        return CommandResult.success();
    }
}