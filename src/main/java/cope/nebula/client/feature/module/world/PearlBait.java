package cope.nebula.client.feature.module.world;

import cope.nebula.client.events.PacketEvent;
import cope.nebula.client.events.PacketEvent.Direction;
import cope.nebula.client.feature.module.Module;
import cope.nebula.client.feature.module.ModuleCategory;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Comparator;

// TODO: delay movement packets?
public class PearlBait extends Module {
    public PearlBait() {
        super("PearlBait", ModuleCategory.WORLD, "Prevents pearl teleports");
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (event.getDirection().equals(Direction.INCOMING) && event.getPacket() instanceof SPacketSpawnObject) {
            SPacketSpawnObject packet = event.getPacket();
            if (packet.getType() == 65) {
                mc.world.playerEntities.stream()
                        .min(Comparator.comparingDouble((p) -> p.getDistance(packet.getX(), packet.getY(), packet.getZ())))
                        .ifPresent((player) -> {
                            if (player.equals(mc.player)) {
                                if (!mc.player.onGround) {
                                    return;
                                }

                                // do not allow movement
                                mc.player.motionX = 0.0;
                                mc.player.motionY = 0.0;
                                mc.player.motionZ = 0.0;

                                mc.player.movementInput.moveForward = 0.0f;
                                mc.player.movementInput.moveStrafe = 0.0f;

                                // send rubberband packet
                                mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + 1.0, mc.player.posZ, false));
                            }
                        });
            }
        }
    }
}
