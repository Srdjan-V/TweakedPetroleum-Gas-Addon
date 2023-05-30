package io.github.srdjanv.tweakedpetroleumgas.mixin;


import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import flaxbeard.immersivepetroleum.client.ClientProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import io.github.srdjanv.tweakedpetroleum.api.crafting.TweakedPumpjackHandler;
import io.github.srdjanv.tweakedpetroleum.api.ihelpers.IReservoirType;

import java.util.Iterator;
import java.util.LinkedHashMap;

@Mixin(value = ClientProxy.class, remap = false)
public abstract class MixinClientProxy {

    @Redirect(method = "handleReservoirManual", at = @At(value = "FIELD", target = "Lflaxbeard/immersivepetroleum/api/crafting/PumpjackHandler;reservoirList:Ljava/util/LinkedHashMap;"))
    private static LinkedHashMap<PumpjackHandler.ReservoirType, Integer> onHandleReservoirManual() {
        LinkedHashMap<PumpjackHandler.ReservoirType, Integer> reservoirList = new LinkedHashMap<>();
        Iterator<PumpjackHandler.ReservoirType> keySet = PumpjackHandler.reservoirList.keySet().iterator();

        int i = 0;
        while (keySet.hasNext()) {
            PumpjackHandler.ReservoirType reservoirType = keySet.next();
            if (((IReservoirType) reservoirType).getReservoirContent() == TweakedPumpjackHandler.ReservoirContent.LIQUID) {
                reservoirList.put(reservoirType, i);
                i++;
            }
        }

        return reservoirList;
    }

}