package me.pokerman99.ItemTradeEC.Commands;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import com.pixelmonmod.pixelmon.enums.EnumMegaPokemon;
import me.pokerman99.ItemTradeEC.Utils;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;

import java.util.List;
import java.util.Random;

public class MegaCommand implements CommandExecutor {
    public static List<String> MEGASTONEITEMNAMES = Lists.newArrayList();

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        Player player = (Player) src;
        if (!player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
            Utils.sendMessage(src, "&cYou are not holding a mega stone");
            return CommandResult.empty();
        }

        ItemStack heldItem = player.getItemInHand(HandTypes.MAIN_HAND).get();
        String heldItemName = heldItem.getType().getId();

        if (heldItem.getQuantity() > 1) {
            Utils.sendMessage(src, "&cPlease only have one time in the stack!");
            return CommandResult.empty();
        }


        if (!MEGASTONEITEMNAMES.toString().contains(heldItemName)) {
            Utils.sendMessage(src, "&cThe supplied item is not a mega stone!");
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


        return CommandResult.empty();
    }
}
