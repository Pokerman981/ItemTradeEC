package me.pokerman99.ItemTradeEC.Commands;

import com.google.common.collect.Lists;
import me.pokerman99.ItemTradeEC.Main;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.List;

public class ItemTradeCommand  implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        List<Text> text = Lists.newArrayList();
        String message = Main.getInstance().configVariables.getBaseCommandMessage();
        text.add(sendMessageWithColor(message));

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


    public Text sendMessageWithColor(String text) {
        return TextSerializers.FORMATTING_CODE.deserialize(text).toText();
    }

}
