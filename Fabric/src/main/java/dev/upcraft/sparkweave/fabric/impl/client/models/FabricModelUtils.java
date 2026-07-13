package dev.upcraft.sparkweave.fabric.impl.client.models;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;

public class FabricModelUtils {

	public static void copyModelProperties(HumanoidModel<?> original, HumanoidModel<?> replacement) {
		copyModelPartProperties(original.head, replacement.head);
		copyModelPartProperties(original.hat, replacement.hat);
		copyModelPartProperties(original.body, replacement.body);
		copyModelPartProperties(original.rightArm, replacement.rightArm);
		copyModelPartProperties(original.leftArm, replacement.leftArm);
		copyModelPartProperties(original.rightLeg, replacement.rightLeg);
		copyModelPartProperties(original.leftLeg, replacement.leftLeg);
	}

	private static void copyModelPartProperties(ModelPart original, ModelPart replacement) {
		replacement.visible = original.visible;
		replacement.x = original.x;
		replacement.y = original.y;
		replacement.z = original.z;
		replacement.xRot = original.xRot;
		replacement.yRot = original.yRot;
		replacement.zRot = original.zRot;
		replacement.xScale = original.xScale;
		replacement.yScale = original.yScale;
		replacement.zScale = original.zScale;
	}
}
