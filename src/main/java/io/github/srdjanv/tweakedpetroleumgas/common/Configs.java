package io.github.srdjanv.tweakedpetroleumgas.common;


import io.github.srdjanv.tweakedpetroleumgas.TweakedPetroleumGas;
import net.minecraftforge.common.config.Config;

import static net.minecraftforge.common.config.Config.*;

@Config(modid = TweakedPetroleumGas.MODID)
public class Configs {
    public static ClientConfig clientConfig = new ClientConfig();

    public static class  ClientConfig {
        @RequiresMcRestart
        @Name("Replace Core Sample Model")
        @Comment("This will enable correct gas reservoir rendering, In case of errors disable")
        public boolean replaceCoreSampleModel = true;

    }
}
