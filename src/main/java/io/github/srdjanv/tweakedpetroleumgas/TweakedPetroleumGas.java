package io.github.srdjanv.tweakedpetroleumgas;


import io.github.srdjanv.tweakedlib.api.integration.DiscoveryHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = TweakedPetroleumGas.MODID,
        version = TweakedPetroleumGas.VERSION,
        name = "Tweaked Petroleum: Gas Addon",
        dependencies = "required-after:tweakedpetroleum@["+ Tags.TWEAKED_PETROLEUM_VERSION+",);" +
                "required-after:mekanism;")

public class TweakedPetroleumGas {

    public static final String MODID = "tweakedpetroleumgas";
    public static final String VERSION = Tags.VERSION;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        DiscoveryHandler.getInstance().preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        DiscoveryHandler.getInstance().init(event);
    }

    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        DiscoveryHandler.getInstance().postInit(event);
    }
}
