package cope.nebula.asm.mixins.render.shader;

import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ShaderGroup.class)
public interface IShaderGroup {
    @Accessor List<Shader> getListShaders();

    @Accessor List<Framebuffer> getListFramebuffers();
}
