package technology.roughness.whitenoise.config;

import java.util.Arrays;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterials;

import org.apache.commons.lang3.tuple.Pair;

public class WhiteNoiseTestConfig {

    public static final WhiteNoiseConfigSpec CLIENT_SPEC;
    public static final WhiteNoiseConfigSpec COMMON_SPEC;
    public static final WhiteNoiseConfigSpec SERVER_SPEC;
    public static final Test CLIENT_TEST;
    public static final Test COMMON_TEST;
    public static final Test SERVER_TEST;

    static {
        Pair<Test, WhiteNoiseConfigSpec> testSpecPair =
                new WhiteNoiseConfigSpec.Builder().configure(Test::new);
        CLIENT_SPEC = testSpecPair.getRight();
        CLIENT_TEST = testSpecPair.getLeft();

        testSpecPair =
                new WhiteNoiseConfigSpec.Builder().configure(Test::new);
        COMMON_SPEC = testSpecPair.getRight();
        COMMON_TEST = testSpecPair.getLeft();

        testSpecPair =
                new WhiteNoiseConfigSpec.Builder().configure(Test::new);
        SERVER_SPEC = testSpecPair.getRight();
        SERVER_TEST = testSpecPair.getLeft();
    }

    public static class Test {

        public final WhiteNoiseConfigSpec.IntValue intValue;
        public final WhiteNoiseConfigSpec.DoubleValue doubleValue;
        public final WhiteNoiseConfigSpec.LongValue longValue;
        public final WhiteNoiseConfigSpec.BooleanValue booleanValue;
        public final WhiteNoiseConfigSpec.BooleanValue booleanValue1;
        public final WhiteNoiseConfigSpec.BooleanValue booleanValue2;
        public final WhiteNoiseConfigSpec.EnumValue<ArmorMaterials> enumValue1;
        public final WhiteNoiseConfigSpec.ConfigValue<String> stringValue1;
        public final WhiteNoiseConfigSpec.ConfigValue<List<? extends String>> stringList1;
        public final WhiteNoiseConfigSpec.ConfigValue<List<? extends String>> validatedList1;
        public final WhiteNoiseConfigSpec.EnumValue<ArmorMaterials> enumValue;
        public final WhiteNoiseConfigSpec.ConfigValue<String> stringValue;
        public final WhiteNoiseConfigSpec.ConfigValue<List<? extends String>> stringList;
        public final WhiteNoiseConfigSpec.ConfigValue<List<? extends String>> validatedList;

        public Test(WhiteNoiseConfigSpec.Builder builder) {

            this.intValue = builder.comment("Integer Value Comment").translation("gui.intValue")
                    .defineInRange("intVal", 0, -10, 10);
            this.doubleValue = builder.comment("Double Value Comment").translation("gui.doubleValue")
                    .defineInRange("doubleVal", 0.0D, -10.0D, 10.0D);
            this.booleanValue = builder.comment("B\nNew Line").define("booleanValue", false);
            this.booleanValue1 = builder.comment("Boolean Value Comment").define("booleanValue1", false);
            this.booleanValue2 = builder.comment("Boolean Value Comment").define("booleanValue2", false);
            this.longValue =
                    builder.comment("Long Value Comment").defineInRange("longValue", 0L, -10L, 10L);
            this.stringValue1 =
                    builder.comment("String Value Comment").define("stringValue1", "String Value");
            this.enumValue1 =
                    builder.comment("Enum Value Comment").defineEnum("enumValue1", ArmorMaterials.CHAIN);
            this.stringList1 = builder.comment("String List Comment")
                    .defineList("stringList1", Arrays.asList("first", "second", "third"),
                            s -> s instanceof String);
            this.validatedList1 = builder.comment("Validated List Comment").defineList("listOfItems1",
                    Arrays.asList("minecraft:diamond", "minecraft:emerald", "minecraft:stone"),
                    s -> s instanceof String s1 && ResourceLocation.isValidResourceLocation(s1) && !s1.isBlank());

            builder.push("nested");
            this.stringValue =
                    builder.comment("String Value Comment").define("stringValue", "String Value");
            this.enumValue =
                    builder.comment("Enum Value Comment").defineEnum("enumValue", ArmorMaterials.CHAIN);
            builder.pop();

            builder.comment("Nested Comment").push("second nested");
            this.stringList = builder.comment("String List Comment")
                    .defineList("stringList", Arrays.asList("first", "second", "third"),
                            s -> s instanceof String);
            this.validatedList = builder.comment("Validated List Comment").defineList("listOfItems",
                    Arrays.asList("minecraft:diamond", "minecraft:emerald", "minecraft:stone"),
                    s -> s instanceof String s1 && ResourceLocation.isValidResourceLocation(s1));
            builder.pop();
        }

    }

}

