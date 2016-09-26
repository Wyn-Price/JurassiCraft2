package org.jurassicraft.server.block.fence;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jurassicraft.server.block.entity.ElectricFencePoleBlockEntity;
import org.jurassicraft.server.block.entity.ElectricFenceWireBlockEntity;
import org.jurassicraft.server.tab.TabHandler;

import java.util.HashSet;

public class ElectricFencePoleBlock extends BlockContainer {
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.3425, 0.0, 0.3425, 0.6575, 1.0, 0.6575);

    public ElectricFencePoleBlock() {
        super(Material.IRON);
        this.setHardness(3.0F);
        this.setCreativeTab(TabHandler.BLOCKS);
        this.setSoundType(SoundType.METAL);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDS;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
        return this.getBoundingBox(state, world, pos);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new ElectricFencePoleBlockEntity();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NORTH, SOUTH, WEST, EAST, POWERED);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess blockAccess, BlockPos pos) {
        BlockPos northPos = pos.offset(EnumFacing.NORTH);
        BlockPos southPos = pos.offset(EnumFacing.SOUTH);
        BlockPos eastPos = pos.offset(EnumFacing.EAST);
        BlockPos westPos = pos.offset(EnumFacing.WEST);
        IBlockState northBlock = blockAccess.getBlockState(northPos);
        IBlockState southBlock = blockAccess.getBlockState(southPos);
        IBlockState westBlock = blockAccess.getBlockState(westPos);
        IBlockState eastBlock = blockAccess.getBlockState(eastPos);
        boolean north = this.canConnect(blockAccess, northPos, EnumFacing.NORTH, northBlock);
        boolean south = this.canConnect(blockAccess, southPos, EnumFacing.SOUTH, southBlock);
        boolean west = this.canConnect(blockAccess, westPos, EnumFacing.WEST, westBlock);
        boolean east = this.canConnect(blockAccess, eastPos, EnumFacing.EAST, eastBlock);
        boolean powered = false;
        if (blockAccess instanceof World) {
            World world = (World) blockAccess;
            BlockPos downPos = pos.down();
            IBlockState down = blockAccess.getBlockState(downPos);
            if ((world.isBlockPowered(downPos) && down.getBlock() instanceof ElectricFenceBaseBlock) || (down.getBlock() instanceof ElectricFencePoleBlock && down.getActualState(blockAccess, downPos).getValue(POWERED))) {
                powered = true;
            }
        }
        return state.withProperty(NORTH, north).withProperty(SOUTH, south).withProperty(WEST, west).withProperty(EAST, east).withProperty(POWERED, powered);
    }

    protected boolean canConnect(IBlockAccess world, BlockPos pos, EnumFacing direction, IBlockState state) {
        if (state.getBlock() instanceof ElectricFenceWireBlock || state.getBlock() instanceof ElectricFencePoleBlock) {
            return true;
        } else {
            IBlockState down = world.getBlockState(pos.down());
            if (down.getBlock() instanceof ElectricFenceWireBlock && down.getActualState(world, pos).getValue(ElectricFenceWireBlock.UP_DIRECTION).getOpposite() == direction) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block) {
        if (!world.isRemote) {
            this.updateConnectedWires(world, pos);
        }
    }

    public void updateConnectedWires(World world, BlockPos pos) {
        boolean powered = world.isBlockPowered(pos.down());
        HashSet<BlockPos> wires = new HashSet<>();
        HashSet<BlockPos> bases = new HashSet<>();
        this.processConnectedWires(bases, wires, world, pos.down(), pos.down());
        for (BlockPos wirePos : wires) {
            TileEntity tile = world.getTileEntity(wirePos);
            if (tile instanceof ElectricFenceWireBlockEntity) {
                ElectricFenceWireBlockEntity wire = (ElectricFenceWireBlockEntity) tile;
                wire.power(pos, powered);
            }
        }
    }

    protected void processConnectedWires(HashSet<BlockPos> bases, HashSet<BlockPos> wires, World world, BlockPos origin, BlockPos current) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (!((x == z || x == -z) && y != 0)) {
                        BlockPos offset = current.add(x, y, z);
                        if (!bases.contains(offset)) {
                            bases.add(offset);
                            IBlockState state = world.getBlockState(offset);
                            Block block = state.getBlock();
                            if (block instanceof ElectricFenceBaseBlock) {
                                int deltaX = offset.getX() - origin.getX();
                                int deltaZ = offset.getZ() - origin.getZ();
                                double delta = MathHelper.sqrt_double((deltaX * deltaX) + (deltaZ * deltaZ));
                                if (delta <= 8) {
                                    BlockPos wirePos = new BlockPos(offset);
                                    while (world.getBlockState(wirePos = wirePos.up()).getBlock() instanceof ElectricFenceWireBlock) {
                                        if (!wires.contains(wirePos)) {
                                            wires.add(wirePos);
                                        }
                                    }
                                    this.processConnectedWires(bases, wires, world, origin, offset);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
