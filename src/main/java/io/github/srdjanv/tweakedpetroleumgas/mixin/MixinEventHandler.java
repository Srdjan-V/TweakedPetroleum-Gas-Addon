package io.github.srdjanv.tweakedpetroleumgas.mixin;

import blusunrize.immersiveengineering.client.ClientProxy;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.Config;
import blusunrize.immersiveengineering.common.blocks.stone.TileEntityCoresample;
import blusunrize.immersiveengineering.common.items.ItemCoresample;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import flaxbeard.immersivepetroleum.common.EventHandler;
import flaxbeard.immersivepetroleum.common.entity.EntitySpeedboat;
import io.github.srdjanv.tweakedpetroleum.api.mixins.ITweakedPetReservoirType;
import io.github.srdjanv.tweakedpetroleumgas.api.mixins.ITweakedGasReservoirType;
import mekanism.api.gas.Gas;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

@Mixin(value = EventHandler.class, remap = false)
public abstract class MixinEventHandler {

    @Overwrite
    @SubscribeEvent
    static void handleItemTooltip(ItemTooltipEvent event) {
        final ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof ItemCoresample) || !ItemNBTHelper.hasKey(stack, "oil")) return;
        final List<String> tooltip = event.getToolTip();
        final String resName;
        if (ItemNBTHelper.hasKey(stack, "resType")) {
            resName = ItemNBTHelper.getString(stack, "resType");
        } else resName = null;

        Optional<ITweakedPetReservoirType> res = PumpjackHandler.reservoirList.keySet().stream()
                .map(reservoirType -> (ITweakedPetReservoirType) reservoirType)
                .filter(reservoirType -> reservoirType.getName().equals(resName))
                .findFirst();

        if (res.isPresent()) {
            ITweakedPetReservoirType reservoirType = res.get();
            int amount = ItemNBTHelper.getInt(stack, "oil");
            String localizedName;
            switch (reservoirType.getReservoirContent()) {
                case LIQUID -> {
                    Fluid fluid = ((PumpjackHandler.ReservoirType) reservoirType).getFluid();
                    localizedName = I18n.format(fluid.getUnlocalizedName());
                }
                case GAS -> {
                    Gas gas = ((ITweakedGasReservoirType) reservoirType).getGas();
                    localizedName = gas.getLocalizedName();
                }
                default -> {
                    return;
                }
            }
            if (amount > 0) {
                int est = amount / 1000 * 1000;
                tooltip.add(2, I18n.format("chat.immersivepetroleum.info.coresample.oil", (new DecimalFormat("#,###.##")).format(est), localizedName));
            } else if (reservoirType.getReplenishRate() > 0) {
                tooltip.add(2, I18n.format("chat.immersivepetroleum.info.coresample.oilRep", reservoirType.getReplenishRate(), localizedName));
            }
        } else tooltip.add(2, I18n.format("chat.immersivepetroleum.info.coresample.noOil"));
    }

    @Overwrite
    @SubscribeEvent
    static void renderCoresampleInfo(RenderGameOverlayEvent.Post event) {
        if (ClientUtils.mc().player == null || event.getType() != RenderGameOverlayEvent.ElementType.TEXT) return;

        EntityPlayer player = ClientUtils.mc().player;
        if (ClientUtils.mc().objectMouseOver == null) return;

        boolean hammer = player.getHeldItem(EnumHand.MAIN_HAND) != null && Utils.isHammer(player.getHeldItem(EnumHand.MAIN_HAND));
        RayTraceResult mop = ClientUtils.mc().objectMouseOver;
        if (mop == null) return;

        switch (mop.typeOfHit) {
            case BLOCK -> {
                if (!(player.world.getTileEntity(mop.getBlockPos()) instanceof TileEntityCoresample coresampleTile)) return;
                String[] text = coresampleTile.getOverlayText(ClientUtils.mc().player, mop, hammer);
                boolean useNixie = coresampleTile.useNixieFont(ClientUtils.mc().player, mop);
                ItemStack coresample = coresampleTile.coresample;
                if (!ItemNBTHelper.hasKey(coresample, "oil") || text == null || text.length == 0) return;
                final String resName = ItemNBTHelper.hasKey(coresample, "resType") ? ItemNBTHelper.getString(coresample, "resType") : "";
                int amount = ItemNBTHelper.getInt(coresample, "oil");

                Optional<ITweakedPetReservoirType> res = PumpjackHandler.reservoirList.keySet().stream().
                        map(reservoirType -> (ITweakedPetReservoirType) reservoirType).
                        filter(reservoirType -> reservoirType.getName().equals(resName)).
                        findFirst();

                String tooltip = null;
                if (res.isPresent()) {
                    ITweakedPetReservoirType reservoirType = res.get();
                    String localizedName;
                    switch (reservoirType.getReservoirContent()) {
                        case LIQUID -> {
                            Fluid fluid = ((PumpjackHandler.ReservoirType) reservoirType).getFluid();
                            localizedName = I18n.format(fluid.getUnlocalizedName());
                        }
                        case GAS -> {
                            Gas gas = ((ITweakedGasReservoirType) reservoirType).getGas();
                            localizedName = gas.getLocalizedName();
                        }
                        default -> {
                            return;
                        }
                    }
                    if (amount > 0) {
                        int est = amount / 1000 * 1000;
                        tooltip = I18n.format("chat.immersivepetroleum.info.coresample.oil", (new DecimalFormat("#,###.##")).format(est), localizedName);
                    } else if (reservoirType.getReplenishRate() > 0)
                        tooltip = I18n.format("chat.immersivepetroleum.info.coresample.oilRep", reservoirType.getReplenishRate(), localizedName);
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

            case ENTITY -> {
                if (!(mop.entityHit instanceof EntitySpeedboat)) return;
                String[] text = ((EntitySpeedboat) mop.entityHit).getOverlayText(ClientUtils.mc().player, mop);
                if (text == null) return;

                int i = 0;
                FontRenderer font = ClientUtils.font();
                for (String t : text) {
                    if (t != null)
                        font.drawString(t,
                                (float) (event.getResolution().getScaledWidth() / 2 + 8),
                                (float) (event.getResolution().getScaledHeight() / 2 + 8 + i++ * font.FONT_HEIGHT),
                                16777215, true);
                }
            }
        }
    }

}
