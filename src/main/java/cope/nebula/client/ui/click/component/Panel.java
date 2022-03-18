package cope.nebula.client.ui.click.component;

import cope.nebula.client.feature.module.Module;
import cope.nebula.client.feature.module.ModuleCategory;
import cope.nebula.client.feature.module.render.ClickGUI;
import cope.nebula.client.ui.click.component.button.ModuleButton;
import cope.nebula.util.renderer.FontUtil;
import cope.nebula.util.renderer.RenderUtil;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.List;

/**
 * Represents a panel (or frame) that will hold all the components
 *
 * @author aesthetical
 * @since 3/12/22
 */
public class Panel extends Component {
    private boolean expanded = false;

    private boolean dragging = false;
    private double dragX, dragY;

    // opening animation
    private long time = System.currentTimeMillis();
    private boolean opening = false;
    private double heightProgress = 0.0;

    public Panel(double x, ModuleCategory category, List<Module> modules) {
        super(category.getDisplayName());

        setX(x);
        setY(26.0);

        setWidth(112.0);
        setHeight(18.0);

        // TODO
        modules.forEach((module) -> children.add(new ModuleButton(module)));
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        if (dragging) {
            setX(dragX + mouseX);
            setY(dragY + mouseY);
        }

        RenderUtil.drawHalfRoundedRectangle(getX(), getY(), getWidth(), getHeight(), 5.0, new Color(29, 29, 29).getRGB());
        FontUtil.drawString(getName(), (int) (getX() + 2.3f), (int) ((getY() + (getHeight() / 2.0)) - FontUtil.getHeight() / 2.0), -1);

        if (ClickGUI.animations.getValue()) {
            if (System.currentTimeMillis() - time >= 5L) {
                time = System.currentTimeMillis();
                double speed = ClickGUI.speed.getValue();

                if (opening) {
                    heightProgress += speed;
                } else {
                    heightProgress -= speed;
                }

                heightProgress = MathHelper.clamp(heightProgress, 0, 200.0);
            }
        } else {
            if (expanded) {
                heightProgress = 200.0;
            } else {
                heightProgress = 0.0;
            }
        }

        RenderUtil.scissor(getX(), getY() + getHeight(), getX() + getWidth(), getY() + heightProgress);
        RenderUtil.drawRectangle(getX(), getY() + getHeight(), getWidth(), heightProgress, new Color(28, 28, 28, 226).getRGB());

        double posY = getY() + getHeight();
        for (Component component : children) {
            component.setX(getX() + 2.0);
            component.setY(posY);
            component.setWidth(getWidth() - 4.0);
            component.setHeight(15.0);

            component.drawComponent(mouseX, mouseY);

            posY += component.getHeight() + 1.0;
        }

        RenderUtil.stopScissor();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseWithinBounds(mouseX, mouseY, getX(), getY(), getWidth(), getHeight())) {
            switch (button) {
                case 0: {
                    dragging = true;

                    dragX = getX() - mouseX;
                    dragY = getY() - mouseY;
                    break;
                }

                case 1: {
                    time = System.currentTimeMillis();

                    expanded = !expanded;
                    opening = expanded;
                    break;
                }
            }
        }

        if (expanded) {
            children.forEach((child) -> child.mouseClicked(mouseX, mouseY, button));
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0 && dragging) {
            dragging = false;
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }
}
