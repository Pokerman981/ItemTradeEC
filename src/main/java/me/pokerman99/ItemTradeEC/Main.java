package me.pokerman99.ItemTradeEC;



import com.google.inject.Inject;
import me.pokerman99.ItemTradeEC.Commands.ItemTradeCommand;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.config.ConfigDir;

import java.io.IOException;
import java.nio.file.Path;

@Plugin(
        id = "itemtradeec",
        name = "ItemTradeEC",
        version = "1.0",
        description = "A plugin for Justin's servers to provide random tms, evo, and mega items in exchange for there own current item of the three."
)
public class Main {


    @Inject
    @DefaultConfig(sharedRoot = false)
    public Path defaultConfig;

    @Inject
    @DefaultConfig(sharedRoot = false)
    public ConfigurationLoader<CommentedConfigurationNode> loader;

    @Inject
    @ConfigDir(sharedRoot = false)
    public Path ConfigDir;

    @Inject
    public PluginContainer plugin;
    public PluginContainer getPlugin() {
        return this.plugin;
    }

    public static CommentedConfigurationNode rootNode;

    public static Main instance;
    public static Main getInstance(){
        return instance;
    }

    public ConfigVariables configVariables;


    @Listener
    public void onInit(GameInitializationEvent event) throws IOException {
        rootNode = loader.load();
        instance = this;
        configVariables = new ConfigVariables();

        if (!defaultConfig.toFile().exists()) {
            //TODO Gen default config
            genDefaultConfig();
        }

        //TODO Register commands, listeners, populate variables
        registerCommands();

    }

    public void registerCommands() {
        CommandSpec itemtrade = CommandSpec.builder()
                .permission("itemtradeec.base")
                .executor(new ItemTradeCommand())
                .build();

        Sponge.getCommandManager().register(instance, itemtrade, "itemtrade");
    }

    public void populateVariables() {
        configVariables.setBaseCommandMessage(rootNode.getNode("base-command-message").getString());
    }

    public void genDefaultConfig() {
        rootNode.getNode("base-command-message").setValue("&6&l/itemtrade tm &8- Trade a TM/HM\n&6&l/itemtrade evo &8- Trade an evolution stone\n&6&l/itemtrade mega &8- Trade a mega stone").setComment("This auto uses pageintation");
        try {
            loader.save(rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
