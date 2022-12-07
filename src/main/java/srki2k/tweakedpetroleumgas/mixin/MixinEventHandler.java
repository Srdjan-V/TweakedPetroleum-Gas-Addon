package srki2k.tweakedpetroleumgas.mixin;

import blusunrize.immersiveengineering.client.ClientProxy;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.Config;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.stone.TileEntityCoresample;
import blusunrize.immersiveengineering.common.items.ItemCoresample;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import flaxbeard.immersivepetroleum.common.EventHandler;
import flaxbeard.immersivepetroleum.common.entity.EntitySpeedboat;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
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
public abstract class MixinEventHandler {

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

    @Inject(method = "renderCoresampleInfo", cancellable = true, at = @At(
            value = "INVOKE",
            target = "Lblusunrize/immersiveengineering/client/ClientUtils;mc()Lnet/minecraft/client/Minecraft;",
            shift = At.Shift.BEFORE,
            ordinal = 1))
    private static void onRenderCoresampleInfo(RenderGameOverlayEvent.Post event, CallbackInfo ci) {
        EntityPlayer player = ClientUtils.mc().player;
        if (ClientUtils.mc().objectMouseOver != null) {
            boolean hammer = player.getHeldItem(EnumHand.MAIN_HAND) != null && Utils.isHammer(player.getHeldItem(EnumHand.MAIN_HAND));
            RayTraceResult mop = ClientUtils.mc().objectMouseOver;
            int amnt;
            if (mop != null && mop.getBlockPos() != null) {
                TileEntity tileEntity = player.world.getTileEntity(mop.getBlockPos());
                if (tileEntity instanceof TileEntityCoresample) {
                    IEBlockInterfaces.IBlockOverlayText overlayBlock = (IEBlockInterfaces.IBlockOverlayText) tileEntity;
                    String[] text = overlayBlock.getOverlayText(ClientUtils.mc().player, mop, hammer);
                    boolean useNixie = overlayBlock.useNixieFont(ClientUtils.mc().player, mop);
                    ItemStack coresample = ((TileEntityCoresample) tileEntity).coresample;
                    if (ItemNBTHelper.hasKey(coresample, "oil") && text != null && text.length > 0) {
                        final String resName = ItemNBTHelper.hasKey(coresample, "resType") ? ItemNBTHelper.getString(coresample, "resType") : "";
                        amnt = ItemNBTHelper.getInt(coresample, "oil");

                            Optional<IReservoirType> res = PumpjackHandler.reservoirList.keySet().stream().
                                    map(reservoirType -> (IReservoirType) reservoirType).
                                    filter(reservoirType -> reservoirType.getName().equals(resName)).
                                    findFirst();

                            String tooltip = null;
                            if (res.isPresent()) {
                                IReservoirType reservoirType = res.get();
                                String fluidName;
                                if (reservoirType.getReservoirContent() == TweakedPumpjackHandler.ReservoirContent.LIQUID) {
                                    if (amnt > 0) {
                                        int est = amnt / 1000 * 1000;
                                        Fluid fluid = FluidRegistry.getFluid(reservoirType.getStringFluid());
                                        fluidName = net.minecraft.util.text.translation.I18n.translateToLocal(fluid.getUnlocalizedName());
                                        tooltip = I18n.format("chat.immersivepetroleum.info.coresample.oil", (new DecimalFormat("#,###.##")).format(est), fluidName);
                                    } else if (reservoirType.getReplenishRate() > 0) {
                                        Fluid fluid = FluidRegistry.getFluid(reservoirType.getStringFluid());
                                        fluidName = net.minecraft.util.text.translation.I18n.translateToLocal(fluid.getUnlocalizedName());
                                        tooltip = I18n.format("chat.immersivepetroleum.info.coresample.oilRep", reservoirType.getReplenishRate(), fluidName);
                                    }
                                } else if (reservoirType.getReservoirContent() == TweakedPumpjackHandler.ReservoirContent.GAS) {
                                    if (amnt > 0) {
                                        int est = amnt / 1000 * 1000;
                                        Gas g = GasRegistry.getGas(reservoirType.getStringFluid());
                                        fluidName = g.getLocalizedName();
                                        tooltip = I18n.format("chat.immersivepetroleum.info.coresample.oil", (new DecimalFormat("#,###.##")).format(est), fluidName);
                                    } else if (reservoirType.getReplenishRate() > 0) {
                                        Gas g = GasRegistry.getGas(reservoirType.getStringFluid());
                                        fluidName = g.getLocalizedName();
                                        tooltip = I18n.format("chat.immersivepetroleum.info.coresample.oilRep", reservoirType.getReplenishRate(), fluidName);
                                    }
                                }
                            } else {
                                tooltip = I18n.format("chat.immersivepetroleum.info.coresample.noOil");
                            }

                            if (tooltip != null) {
                                FontRenderer font = useNixie ? ClientProxy.nixieFontOptional : ClientUtils.font();
                                int col = useNixie && Config.IEConfig.nixietubeFont ? 16750848 : 16777215;
                                int i = text.length;
                                font.drawString(tooltip, (float) (event.getResolution().getScaledWidth() / 2 + 8), (float) (event.getResolution().getScaledHeight() / 2 + 8 + i * font.FONT_HEIGHT), col, true);
                            }
                    }
                }
            } else if (mop != null && mop.entityHit != null && mop.entityHit instanceof EntitySpeedboat) {
                String[] text = ((EntitySpeedboat) mop.entityHit).getOverlayText(ClientUtils.mc().player, mop);
                if (text != null && text.length > 0) {
                    FontRenderer font = ClientUtils.font();
                    int col = 16777215;
                    int i = 0;

                    for (amnt = 0; amnt < text.length; ++amnt) {
                        String s = text[amnt];
                        if (s != null) {
                            font.drawString(s, (float) (event.getResolution().getScaledWidth() / 2 + 8), (float) (event.getResolution().getScaledHeight() / 2 + 8 + i++ * font.FONT_HEIGHT), col, true);
                        }
                    }
                }
            }
        }
        ci.cancel();
    }

}
