package foundry.veil.example.editor;

import foundry.veil.api.client.editor.SingleWindowEditor;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.shader.definition.ShaderPreDefinitions;
import imgui.ImGui;
import imgui.type.ImBoolean;
import org.jetbrains.annotations.Nullable;

public class VeilExampleModEditor extends SingleWindowEditor {

    private static final int[] minTessLevel = new int[]{4};
    private static final int[] maxTessLevel = new int[]{64};
    private static final int[] minDistance = new int[]{1};
    private static final int[] maxDistance = new int[]{6};
    private static final ImBoolean useTessellation = new ImBoolean(true);
    private static final ImBoolean tessellationWireframe = new ImBoolean(false);

    @Override
    protected void renderComponents() {
        ShaderPreDefinitions definitions = VeilRenderSystem.renderer().getShaderDefinitions();

        if (ImGui.beginTabBar("Examples")) {
            for (Example value : Example.values()) {
                if (ImGui.beginTabItem(value.name + " Example")) {
                    if (value == Example.TESSELLATION) {
                        ImGui.checkbox("Use Tessellation", useTessellation);
                        ImGui.sameLine();
                        ImGui.checkbox("Wireframe", tessellationWireframe);
                        if (ImGui.dragInt("Min Tessellation Level", minTessLevel, 1, 1, Integer.MAX_VALUE)) {
                            definitions.define("MIN_TESS_LEVEL", String.valueOf(minTessLevel[0]));
                        }
                        if (ImGui.dragInt("Max Tessellation Level", maxTessLevel, 1, minTessLevel[0], Integer.MAX_VALUE)) {
                            definitions.define("MAX_TESS_LEVEL", String.valueOf(maxTessLevel[0]));
                        }
                        if (ImGui.dragInt("Min Distance", minDistance, 1, 0, Integer.MAX_VALUE)) {
                            definitions.define("MIN_DISTANCE", String.valueOf(minDistance[0]));
                        }
                        if (ImGui.dragInt("Max Distance", maxDistance, 1, minDistance[0], Integer.MAX_VALUE)) {
                            definitions.define("MAX_DISTANCE", String.valueOf(maxDistance[0]));
                        }
                    }
                }
                ImGui.endTabItem();

                if (ImGui.isItemHovered()) {
                    ImGui.beginTooltip();
                    ImGui.pushTextWrapPos(ImGui.getFontSize() * 35.0f);
                    ImGui.textUnformatted(value.tooltip);
                    ImGui.popTextWrapPos();
                    ImGui.endTooltip();
                }
            }

            ImGui.endTabBar();
        }
    }

    @Override
    public String getDisplayName() {
        return "Veil Example Mod";
    }

    public static boolean useTessellation() {
        return useTessellation.get();
    }

    public static boolean tessellationWireframe() {
        return tessellationWireframe.get();
    }

    public static int getMinTessLevel() {
        return minTessLevel[0];
    }

    public static int getMaxTessLevel() {
        return maxTessLevel[0];
    }

    public static int getMinDistance() {
        return minDistance[0];
    }

    public static int getMaxDistance() {
        return maxDistance[0];
    }

    private enum Example {
        TESSELLATION("Tessellation", "");

        private final String name;
        private final String tooltip;

        Example(String name, @Nullable String tooltip) {
            this.name = name;
            this.tooltip = tooltip;
        }
    }
}
