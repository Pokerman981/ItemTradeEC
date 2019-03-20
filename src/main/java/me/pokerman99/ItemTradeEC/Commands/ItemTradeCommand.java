package me.pokerman99.ItemTradeEC.Commands;

import com.google.common.collect.Lists;
import me.pokerman99.ItemTradeEC.Main;
import me.pokerman99.ItemTradeEC.Utils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.List;

public class ItemTradeCommand  implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        List<Text> text = Lists.newArrayList();
        text.add(Utils.sendMessage(Main.getInstance().configVariables.getBaseCommandMessage()));

        PaginationList.builder()
                .contents(text)
                .padding(Text.of(
                        TextStyles.RESET, TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH, "-"
                ))
                .title(Text.of(
                        TextColors.GREEN, "Item Trade"
                ))
                .sendTo(src);

        return CommandResult.success();
    }
}
