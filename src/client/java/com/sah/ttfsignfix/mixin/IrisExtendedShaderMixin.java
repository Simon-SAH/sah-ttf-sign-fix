package com.sah.ttfsignfix.mixin;

import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL33C;
import org.lwjgl.opengl.GL46C;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;

@Mixin(
        targets = "net.irisshaders.iris.pipeline.programs.ExtendedShader",
        remap = false
)
public abstract class IrisExtendedShaderMixin {

    @Shadow
    @Final
    private boolean intensitySwizzle;

    @Inject(
            method = "iris$setupState",
            at = @At("TAIL"),
            remap = false
    )
    private void sah$restoreTtfIntensitySwizzle(CallbackInfo ci) {
        if (!this.intensitySwizzle) {
            return;
        }

        try {
            int textureId = sah$getCurrentAlbedoTexture();

            if (textureId == 0) {
                return;
            }

            GL46C.glTextureParameteri(
                    textureId,
                    GL33C.GL_TEXTURE_SWIZZLE_R,
                    GL11C.GL_RED
            );

            GL46C.glTextureParameteri(
                    textureId,
                    GL33C.GL_TEXTURE_SWIZZLE_G,
                    GL11C.GL_RED
            );

            GL46C.glTextureParameteri(
                    textureId,
                    GL33C.GL_TEXTURE_SWIZZLE_B,
                    GL11C.GL_RED
            );

            GL46C.glTextureParameteri(
                    textureId,
                    GL33C.GL_TEXTURE_SWIZZLE_A,
                    GL11C.GL_RED
            );

        } catch (Throwable t) {
            System.err.println(
                    "[SAH TTF Sign Fix] Failed to restore TTF intensity swizzle."
            );
            t.printStackTrace();
        }
    }

    private static int sah$getCurrentAlbedoTexture() throws Exception {

        Class<?> irisClass =
                Class.forName("net.irisshaders.iris.Iris");

        Method getPipelineManager =
                irisClass.getMethod("getPipelineManager");

        Object manager =
                getPipelineManager.invoke(null);

        Method getPipelineNullable =
                manager.getClass().getMethod("getPipelineNullable");

        Object pipeline =
                getPipelineNullable.invoke(manager);

        if (pipeline == null) {
            return 0;
        }

        Method getAlbedoTex =
                pipeline.getClass().getMethod("getAlbedoTex");

        return (Integer) getAlbedoTex.invoke(pipeline);
    }
}