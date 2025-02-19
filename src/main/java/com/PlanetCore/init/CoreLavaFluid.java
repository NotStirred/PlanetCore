package com.PlanetCore.init;


import java.util.HashSet;
import java.util.List;
import java.util.Random;

import com.PlanetCore.blocks.Corestone;

import com.google.gson.internal.bind.JsonTreeReader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemBlock;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;


public class CoreLavaFluid extends BlockFluidClassic {

	public CoreLavaFluid(String name, Fluid fluid, Material material) {
		super(fluid, material);
		setTranslationKey(name);
		setRegistryName(name);

		setLightLevel(1);
		setDensity(400);
		setTickRate(30);
		setTickRandomly(true);
		ModBlocks.BLOCKS.add(this);
	}


	public void vaporizeWater(World worldIn, BlockPos pos, IBlockState state)
	{
		if (this.material == Material.LAVA)
		{
			for (EnumFacing enumfacing : EnumFacing.values()) {

				if (worldIn.getBlockState(pos.offset(enumfacing)) == Blocks.WATER.getDefaultState()) {
					worldIn.setBlockState(pos.offset(enumfacing), Blocks.AIR.getDefaultState());
				}

			}
		}
	}


	public static void burnEntities(World world, int x, int y, int z, int radius) {

		float f = radius;
		HashSet hashset = new HashSet();
		int i;
		int j;
		int k;
		double d5;
		double d6;
		double d7;
		double wat = radius/** 2 */
				;
		boolean isOccupied = false;

		// bombStartStrength *= 2.0F;
		i = MathHelper.floor(x - wat - 1.0D);
		j = MathHelper.floor(x + wat + 1.0D);
		k = MathHelper.floor(y - wat - 1.0D);
		int i2 = MathHelper.floor(y + wat + 1.0D);
		int l = MathHelper.floor(z - wat - 1.0D);
		int j2 = MathHelper.floor(z + wat + 1.0D);
		AxisAlignedBB bb = new AxisAlignedBB(i,k,l,j,i2,j2);
		List list = world.getEntitiesWithinAABBExcludingEntity(null, bb);
		Vec3d vec3 = new Vec3d(x, y, z);

		for (int i1 = 0; i1 < list.size(); ++i1) {
			Entity entity = (Entity) list.get(i1);
			double d4 = entity.getDistance(x, y, z) / radius;

			if (d4 <= 1.0D) {
				d5 = entity.posX - x;
				d6 = entity.posY + entity.getEyeHeight() - y;
				d7 = entity.posZ - z;
				BlockPos pos1 = new BlockPos(x,y,z);
				BlockPos pos2 = new BlockPos(entity.posX,entity.posY + entity.getEyeHeight(),entity.posZ);
				double d9 = MathHelper.sqrt(d5 * d5 + d6 * d6 + d7 * d7);

				if (d9 < wat) {
					d5 /= d9;
					d6 /= d9;
					d7 /= d9;
					double d11 = (1.0D - d4);// * d10;
					if (!(entity instanceof EntityPlayerMP) || (entity instanceof EntityPlayerMP
							&& !((EntityPlayerMP) entity).isCreative())) {
						entity.attackEntityFrom(DamageSource.ON_FIRE, 6);
						entity.setFire(10);
					}
				}
			}
		}
		radius = (int) f;
	}



	public void thermalEffects(World worldIn, BlockPos pos, IBlockState state)
	{
		int X = pos.getX();
		int Y = pos.getY();
		int Z = pos.getZ();
		Random rand = new Random();

		for(int x = -2; x < 3; x++) {
			for(int y = -2; y < 3; y++) {
				for(int z = -2; z < 3; z++) {
					IBlockState state2 = worldIn.getBlockState(pos.add(x, y, z));
					Block block = state2.getBlock();
					if(state2.getMaterial()==Material.ROCK || state2.getMaterial()==Material.GROUND || state2.getMaterial()==Material.GRASS
							&& !(block instanceof Corestone) && block!=Blocks.BEDROCK /*&& block!=ModBlocks.COLD_CORESTONE*/)
					{
						worldIn.setBlockState(new BlockPos(x, y, z), Blocks.LAVA.getDefaultState());
					}

					if(state2.getMaterial()==Material.SNOW)
					{
						worldIn.setBlockToAir(new BlockPos(x, y, z));
					}
					if(state2.getMaterial()==Material.WATER || state2.getMaterial()==Material.ICE || state2.getMaterial()==Material.CRAFTED_SNOW)
					{
						worldIn.setBlockToAir(new BlockPos(x, y, z));
						//worldIn.setBlockState(pos, ModBlocks.CORESTONE.getDefaultState());
					}
					if(state2.getBlock().getFlammability(worldIn, pos, null)>0 || state2.getMaterial()==Material.WOOD || state2.getMaterial()==Material.CLOTH || state2.getMaterial()==Material.PLANTS || state2.getMaterial()==Material.LEAVES)
					{
						worldIn.setBlockState(new BlockPos(x, y, z), Blocks.FIRE.getDefaultState());
					}
				}
			}
		}
	}

