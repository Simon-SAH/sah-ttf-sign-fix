package com.sah.ttfsignfix;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.lang.reflect.Method;

public final class SahTtfSignFixClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        if (!FabricLoader.getInstance().isModLoaded("iris")) {
            return;
        }

        try {
            Class<?> renderPipelines =
                    Class.forName("net.minecraft.client.renderer.RenderPipelines");

            Object sourcePipeline =
                    renderPipelines
                            .getField("TEXT_GRAYSCALE")
                            .get(null);

            Object targetPipeline =
                    renderPipelines
                            .getField("TEXT_GRAYSCALE_POLYGON_OFFSET")
                            .get(null);

            Class<?> irisPipelines =
                    Class.forName(
                            "net.irisshaders.iris.pipeline.IrisPipelines"
                    );

            Method copyPipeline = null;

            for (Method method : irisPipelines.getMethods()) {
                if (!method.getName().equals("copyPipeline")) {
                    continue;
                }

                if (method.getParameterCount() != 2) {
                    continue;
                }

                Class<?>[] parameters = method.getParameterTypes();

                if (parameters[0].isInstance(sourcePipeline)
                        && parameters[1].isInstance(targetPipeline)) {

                    copyPipeline = method;
                    break;
                }
            }

            if (copyPipeline == null) {
                throw new NoSuchMethodException(
                        "Compatible IrisPipelines.copyPipeline method not found"
                );
            }

            copyPipeline.invoke(
                    null,
                    sourcePipeline,
                    targetPipeline
            );

            System.out.println(
                    "[SAH TTF Sign Fix] Grayscale polygon-offset pipeline restored."
            );

        } catch (Throwable t) {
            System.err.println(
                    "[SAH TTF Sign Fix] Failed to restore Iris text pipeline."
            );
            t.printStackTrace();
        }
    }
}