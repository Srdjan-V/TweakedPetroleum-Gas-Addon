package io.github.srdjanv.tweakedpetroleumgas.util;

import io.github.srdjanv.tweakedlib.api.integration.IInitializer;
import io.github.srdjanv.tweakedpetroleumgas.TweakedPetroleumGas;

public interface TweakedPetroleumGasInitializer extends IInitializer {
    default String getModID() {
        return TweakedPetroleumGas.MODID;
    }
}
