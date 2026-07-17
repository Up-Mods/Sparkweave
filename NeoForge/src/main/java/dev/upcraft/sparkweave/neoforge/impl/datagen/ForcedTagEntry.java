package dev.upcraft.sparkweave.neoforge.impl.datagen;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagEntry;

import java.util.function.Predicate;

public class ForcedTagEntry extends TagEntry {

	public ForcedTagEntry(Identifier id) {
		super(id, true, true);
	}

	@Override
	public boolean verifyIfPresent(Predicate<Identifier> elementCheck, Predicate<Identifier> tagCheck) {
		return true;
	}
}
