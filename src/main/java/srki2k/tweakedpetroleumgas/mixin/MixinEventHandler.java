package srki2k.tweakedpetroleumgas.mixin;

import blusunrize.immersiveengineering.common.items.ItemCoresample;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import flaxbeard.immersivepetroleum.common.EventHandler;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srki2k.tweakedpetroleum.api.crafting.TweakedPumpjackHandler;
import srki2k.tweakedpetroleum.api.ihelpers.IReservoirType;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

@Mixin(value = EventHandler.class, remap = false)
public class MixinEventHandler {

    @Inject(method = "handleItemTooltip", cancellable = true, at = @At(
            value = "INVOKE",
            target = "Lnet/minecraftforge/event/entity/player/ItemTooltipEvent;getItemStack()Lnet/minecraft/item/ItemStack;",
            shift = At.Shift.BEFORE))
    private static void onHandleItemTooltip(ItemTooltipEvent event, CallbackInfo ci) {
        ItemStack stack = event.getItemStack();

        if (stack.getItem() instanceof ItemCoresample && ItemNBTHelper.hasKey(stack, "oil")) {
            final String resName;
            if (ItemNBTHelper.hasKey(stack, "resType")) {
                resName = ItemNBTHelper.getString(stack, "resType");
            } else {
                resName = "";
            }

            Optional<IReservoirType> res = PumpjackHandler.reservoirList.keySet().stream().
                    map(reservoirType -> (IReservoirType) reservoirType).
                    filter(reservoirType -> reservoirType.getName().equals(resName)).
                    findFirst();
            List<String> tooltip = event.getToolTip();
            if (res.isPresent()) {
                IReservoirType reservoirType = res.get();

                int amnt = ItemNBTHelper.getInt(stack, "oil");
                String fluidName;
                if (reservoirType.getReservoirContent() == TweakedPumpjackHandler.ReservoirContent.LIQUID) {
                    if (amnt > 0) {
                        int est = amnt / 1000 * 1000;
                        Fluid fluid = FluidRegistry.getFluid(reservoirType.getStringFluid());
                        fluidName = net.minecraft.util.text.translation.I18n.translateToLocal(fluid.getUnlocalizedName());
                        tooltip.add(2, I18n.format("chat.immersivepetroleum.info.coresample.oil", (new DecimalFormat("#,###.##")).format(est), fluidName));
                    } else if (reservoirType.getReplenishRate() > 0) {
                        Fluid fluid = FluidRegistry.getFluid(reservoirType.getStringFluid());
                        fluidName = net.minecraft.util.text.translation.I18n.translateToLocal(fluid.getUnlocalizedName());
                        tooltip.add(2, I18n.format("chat.immersivepetroleum.info.coresample.oilRep", reservoirType.getReplenishRate(), fluidName));
                    }
                } else if (reservoirType.getReservoirContent() == TweakedPumpjackHandler.ReservoirContent.GAS) {
                    if (amnt > 0) {
                        int est = amnt / 1000 * 1000;
                        Gas g = GasRegistry.getGas(reservoirType.getStringFluid());
                        fluidName = g.getLocalizedName();
                        tooltip.add(2, I18n.format("chat.immersivepetroleum.info.coresample.oil", (new DecimalFormat("#,###.##")).format(est), fluidName));
                    } else if (reservoirType.getReplenishRate() > 0) {
                        Gas g = GasRegistry.getGas(reservoirType.getStringFluid());
                        fluidName = g.getLocalizedName();
                        tooltip.add(2, I18n.format("chat.immersivepetroleum.info.coresample.oilRep", reservoirType.getReplenishRate(), fluidName));
                    }
                }
            } else {
                tooltip.add(2, I18n.format("chat.immersivepetroleum.info.coresample.noOil"));
            }
        }
        ci.cancel();
    }
}
