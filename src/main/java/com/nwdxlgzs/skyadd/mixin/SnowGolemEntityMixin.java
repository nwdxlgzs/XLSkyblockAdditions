package com.nwdxlgzs.skyadd.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowGolemEntity.class)
public class SnowGolemEntityMixin extends GolemEntity {//为啥public class extends SnowGolemEntity时出错啊
    private static final String ICE_SnowGolemEntity = "ice-factory";

    public SnowGolemEntityMixin(EntityType<? extends SnowGolemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tickMovement",
            at = @At("HEAD")
    )
    public void tickMovement(CallbackInfo ci) {
        World world = getWorld();
        if (world instanceof ServerWorld) {
            Text customName = getCustomName();
            if (customName != null && ICE_SnowGolemEntity.equals(customName.getString())) {
                BlockState ICE_Block = Blocks.ICE.getDefaultState();//冰
                BlockState PACKED_ICE_Block = Blocks.PACKED_ICE.getDefaultState();//浮冰
                BlockState BLUE_ICE_Block = Blocks.BLUE_ICE.getDefaultState();//蓝冰
                for (int i = 0; i < 4; ++i) {
                    int j = MathHelper.floor(getX() + (double) ((float) (i % 2 * 2 - 1) * 0.25F));
                    int k = MathHelper.floor(getY() - 1);
                    int l = MathHelper.floor(getZ() + (double) ((float) (i / 2 % 2 * 2 - 1) * 0.25F));
                    BlockPos blockPos = new BlockPos(j, k, l);
                    BlockState blockState = world.getBlockState(blockPos);
                    if (blockState.isOf(Blocks.WATER)) {
                        FluidState fluidState = blockState.getFluidState();
                        if (fluidState.isStill() && BLUE_ICE_Block.canPlaceAt(world, blockPos)) {
                            world.setBlockState(blockPos, BLUE_ICE_Block);
                            world.emitGameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Emitter.of(this, BLUE_ICE_Block));
                        } else if (fluidState.getLevel() >= 7 && PACKED_ICE_Block.canPlaceAt(world, blockPos)) {
                            world.setBlockState(blockPos, PACKED_ICE_Block);
                            world.emitGameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Emitter.of(this, PACKED_ICE_Block));
                        } else if (ICE_Block.canPlaceAt(world, blockPos)) {
                            world.setBlockState(blockPos, ICE_Block);
                            world.emitGameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Emitter.of(this, ICE_Block));
                        }
                    }
                }
            }
        }
    }
}
