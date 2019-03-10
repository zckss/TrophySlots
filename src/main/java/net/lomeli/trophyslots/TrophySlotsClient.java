package net.lomeli.trophyslots;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lomeli.knit.config.ConfigFile;
import net.lomeli.knit.utils.Logger;
import net.lomeli.knit.utils.network.MessageUtil;
import net.lomeli.trophyslots.client.ClientConfig;
import net.lomeli.trophyslots.client.handler.EventHandlerClient;
import net.lomeli.trophyslots.client.handler.SpriteHandler;
import net.lomeli.trophyslots.core.network.MessageClientConfig;
import net.lomeli.trophyslots.core.network.MessageServerConfig;
import net.lomeli.trophyslots.core.network.MessageSlotClient;

@Environment(EnvType.CLIENT)
public class TrophySlotsClient implements ClientModInitializer {

    public static Logger log;

    public static ConfigFile config;

    @Override
    public void onInitializeClient() {
        log = new Logger(TrophySlots.MOD_NAME + "/Client");

        log.info("Loading client configs");
        config = new ConfigFile(TrophySlots.MOD_ID + "_client", ClientConfig.class);
        config.loadConfig();

        log.info("Registering client events");
        SpriteHandler.stitchSprites();
        EventHandlerClient.initClientEvents();

        log.info("Registering client packets");
        MessageUtil.registerMessage(new MessageSlotClient(), EnvType.CLIENT);
        MessageUtil.registerMessage(new MessageServerConfig(), EnvType.CLIENT);
        MessageUtil.registerMessage(new MessageClientConfig(), EnvType.CLIENT);
    }
}
