package cope.nebula.client.feature.module.movement;

import cope.nebula.client.feature.module.Module;
import cope.nebula.client.feature.module.ModuleCategory;
import cope.nebula.client.value.Value;
import cope.nebula.util.renderer.FontUtil;

public class ReverseStep extends Module {
    public ReverseStep() {
        super("ReverseStep", ModuleCategory.MOVEMENT, "Steps down blocks faster");
    }

    public static final Value<Mode> mode = new Value<>("Mode", Mode.MOTION);
    public static final Value<Double> height = new Value<>("Height", 2.0, 1.0, 5.0);
    public static final Value<Double> speed = new Value<>("Speed", 1.0, 0.5, 3.0);

    @Override
    public String getDisplayInfo() {
        return FontUtil.formatText(mode.getValue().name());
    }

    @Override
    public void onTick() {
        if (mc.player.onGround && !mc.player.isInWater() && !mc.player.isInLava()) {
            for (double y = 0.0; y <= height.getValue() + 0.5; y += 0.1) {
                if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
                    if (mode.getValue().equals(Mode.SHIFT)) {
                        // TODO: very broken does not work

                        mc.player.motionX = 0.0;
                        mc.player.motionZ = 0.0;
                        mc.player.motionY = -speed.getValue();
                    } else {
                        mc.player.motionY -= speed.getValue();
                    }

                    break;
                }
            }
        }
    }

    public enum Mode {
        MOTION, SHIFT
    }
}
