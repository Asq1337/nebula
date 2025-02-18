package cope.nebula.asm.mixins.input;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerControllerMP.class)
public interface IPlayerControllerMP {
    @Invoker("syncCurrentPlayItem") void hookSyncCurrentPlayItem();
}
