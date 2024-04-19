package io.github.srdjanv.tweakedpetroleumgas.client;

import blusunrize.immersiveengineering.common.IEContent;
import io.github.srdjanv.tweakedpetroleumgas.client.model.ModelCoresampleGasExtended;
import io.github.srdjanv.tweakedpetroleumgas.common.Configs;
import io.github.srdjanv.tweakedpetroleumgas.util.TweakedPetroleumGasInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientHandler implements TweakedPetroleumGasInitializer {
    @Override public boolean shouldRun() {
        return FMLCommonHandler.instance().getSide().isClient();
    }

    @Override public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override public void init(FMLInitializationEvent event) {
        //this will allow the application of fluid and gas color tint
        if (Configs.clientConfig.replaceCoreSampleModel)
            Minecraft.getMinecraft().getItemColors().registerItemColorHandler(
                    (stack, tintIndex) -> Math.max(tintIndex, -1),
                    IEContent.itemCoresample);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onModelBakeEvent(ModelBakeEvent event) {
        if (Configs.clientConfig.replaceCoreSampleModel) {
            ModelResourceLocation mLoc = new ModelResourceLocation(new ResourceLocation("immersiveengineering", IEContent.itemCoresample.itemName), "inventory");
            event.getModelRegistry().putObject(mLoc, new ModelCoresampleGasExtended());
        }
    }
}
