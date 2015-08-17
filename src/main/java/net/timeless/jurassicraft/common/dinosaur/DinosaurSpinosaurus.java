package net.timeless.jurassicraft.common.dinosaur;

import net.timeless.jurassicraft.common.entity.EntitySpinosaurus;
import net.timeless.jurassicraft.common.entity.base.EntityDinosaur;
import net.timeless.jurassicraft.common.entity.base.EnumGrowthStage;
import net.timeless.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurSpinosaurus extends Dinosaur
{
    private String[] maleTextures;
    private String[] femaleTextures;
    private String[] maleOverlayTextures;
    private String[] femaleOverlayTextures;

    public DinosaurSpinosaurus()
    {
        this.maleTextures = new String[] { getDinosaurTexture("male") };
        this.femaleTextures = new String[] { getDinosaurTexture("female") };

        this.maleOverlayTextures = new String[] { getDinosaurTexture("male_detail") };
        this.femaleOverlayTextures = new String[] { getDinosaurTexture("female_detail") };
    }

    // TODO: Figure out all the entities properties

    @Override
    public String getName(int geneticVariant)
    {
        return "Spinosaurus";
    }

    @Override
    public Class<? extends EntityDinosaur> getDinosaurClass()
    {
        return EntitySpinosaurus.class;
    }

    @Override
    public EnumTimePeriod getPeriod()
    {
        return EnumTimePeriod.CRETACEOUS;
    }

    @Override
    public int getEggPrimaryColor()
    {
        return 0x48403D;
    }

    @Override
    public int getEggSecondaryColor()
    {
        return 0xC5CFDA;
    }

    @Override
    public double getBabyHealth()
    {
        return 20;
    }

    @Override
    public double getAdultHealth()
    {
        return 100;
    }

    @Override
    public double getBabySpeed()
    {
        return 0.46;
    }

    @Override
    public double getAttackSpeed()
    {
        return 0.40;
    }

    @Override
    public double getAdultSpeed()
    {
        return 0.80;
    }

    @Override
    public double getBabyStrength()
    {
        return 6;
    }

    @Override
    public double getAdultStrength()
    {
        return 36;
    }

    @Override
    public double getBabyKnockback()
    {
        return 0.3;
    }

    @Override
    public double getAdultKnockback()
    {
        return 0.6;
    }

    @Override
    public int getMaximumAge()
    {
        return fromDays(55);
    }

    @Override
    public String[] getMaleTextures(int geneticVariant, EnumGrowthStage stage)
    {
        return maleTextures;
    }

    @Override
    public String[] getFemaleTextures(int geneticVariant, EnumGrowthStage stage)
    {
        return femaleTextures;
    }

    @Override
    public String[] getMaleOverlayTextures(int geneticVariant, EnumGrowthStage stage)
    {
        return maleOverlayTextures;
    }

    @Override
    public String[] getFemaleOverlayTextures(int geneticVariant, EnumGrowthStage stage)
    {
        return femaleOverlayTextures;
    }

    @Override
    public float getBabyEyeHeight()
    {
        return 0.6F;
    }

    @Override
    public float getAdultEyeHeight()
    {
        return 3.8F;
    }

    @Override
    public float getBabySizeX()
    {
        return 0.6F;
    }

    @Override
    public float getBabySizeY()
    {
        return 0.8F;
    }

    @Override
    public float getAdultSizeX()
    {
        return 3.0F;
    }

    @Override
    public float getAdultSizeY()
    {
        return 4.8F;
    }
}