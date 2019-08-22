package me.pokerman99.ItemTradeEC;

import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.enums.EnumMegaPokemon;
import com.pixelmonmod.pixelmon.enums.EnumPlate;
import com.pixelmonmod.pixelmon.enums.items.EnumZCrystals;
import me.pokerman99.ItemTradeEC.Commands.*;
import net.minecraft.item.Item;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

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
            genDefaultConfig();
        }

        loadPlateNames();
        loadZCrystalNames();
        loadMegaStoneNames();

        registerCommands();
        populateVariables();
    }

    public void populateVariables() {
        configVariables.setBaseCommandMessage(rootNode.getNode("base-command-message").getString());

        configVariables.setNotHoldingItemTM(rootNode.getNode("commands", "tm-hm", "not-holding-item").getString());
        configVariables.setOnlyOneItemTM(rootNode.getNode("commands", "tm-hm", "only-one-item").getString());

        configVariables.setNotHoldingItemEVO(rootNode.getNode("commands", "evo", "not-holding-item").getString());
        configVariables.setOnlyOneItemEVO(rootNode.getNode("commands", "evo", "only-one-item").getString());

        configVariables.setNotHoldingItemMEGA(rootNode.getNode("commands", "mega", "not-holding-item").getString());
        configVariables.setOnlyOneItemMEGA(rootNode.getNode("commands", "mega", "only-one-item").getString());

        configVariables.setNotHoldingItemZCrystal(rootNode.getNode("commands", "zcrystal", "not-holding-item").getString());
        configVariables.setOnlyOneItemZCrystal(rootNode.getNode("commands", "zcrystal", "only-one-item").getString());

        configVariables.setNotHoldingItemPlate(rootNode.getNode("commands", "plate", "not-holding-item").getString());
        configVariables.setOnlyOneItemPlate(rootNode.getNode("commands", "plate", "only-one-item").getString());
    }

    public void genDefaultConfig() {
        rootNode.getNode("base-command-message").setValue("&6&l/itemtrade tm &8- Trade a TM/HM\n&6&l/itemtrade evo &8- Trade an evolution stone\n&6&l/itemtrade mega &8- Trade a mega stone").setComment("This auto uses pageintation");

        rootNode.getNode("commands", "tm-hm", "not-holding-item").setValue("&cYou are not holding a TM/HM");
        rootNode.getNode("commands", "tm-hm", "only-one-item").setValue("&cPlease only have one item in the stack!");

        rootNode.getNode("commands", "evo", "not-holding-item").setValue("&cYou are not holding a evolution stone");
        rootNode.getNode("commands", "evo", "only-one-item").setValue("&cPlease only have one item in the stack!");

        rootNode.getNode("commands", "mega", "not-holding-item").setValue("&cYou are not holding a mega stone");
        rootNode.getNode("commands", "mega", "only-one-item").setValue("&cPlease only have one item in the stack!");

        rootNode.getNode("commands", "zcrystal", "not-holding-item").setValue("&cYou are not holding a Z-Crystal");
        rootNode.getNode("commands", "zcrystal", "only-one-item").setValue("&cPlease only have one item in the stack!");

        rootNode.getNode("commands", "plate", "not-holding-item").setValue("&cYou are not holding a plate");
        rootNode.getNode("commands", "plate", "only-one-item").setValue("&cPlease only have one item in the stack!");

        try {
            loader.save(rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerCommands() {
        CommandSpec tm = CommandSpec.builder()
                .permission("itemtradeec.command.tm")
                .executor(new TMCommand())
                .build();

        CommandSpec evo = CommandSpec.builder()
                .permission("itemtradeec.command.evo")
                .executor(new EvoCommand())
                .build();

        CommandSpec mega = CommandSpec.builder()
                .permission("itemtradeec.command.mega")
                .executor(new MegaCommand())
                .build();

        CommandSpec zcrystal = CommandSpec.builder()
                .permission("itemtradeec.command.zcrystal")
                .executor(new ZCrystalCommand())
                .build();

        CommandSpec plate = CommandSpec.builder()
                .permission("itemtradeec.command.plate")
                .executor(new PlateCommand())
                .build();

        CommandSpec itemtrade = CommandSpec.builder()
                .permission("itemtradeec.base")
                .child(tm, "tm", "hm")
                .child(evo, "evo")
                .child(mega, "mega")
                .child(plate, "plate")
                .child(zcrystal, "zcrystal")
                .executor(new ItemTradeCommand())
                .build();

        Sponge.getCommandManager().register(instance, itemtrade, "itemtrade");
    }

    public void loadMegaStoneNames() {
        EnumMegaPokemon[] enumMegaPokemon = EnumMegaPokemon.values();
        for (EnumMegaPokemon enumMegaPokemon1 : enumMegaPokemon) {
            Item[] items = enumMegaPokemon1.getMegaEvoItems();

            for (Item item : items) {
                String itemName = item.getRegistryName().toString();
                if (itemName.contains("air")) {
                    continue;
                }

                MegaCommand.MEGASTONEITEMNAMES.add(itemName);
            }
        }
    }

    public void loadZCrystalNames() {
        EnumZCrystals[] enumZCrystals = EnumZCrystals.values();
        for (EnumZCrystals enumZ : enumZCrystals) {
            ZCrystalCommand.ZCRYSTALITEMNAMES.add(enumZ.getFileName());
        }
    }

    public void loadPlateNames() {
        EnumPlate[] enumZCrystals = EnumPlate.values();
        for (EnumPlate enumPlate : enumZCrystals) {
            String name = "pixelmon:" + enumPlate.getItem().getUnlocalizedName().replace("item.", "");
            PlateCommand.PLATEITEMNAMES.add(name);
        }
    }
}