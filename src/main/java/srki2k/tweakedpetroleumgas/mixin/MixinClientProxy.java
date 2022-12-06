package srki2k.tweakedpetroleumgas.mixin;


import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import flaxbeard.immersivepetroleum.client.ClientProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srki2k.tweakedpetroleum.api.crafting.TweakedPumpjackHandler;
import srki2k.tweakedpetroleum.api.util.IReservoirType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(value = ClientProxy.class, remap = false)
public abstract class MixinClientProxy {

    @Redirect(method = "handleReservoirManual", at = @At(value = "FIELD", target = "Lflaxbeard/immersivepetroleum/api/crafting/PumpjackHandler;reservoirList:Ljava/util/LinkedHashMap;"))
    private static LinkedHashMap<PumpjackHandler.ReservoirType, Integer> onHandleReservoirManual() {
        return PumpjackHandler.reservoirList.entrySet().stream().
                filter(reservoir -> ((IReservoirType) reservoir.getKey()).getReservoirContent() == TweakedPumpjackHandler.ReservoirContent.LIQUID).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum, LinkedHashMap::new));
    }

}