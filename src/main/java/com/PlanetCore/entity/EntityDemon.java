package com.PlanetCore.entity;

/*import com.PlanetCore.entity.goals.DemonFireBallGoal;
import com.PlanetCore.util.handlers.LootTableHandler;


import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class EntityDemon extends EntityMob implements IAnimatable {
	public EntityDemon(World worldIn)   {
		super(worldIn);
		setSize(2f, 3f);
		this.isImmuneToFire = true;
	}

	private static final DataParameter<Integer> TEXTURE = EntityDataManager.<Integer>createKey(EntityDemon.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> ATTACK_TICK = EntityDataManager.<Integer>createKey(EntityDemon.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> FIREBALL_TICK = EntityDataManager.<Integer>createKey(EntityDemon.class, DataSerializers.VARINT);

	public int getTextureType(){
		return this.dataManager.get(TEXTURE);
	}

	@Override
	public void onAddedToWorld() {
		super.onAddedToWorld();
		if (this.getTextureType() == -1){
			this.dataManager.set(TEXTURE, rand.nextInt(2));
		}
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (this.getAttackTick() > 0){
			this.dataManager.set(ATTACK_TICK, this.getAttackTick() - 1);
		}
		if (this.getFireballTick() > 0){
			this.dataManager.set(FIREBALL_TICK, this.getFireballTick() - 1);
		}
	}

	protected void initEntityAI () {
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayerMP.class, false, false));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, true));
		this.tasks.addTask(3, new EntityAIAttackMelee(this, 0.6, true));
		this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayerMP.class, (float) 12));
		this.tasks.addTask(5, new EntityAIWander(this, 0.6));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.tasks.addTask(7, new EntityAISwimming(this));
		this.tasks.addTask(3, new DemonFireBallGoal(this));
	}
	
	@Override
	protected void entityInit()  {
		super.entityInit();
		this.dataManager.register(TEXTURE, -1);
		this.dataManager.register(FIREBALL_TICK, 0);
		this.dataManager.register(ATTACK_TICK, 0);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.dataManager.set(TEXTURE, compound.getInteger("texture"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("texture", this.getTextureType());
	}
	
	@Override
	protected ResourceLocation getLootTable() {
	return LootTableHandler.DEMON;
	}
	
	@Override
	public float getEyeHeight()
	{ return 3.0F; }
	
	protected Item getDropItem() {
		return null;
	}

	public net.minecraft.util.SoundEvent getAmbientSound() {
		return (net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.enderdragon.growl"));
	}

	public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
		return (net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.enderdragon.hurt"));
	}

	public net.minecraft.util.SoundEvent getDeathSound() {
		return (net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation(
				"entity.enderdragon_fireball.explode"));
	}

	@Override
	public boolean getCanSpawnHere() {
		if (posY <= -600) {
			return super.getCanSpawnHere();
		} else {
			return false;
		}
	}
	
	@Override
	public int getMaxSpawnedInChunk() {
		if (posY <= -800 && posY >= -1000) {
			return 2;
		}
		if (posY < -1000 && posY >= -1200) {
			return 3;
		}
		if (posY < -1200 && posY >= -1400) {
			return 5;
		}
		if (posY < -1400 && posY >= -1600) {
			return 7;
		}
		if (posY < -1600 && posY >= -1800) {
			return 9;
		}
		if (posY < -1800 && posY >= -2000) {
			return 12;
		}
		if (posY < -2000 && posY >= -2200) {
			return 15;
		}
		if (posY < -2200) {
			return 20;
		} else {
			return 1;
		}
	}
	
	
	
	protected float getSoundVolume() {
		return 0.6F;
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(120D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(5.0D);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(80.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(12D);
	}


	// GECKOLIB ANIMATIONS

	AnimationFactory factory = new AnimationFactory(this);
	@Override
	public void registerControllers(AnimationData animationData) {
		animationData.addAnimationController(new AnimationController(this, "movementController", 0, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		if (this.getFireballTick() > 0){
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.demon.fireball", true));
			return PlayState.CONTINUE;
		}

		if (this.getAttackTick() > 0){
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.demon.punch", true));
			return PlayState.CONTINUE;
		}

		if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.demon.walk", true));
			return PlayState.CONTINUE;
		}

		return PlayState.STOP;
	}

	public void startFireballAnim() {
		this.dataManager.set(FIREBALL_TICK, 14);
	}

	public int getFireballTick() {
		return this.dataManager.get(FIREBALL_TICK);
	}

	public int getAttackTick() {
		return this.dataManager.get(ATTACK_TICK);
	}
}

 */
