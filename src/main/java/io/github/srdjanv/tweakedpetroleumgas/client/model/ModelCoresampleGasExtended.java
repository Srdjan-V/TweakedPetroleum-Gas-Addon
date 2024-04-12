package io.github.srdjanv.tweakedpetroleumgas.client.model;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import flaxbeard.immersivepetroleum.client.model.ModelCoresampleExtended;
import flaxbeard.immersivepetroleum.common.IPContent;
import flaxbeard.immersivepetroleum.common.blocks.metal.BlockTypes_Dummy;
import io.github.srdjanv.tweakedpetroleumgas.api.mixins.ITweakedGasReservoirType;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mekanism.api.gas.Gas;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.fluids.Fluid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public class ModelCoresampleGasExtended extends ModelCoresampleExtended {
    private final static Logger logger = LogManager.getLogger(ModelCoresampleGasExtended.class);

    public static Map<String, ModelCoresampleGasExtended> modelCache = new Object2ObjectOpenHashMap<>();
    private final Supplier<ContentData> contentDataSupplier;
    private final ExcavatorHandler.MineralMix mineral;
    private final ItemOverrideList overrideList;
    private List<BakedQuad> bakedQuads;

    public ModelCoresampleGasExtended(ExcavatorHandler.MineralMix mineral,
                                      com.google.common.base.Supplier<ContentData> contentDataSupplier) {
        super(mineral);
        overrideList = new ItemOverrideList(Collections.emptyList()) {
            public @NotNull IBakedModel handleItemState(@NotNull IBakedModel originalModel, @NotNull ItemStack stack, World world, EntityLivingBase entity) {
                String resName = ItemNBTHelper.hasKey(stack, "resType") ? ItemNBTHelper.getString(stack, "resType") : null;
                if (ItemNBTHelper.hasKey(stack, "oil") && resName == null && ItemNBTHelper.getInt(stack, "oil") > 0) {
                    resName = "oil";
                }

                if (ItemNBTHelper.hasKey(stack, "mineral")) {
                    final String name = ItemNBTHelper.getString(stack, "mineral");
                    String indexName = resName == null ? name : name + "_" + resName;
                    if (name != null && !name.isEmpty()) {
                        if (!modelCache.containsKey(indexName)) {
                            var opMix = ExcavatorHandler.mineralList.keySet()
                                    .stream()
                                    .filter(mixx -> name.equals(mixx.name))
                                    .findFirst();

                            lableRet:
                            if (opMix.isPresent()) {
                                var mix = opMix.get();
                                if (resName != null) {
                                    for (PumpjackHandler.ReservoirType type : PumpjackHandler.reservoirList.keySet()) {
                                        if (!resName.equals(type.name)) continue;

                                        String[] newOres = new String[mix.ores.length + 1];
                                        float[] newChances = new float[mix.chances.length + 1];
                                        newOres[mix.ores.length] = "obsidian";
                                        newChances[mix.ores.length] = 0.4F;

                                        for (int i = 0; i < mix.ores.length; ++i) {
                                            newOres[i] = mix.ores[i];
                                            newChances[i] = mix.chances[i];
                                        }

                                        ExcavatorHandler.MineralMix mix2 = new ExcavatorHandler.MineralMix(mix.name, mix.failChance, newOres, newChances);
                                        mix2.recalculateChances();
                                        mix2.oreOutput.set(mix2.oreOutput.size() - 1, new ItemStack(IPContent.blockDummy, 1, BlockTypes_Dummy.OIL_DEPOSIT.getMeta()));
                                        switch (((ITweakedGasReservoirType) type).getReservoirContent()) {
                                            case GAS ->
                                                    modelCache.put("_" + resName, new ModelCoresampleGasExtended(mix2, ((ITweakedGasReservoirType) type).getGas()));
                                            case LIQUID ->
                                                    modelCache.put("_" + resName, new ModelCoresampleGasExtended(mix2, type.getFluid()));
                                            case EMPTY, DEFAULT ->
                                                    modelCache.put("_" + resName, new ModelCoresampleGasExtended(mix2, ContentData.NONE));
                                        }
                                        break lableRet;
                                    }

                                    modelCache.put(indexName, new ModelCoresampleGasExtended(mix));
                                } else {
                                    modelCache.put(indexName, new ModelCoresampleGasExtended(mix));
                                }
                            }
                        }

                        IBakedModel modelx = modelCache.get(indexName);
                        if (modelx != null) return modelx;
                    }
                }

                if (resName != null) {
                    if (!modelCache.containsKey("_" + resName)) {
                        for (PumpjackHandler.ReservoirType typex : PumpjackHandler.reservoirList.keySet()) {
                            if (!resName.equals(typex.name)) continue;

                            ExcavatorHandler.MineralMix mixx = new ExcavatorHandler.MineralMix(resName, 1.0F, new String[]{"obsidian"}, new float[]{1.0F});
                            mixx.recalculateChances();
                            mixx.oreOutput.set(0, new ItemStack(IPContent.blockDummy, 1, BlockTypes_Dummy.OIL_DEPOSIT.getMeta()));
                            switch (((ITweakedGasReservoirType) typex).getReservoirContent()) {
                                case GAS ->
                                        modelCache.put("_" + resName, new ModelCoresampleGasExtended(mixx, ((ITweakedGasReservoirType) typex).getGas()));
                                case LIQUID ->
                                        modelCache.put("_" + resName, new ModelCoresampleGasExtended(mixx, typex.getFluid()));
                                case EMPTY, DEFAULT ->
                                        modelCache.put("_" + resName, new ModelCoresampleGasExtended(mixx, ContentData.NONE));
                            }
                        }
                    }

                    IBakedModel model = modelCache.get("_" + resName);
                    if (model != null) return model;
                }

                return originalModel;
            }
        };

        this.mineral = mineral;
        this.contentDataSupplier = contentDataSupplier == null ? ContentData.NONE : Suppliers.memoize(contentDataSupplier);
    }

    public ModelCoresampleGasExtended(ExcavatorHandler.MineralMix mineral, Gas gas) {
        this(mineral, ContentData.gas(gas));
    }

    public ModelCoresampleGasExtended(ExcavatorHandler.MineralMix mineral, Fluid fluid) {
        this(mineral, ContentData.fluid(fluid));
    }

    public ModelCoresampleGasExtended(ExcavatorHandler.MineralMix mineral) {
        this(mineral, (com.google.common.base.Supplier<ContentData>) null);
    }

    public ModelCoresampleGasExtended() {
        this(null);
    }

    public List<BakedQuad> getQuads(@Nullable IBlockState blockState, @Nullable EnumFacing side, long rand) {
        if (bakedQuads == null) bakedQuads = buildBakedQuads();
        return bakedQuads;
    }

    @NotNull
    protected List<BakedQuad> buildBakedQuads() {
        List<BakedQuad> bakedQuads = new ObjectArrayList<>();
        try {
            float width = 0.25F;
            float depth = 0.25F;
            float wOff = (1.0F - width) / 2.0F;
            float dOff = (1.0F - depth) / 2.0F;
            float fWidth = 0.24F;
            float fDepth = 0.24F;
            float fWOff = (1.0F - fWidth) / 2.0F;
            float fDOff = (1.0F - fDepth) / 2.0F;
            int pixelLength = 0;
            Object2IntMap<TextureAtlasSprite> textureOre = new Object2IntOpenHashMap<>();
            if (this.mineral != null && this.mineral.oreOutput != null) {
                NonNullList<ItemStack> oreOutput = this.mineral.oreOutput;
                for (int i = 0; i < oreOutput.size(); i++) {
                    final ItemStack itemStack = oreOutput.get(i);
                    if (itemStack.isEmpty()) continue;
                    int weight = Math.max(2, Math.round(16.0F * this.mineral.recalculatedChances[i]));
                    Block block = Block.getBlockFromItem(itemStack.getItem());
                    IBlockState state = block != Blocks.AIR ? block.getStateFromMeta(itemStack.getMetadata()) : Blocks.STONE.getDefaultState();
                    IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(state);
                    if (block == IPContent.blockDummy) {
                        textureOre.put(null, weight);
                    } else {
                        var particleTexture = model.getParticleTexture();
                        if (particleTexture != null) textureOre.put(particleTexture, weight);
                    }
                    pixelLength += weight;
                }
            } else pixelLength = 16;

            TextureAtlasSprite textureStone = ClientUtils.getSprite(new ResourceLocation("blocks/stone"));
            Vector2f[] stoneUVs = new Vector2f[]{new Vector2f(textureStone.getInterpolatedU(16.0F * wOff), textureStone.getInterpolatedV((double) (16.0F * dOff))), new Vector2f(textureStone.getInterpolatedU((double) (16.0F * wOff)), textureStone.getInterpolatedV((double) (16.0F * (dOff + depth)))), new Vector2f(textureStone.getInterpolatedU((double) (16.0F * (wOff + width))), textureStone.getInterpolatedV((double) (16.0F * (dOff + depth)))), new Vector2f(textureStone.getInterpolatedU((double) (16.0F * (wOff + width))), textureStone.getInterpolatedV((double) (16.0F * dOff)))};
            putNormalVertexDataSpr(
                    bakedQuads,
                    new Vector3f(0.0F, -1.0F, 0.0F),
                    new Vector3f[]{
                            new Vector3f(wOff, 0.0F, dOff),
                            new Vector3f(wOff + width, 0.0F, dOff),
                            new Vector3f(wOff + width, 0.0F, dOff + depth),
                            new Vector3f(wOff, 0.0F, dOff + depth)},
                    stoneUVs, textureStone);
            putNormalVertexDataSpr(
                    bakedQuads,
                    new Vector3f(0.0F, 1.0F, 0.0F),
                    new Vector3f[]{
                            new Vector3f(wOff, 1.0F, dOff),
                            new Vector3f(wOff, 1.0F, dOff + depth),
                            new Vector3f(wOff + width, 1.0F, dOff + depth),
                            new Vector3f(wOff + width, 1.0F, dOff)},
                    stoneUVs, textureStone);
            if (textureOre.isEmpty()) {
                Vector2f[][] uvs = new Vector2f[4][];

                for (int j = 0; j < 4; ++j) {
                    uvs[j] = new Vector2f[]{
                            new Vector2f(textureStone.getInterpolatedU(j * 4), textureStone.getInterpolatedV(0.0)),
                            new Vector2f(textureStone.getInterpolatedU(j * 4), textureStone.getInterpolatedV(16.0)),
                            new Vector2f(textureStone.getInterpolatedU((j + 1) * 4), textureStone.getInterpolatedV(16.0)),
                            new Vector2f(textureStone.getInterpolatedU((j + 1) * 4), textureStone.getInterpolatedV(0.0))};
                }

                putNormalVertexDataSpr(
                        bakedQuads,
                        new Vector3f(0.0F, 0.0F, -1.0F),
                        new Vector3f[]{
                                new Vector3f(wOff, 0.0F, dOff),
                                new Vector3f(wOff, 1.0F, dOff),
                                new Vector3f(wOff + width, 1.0F, dOff),
                                new Vector3f(wOff + width, 0.0F, dOff)},
                        uvs[0], textureStone);
                putNormalVertexDataSpr(
                        bakedQuads,
                        new Vector3f(0.0F, 0.0F, 1.0F),
                        new Vector3f[]{
                                new Vector3f(wOff + width, 0.0F, dOff + depth),
                                new Vector3f(wOff + width, 1.0F, dOff + depth),
                                new Vector3f(wOff, 1.0F, dOff + depth),
                                new Vector3f(wOff, 0.0F, dOff + depth)},
                        uvs[2], textureStone);
                putNormalVertexDataSpr(
                        bakedQuads,
                        new Vector3f(-1.0F, 0.0F, 0.0F),
                        new Vector3f[]{
                                new Vector3f(wOff, 0.0F, dOff + depth),
                                new Vector3f(wOff, 1.0F, dOff + depth),
                                new Vector3f(wOff, 1.0F, dOff),
                                new Vector3f(wOff, 0.0F, dOff)},
                        uvs[3], textureStone);
                putNormalVertexDataSpr(
                        bakedQuads,
                        new Vector3f(1.0F, 0.0F, 0.0F),
                        new Vector3f[]{
                                new Vector3f(wOff + width, 0.0F, dOff),
                                new Vector3f(wOff + width, 1.0F, dOff),
                                new Vector3f(wOff + width, 1.0F, dOff + depth),
                                new Vector3f(wOff + width, 0.0F, dOff + depth)},
                        uvs[1], textureStone);
            } else {
                float h = 0.0F;

                float h1;

                for (Object2IntMap.Entry<TextureAtlasSprite> entry : textureOre.object2IntEntrySet()) {
                    TextureAtlasSprite sprite = entry.getKey();
                    final int weight = entry.getIntValue();
                    int v = weight > 8 ? 16 - weight : 8;
                    if (sprite == null) {
                        TextureAtlasSprite fSprite = null;
                        int tint = -1;
                        if (contentDataSupplier.get() != null) {
                            fSprite = contentDataSupplier.get().sprite;
                            tint = contentDataSupplier.get().tint;
                        }

                        if (fSprite != null) {
                            Vector2f[][] uvs = new Vector2f[4][];

                            for (int j = 0; j < 4; ++j) {
                                uvs[j] = new Vector2f[]{
                                        new Vector2f(fSprite.getInterpolatedU(j * 4), fSprite.getInterpolatedV(v)),
                                        new Vector2f(fSprite.getInterpolatedU(j * 4), fSprite.getInterpolatedV(v + weight)),
                                        new Vector2f(fSprite.getInterpolatedU((j + 1) * 4), fSprite.getInterpolatedV(v + weight)),
                                        new Vector2f(fSprite.getInterpolatedU((j + 1) * 4), fSprite.getInterpolatedV(v))};
                            }

                            h1 = (float) weight / (float) pixelLength;
                            putTintedVertexDataSpr(
                                    bakedQuads,
                                    new Vector3f(0.0F, 0.0F, -1.0F),
                                    new Vector3f[]{new Vector3f(fWOff, h, fDOff),
                                            new Vector3f(fWOff, h + h1, fDOff),
                                            new Vector3f(fWOff + fWidth, h + h1, fDOff),
                                            new Vector3f(fWOff + fWidth, h, fDOff)},
                                    uvs[0], fSprite, tint);

                            putTintedVertexDataSpr(
                                    bakedQuads,
                                    new Vector3f(0.0F, 0.0F, 1.0F),
                                    new Vector3f[]{new Vector3f(fWOff + fWidth, h, fDOff + fDepth),
                                            new Vector3f(fWOff + fWidth, h + h1, fDOff + fDepth),
                                            new Vector3f(fWOff, h + h1, fDOff + fDepth),
                                            new Vector3f(fWOff, h, fDOff + fDepth)},
                                    uvs[2], fSprite, tint);
                            putTintedVertexDataSpr(
                                    bakedQuads,
                                    new Vector3f(-1.0F, 0.0F, 0.0F),
                                    new Vector3f[]{new Vector3f(fWOff, h, fDOff + fDepth),
                                            new Vector3f(fWOff, h + h1, fDOff + fDepth),
                                            new Vector3f(fWOff, h + h1, fDOff),
                                            new Vector3f(fWOff, h, fDOff)},
                                    uvs[3], fSprite, tint);
                            putTintedVertexDataSpr(
                                    bakedQuads,
                                    new Vector3f(1.0F, 0.0F, 0.0F),
                                    new Vector3f[]{new Vector3f(fWOff + fWidth, h, fDOff),
                                            new Vector3f(fWOff + fWidth, h + h1, fDOff),
                                            new Vector3f(fWOff + fWidth, h + h1, fDOff + fDepth),
                                            new Vector3f(fWOff + fWidth, h, fDOff + fDepth)},
                                    uvs[1], fSprite, tint);
                        }

                        IBlockState state = IPContent.blockDummy.getStateFromMeta(BlockTypes_Dummy.OIL_DEPOSIT.getMeta());
                        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(state);
                        sprite = model.getParticleTexture();
                    }

                    Vector2f[][] uvs = new Vector2f[4][];

                    for (int j = 0; j < 4; ++j) {
                        uvs[j] = new Vector2f[]{
                                new Vector2f(sprite.getInterpolatedU(j * 4), sprite.getInterpolatedV(v)),
                                new Vector2f(sprite.getInterpolatedU(j * 4), sprite.getInterpolatedV(v + weight)),
                                new Vector2f(sprite.getInterpolatedU((j + 1) * 4), sprite.getInterpolatedV(v + weight)),
                                new Vector2f(sprite.getInterpolatedU((j + 1) * 4), sprite.getInterpolatedV(v))};
                    }

                    h1 = (float) weight / (float) pixelLength;
                    putNormalVertexDataSpr(
                            bakedQuads,
                            new Vector3f(0.0F, 0.0F, -1.0F),
                            new Vector3f[]{
                                    new Vector3f(wOff, h, dOff),
                                    new Vector3f(wOff, h + h1, dOff),
                                    new Vector3f(wOff + width, h + h1, dOff),
                                    new Vector3f(wOff + width, h, dOff)},
                            uvs[0], sprite);
                    putNormalVertexDataSpr(
                            bakedQuads,
                            new Vector3f(0.0F, 0.0F, 1.0F),
                            new Vector3f[]{
                                    new Vector3f(wOff + width, h, dOff + depth),
                                    new Vector3f(wOff + width, h + h1, dOff + depth),
                                    new Vector3f(wOff, h + h1, dOff + depth),
                                    new Vector3f(wOff, h, dOff + depth)},
                            uvs[2], sprite);
                    putNormalVertexDataSpr(
                            bakedQuads,
                            new Vector3f(-1.0F, 0.0F, 0.0F),
                            new Vector3f[]{
                                    new Vector3f(wOff, h, dOff + depth),
                                    new Vector3f(wOff, h + h1, dOff + depth),
                                    new Vector3f(wOff, h + h1, dOff),
                                    new Vector3f(wOff, h, dOff)},
                            uvs[3], sprite);
                    putNormalVertexDataSpr(
                            bakedQuads,
                            new Vector3f(1.0F, 0.0F, 0.0F),
                            new Vector3f[]{
                                    new Vector3f(wOff + width, h, dOff),
                                    new Vector3f(wOff + width, h + h1, dOff),
                                    new Vector3f(wOff + width, h + h1, dOff + depth),
                                    new Vector3f(wOff + width, h, dOff + depth)},
                            uvs[1], sprite);

                    h += h1;
                }
            }
        } catch (Throwable e) {
            logger.warn(e.getLocalizedMessage());
            bakedQuads = Collections.emptyList();
        }

        return bakedQuads;
    }

    protected void putNormalVertexDataSpr(List<BakedQuad> bakedQuads ,
                                          Vector3f normal,
                                          Vector3f[] vertices,
                                          Vector2f[] uvs,
                                          TextureAtlasSprite sprite) {
        putTintedVertexDataSpr(bakedQuads, normal, vertices, uvs, sprite, -1);
    }

    protected void putTintedVertexDataSpr(List<BakedQuad> bakedQuads,
                                          Vector3f normal,
                                          Vector3f[] vertices,
                                          Vector2f[] uvs,
                                          TextureAtlasSprite sprite, int tint) {

        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(DefaultVertexFormats.ITEM);
        builder.setQuadOrientation(EnumFacing.getFacingFromVector(normal.x, normal.y, normal.z));
        builder.setTexture(sprite);
        builder.setQuadTint(tint);

        for (int i = 0; i < vertices.length; ++i) {
            builder.put(0, vertices[i].x, vertices[i].y, vertices[i].z, 1.0F);
            float d = LightUtil.diffuseLight(normal.x, normal.y, normal.z);
            builder.put(1, d, d, d, 1.0F);
            builder.put(2, uvs[i].x, uvs[i].y, 0.0F, 1.0F);
            builder.put(3, normal.x, normal.y, normal.z, 0.0F);
            builder.put(4);
        }

        bakedQuads.add(builder.build());
    }

    public ItemOverrideList getOverrides() {
        return this.overrideList;
    }

    public static class ContentData {
        public static final com.google.common.base.Supplier<ContentData> NONE = () -> null;

        public static com.google.common.base.Supplier<ContentData> gas(Gas gas) {
            return () -> new ContentData(gas.getSprite(), gas.getTint());
        }

        public static com.google.common.base.Supplier<ContentData> fluid(Fluid fluid) {
            return () -> new ContentData(Minecraft.getMinecraft()
                    .getTextureMapBlocks().getAtlasSprite(fluid.getStill().toString()),
                    fluid.getColor());
        }

        private final TextureAtlasSprite sprite;
        private final int tint;

        public ContentData(TextureAtlasSprite sprite, int tint) {
            this.sprite = sprite;
            this.tint = tint;
        }

        public ContentData(TextureAtlasSprite sprite) {
           this(sprite, -1);
        }

        public TextureAtlasSprite getSprite() {
            return sprite;
        }

        public int getTint() {
            return tint;
        }
    }
}