	public void coreTemperature(World worldIn, BlockPos pos,IBlockState state)
	{

		int X = pos.getX();
		int Y = pos.getY();
		int Z = pos.getZ();
		burnEntities(worldIn,X,Y,Z, 9);
		Random rand = new Random();

		if(worldIn.canBlockSeeSky(pos) == true && worldIn.isRaining() == false)
		{
			if(rand.nextInt(120) == 0)
			{
				worldIn.setBlockState(pos, ModBlocks.CORESTONE.getDefaultState());
			}
		}
		else if(worldIn.canBlockSeeSky(pos) == true && worldIn.isRaining() == true)
		{
			if(rand.nextInt(20) == 0)
			{
				worldIn.setBlockState(pos, ModBlocks.CORESTONE.getDefaultState());
			}
		}
		else if(worldIn.canBlockSeeSky(pos) == false && pos.getY() > -1000)
			if(rand.nextInt(240) == 0)
			{
				worldIn.setBlockState(pos, ModBlocks.CORESTONE.getDefaultState());
			}
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(worldIn, pos, state);
		thermalEffects(worldIn, pos, state);
	}

	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if (entityIn instanceof EntityPlayerMP)
		{
			entityIn.attackEntityFrom(DamageSource.GENERIC, 8.0F);
		}
	}



	/*
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);
		thermalEffects(worldIn,pos,state);
		coreTemperature(worldIn,pos,state);
		vaporizeWater(worldIn, pos, state);
		float chance = 0.2F;
		if (pos.getY() <= -1000) {
			if ((worldIn.getBlockState(pos.up()) == Blocks.AIR.getDefaultState()
					|| worldIn.getBlockState(pos.up()) == Blocks.WATER.getDefaultState()) && Math.random() <= chance)
				worldIn.setBlockState(pos.up(), ModBlocks.CORE_LAVA_FLUID.getDefaultState());
			if ((worldIn.getBlockState(pos.down()) == Blocks.AIR.getDefaultState()
					|| worldIn.getBlockState(pos.up()) == Blocks.WATER.getDefaultState()) && Math.random() <= chance)
				worldIn.setBlockState(pos.down(), ModBlocks.CORE_LAVA_FLUID.getDefaultState());
			if ((worldIn.getBlockState(pos.north()) == Blocks.AIR.getDefaultState()
					|| worldIn.getBlockState(pos.up()) == Blocks.WATER.getDefaultState()) && Math.random() <= chance)
				worldIn.setBlockState(pos.north(), ModBlocks.CORE_LAVA_FLUID.getDefaultState());
			if ((worldIn.getBlockState(pos.south()) == Blocks.AIR.getDefaultState()
					|| worldIn.getBlockState(pos.up()) == Blocks.WATER.getDefaultState()) && Math.random() <= chance)
				worldIn.setBlockState(pos.south(), ModBlocks.CORE_LAVA_FLUID.getDefaultState());
			if ((worldIn.getBlockState(pos.west()) == Blocks.AIR.getDefaultState()
					|| worldIn.getBlockState(pos.up()) == Blocks.WATER.getDefaultState()) && Math.random() <= chance)
				worldIn.setBlockState(pos.west(), ModBlocks.CORE_LAVA_FLUID.getDefaultState());
			if ((worldIn.getBlockState(pos.east()) == Blocks.AIR.getDefaultState()
					|| worldIn.getBlockState(pos.up()) == Blocks.WATER.getDefaultState()) && Math.random() <= chance)
				worldIn.setBlockState(pos.east(), ModBlocks.CORE_LAVA_FLUID.getDefaultState());
		}
	}

	 */






	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

}
