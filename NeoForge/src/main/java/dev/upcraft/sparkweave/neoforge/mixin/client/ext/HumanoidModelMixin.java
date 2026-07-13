package dev.upcraft.sparkweave.neoforge.mixin.client.ext;

import dev.upcraft.sparkweave.api.client.ext.HumanoidModelExt;
import net.minecraft.client.model.HumanoidModel;
import net.neoforged.neoforge.client.ClientHooks;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin implements HumanoidModelExt {

	@SuppressWarnings("DataFlowIssue")
	@Override
	public void sparkweave$copyPropertiesTo(HumanoidModel<?> other) {
		ClientHooks.copyModelProperties((HumanoidModel<?>)(Object) this, other);
	}
}
