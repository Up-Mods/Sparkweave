package dev.upcraft.sparkweave.fabric.mixin.client.ext;

import dev.upcraft.sparkweave.api.client.ext.HumanoidModelExt;
import dev.upcraft.sparkweave.fabric.impl.client.models.FabricModelUtils;
import net.minecraft.client.model.HumanoidModel;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin implements HumanoidModelExt {

	@SuppressWarnings("DataFlowIssue")
	@Override
	public void sparkweave$copyPropertiesTo(HumanoidModel<?> other) {
		FabricModelUtils.copyModelProperties((HumanoidModel<?>)(Object) this, other);
	}
}
