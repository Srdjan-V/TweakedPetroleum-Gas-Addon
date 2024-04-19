package io.github.srdjanv.tweakedpetroleumgas.mixin;


import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.IManualPage;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualPages;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import flaxbeard.immersivepetroleum.client.ClientProxy;
import io.github.srdjanv.tweakedpetroleum.api.mixins.ITweakedPetReservoirType;
import io.github.srdjanv.tweakedpetroleumgas.api.mixins.ITweakedGasReservoirType;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mekanism.api.gas.GasStack;
import mekanism.common.MekanismBlocks;
import mekanism.common.item.ItemBlockGasTank;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.text.DecimalFormat;
import java.util.*;

@Mixin(value = ClientProxy.class, remap = false)
public abstract class MixinClientProxy {

    @Shadow
    static ManualInstance.ManualEntry resEntry;


    @Overwrite
    public static void handleReservoirManual() {
        if (ManualHelper.getManual() != null) {
            DecimalFormat f = new DecimalFormat("#,###.##");
            List<IManualPage> pages = new ObjectArrayList<>();
            pages.add(new ManualPages.Text(ManualHelper.getManual(), "oil0"));
            pages.add(new ManualPages.Text(ManualHelper.getManual(), "oil1"));
            PumpjackHandler.ReservoirType[] reservoirTypes = PumpjackHandler.reservoirList.keySet().toArray(new PumpjackHandler.ReservoirType[0]);
            StringBuilder builder = new StringBuilder();
            StringBuilder dimBuilder = new StringBuilder();
            StringBuilder biomeBuilder = new StringBuilder();

            resLoop:
            for (PumpjackHandler.ReservoirType type : reservoirTypes) {
                String name = "desc.immersivepetroleum.info.reservoir." + type.name;
                String localizedName = I18n.format(name);
                if (localizedName.equalsIgnoreCase(name)) localizedName = type.name;

                boolean isVowel = localizedName.toLowerCase().charAt(0) == 'a' || localizedName.toLowerCase().charAt(0) == 'e' || localizedName.toLowerCase().charAt(0) == 'i' || localizedName.toLowerCase().charAt(0) == 'o' || localizedName.toLowerCase().charAt(0) == 'u';
                String aOrAn = I18n.format(isVowel ? "ie.manual.entry.oilVowel" : "ie.manual.entry.oilConsonant");
                builder.setLength(0);
                dimBuilder.setLength(0);
                biomeBuilder.setLength(0);

                if ((type.dimensionWhitelist != null && type.dimensionWhitelist.length > 0) || (type.dimensionBlacklist != null && type.dimensionBlacklist.length > 0)) {
                    if (type.dimensionWhitelist != null && type.dimensionWhitelist.length > 0) {
                        builder.setLength(0);
                        for (int dim : type.dimensionWhitelist)
                            builder.append(builder.length() != 0 ? ", " : "").append("<dim;").append(dim).append(">");

                        dimBuilder.append(I18n.format("ie.manual.entry.oilDimValid", localizedName, builder, aOrAn));
                    } else {
                        builder.setLength(0);
                        for (int dim : type.dimensionBlacklist)
                            builder.append(builder.length() != 0 ? ", " : "").append("<dim;").append(dim).append(">");

                        dimBuilder.append(I18n.format("ie.manual.entry.oilDimInvalid", localizedName, builder, aOrAn));
                    }

                } else dimBuilder.append(I18n.format("ie.manual.entry.oilDimAny", localizedName, aOrAn));

                if ((type.biomeWhitelist != null && type.biomeWhitelist.length > 0) || (type.biomeBlacklist != null && type.biomeBlacklist.length > 0)) {
                    if (type.biomeWhitelist != null && type.biomeWhitelist.length > 0) {
                        builder.setLength(0);
                        for (String biome : type.biomeWhitelist)
                            builder.append(builder.length() != 0 ? ", " : "").append(PumpjackHandler.getTagDisplayName(biome));
                        biomeBuilder.append(I18n.format("ie.manual.entry.oilBiomeValid", builder));
                    } else {
                        builder.setLength(0);
                        for (String biome : type.biomeBlacklist)
                            builder.append(builder.length() != 0 ? ", " : "").append(PumpjackHandler.getTagDisplayName(biome));

                        biomeBuilder.append(I18n.format("ie.manual.entry.oilBiomeInvalid", builder));
                    }

                } else biomeBuilder.append(I18n.format("ie.manual.entry.oilBiomeAny"));


                String resContent = "";
                ItemStack displayStack = ItemStack.EMPTY;
                ITweakedPetReservoirType tweakedType = (ITweakedPetReservoirType) type;
                switch (tweakedType.getReservoirContent()) {
                    case GAS -> {
                        var gasType = (ITweakedGasReservoirType) tweakedType;
                        resContent = gasType.getGas().getLocalizedName();
                        displayStack = new ItemStack(MekanismBlocks.GasTank);
                        var blockGasTank = (ItemBlockGasTank) ItemBlockGasTank.getItemFromBlock(MekanismBlocks.GasTank);
                        blockGasTank.addGas(displayStack, new GasStack(gasType.getGas(), Integer.MAX_VALUE));
                    }
                    case LIQUID -> {
                        Fluid fluid = type.getFluid();
                        if (fluid != null) {
                            String unlocalizedName = fluid.getUnlocalizedName();
                            resContent = unlocalizedName == null ? "" : I18n.format(unlocalizedName);
                        }

                        UniversalBucket bucket = ForgeModContainer.getInstance().universalBucket;
                        ItemStack stack = new ItemStack(bucket);
                        FluidStack fs = new FluidStack(fluid, bucket.getCapacity());
                        IFluidHandlerItem fluidHandler = new FluidBucketWrapper(stack);
                        fluidHandler.fill(fs, true);
                        displayStack = fluidHandler.getContainer();
                    }
                    case EMPTY, DEFAULT -> {
                        continue resLoop;
                    }
                }

                String replenishRate = "";
                if (type.replenishRate > 0)
                    replenishRate = I18n.format("ie.manual.entry.oilReplenish", type.replenishRate, resContent);

                pages.add(new ManualPages.ItemDisplay(ManualHelper.getManual(), switch (tweakedType.getReservoirContent()) {
                    case LIQUID -> I18n.format("ie.manual.entry.oil2",
                            dimBuilder, resContent,
                            f.format(type.minSize), f.format(type.maxSize),
                            replenishRate, biomeBuilder);
                    case GAS -> I18n.format("ie.manual.entry.oil2",
                            dimBuilder, resContent,
                            f.format(type.minSize), f.format(type.maxSize),
                            replenishRate, biomeBuilder)
                            .replace("fluid reservoir", "gas reservoir");//todo fix, wont work if localized
                    case EMPTY, DEFAULT -> {
                        throw new IllegalStateException();
                    }
                }, displayStack));
            }

            if (resEntry != null) {
                resEntry.setPages(pages.toArray(new IManualPage[0]));
            } else {
                ManualHelper.addEntry("oil", "ip", pages.toArray(new IManualPage[0]));
                resEntry = ManualHelper.getManual().getEntry("oil");
            }
        }
    }

}