package io.github.srdjanv.tweakedpetroleumgas;


import net.minecraftforge.fml.common.Mod;

@Mod(modid = TweakedPetroleumGas.MODID,
        version = TweakedPetroleumGas.VERSION,
        name = "Tweaked Petroleum: Gas Addon",
        dependencies = "required-after:tweakedpetroleum@["+ Tags.TWEAKED_PETROLEUM_VERSION+",);" +
                "required-after:mekanism;")

public class TweakedPetroleumGas {

    public static final String MODID = "tweakedpetroleumgas";
    public static final String VERSION = Tags.VERSION;

}
