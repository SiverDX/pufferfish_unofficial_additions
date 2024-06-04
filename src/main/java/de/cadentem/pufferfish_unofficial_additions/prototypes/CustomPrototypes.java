package de.cadentem.pufferfish_unofficial_additions.prototypes;

import de.cadentem.pufferfish_unofficial_additions.PUA;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.puffish.skillsmod.api.calculation.operation.OperationFactory;
import net.puffish.skillsmod.api.calculation.prototype.BuiltinPrototypes;
import net.puffish.skillsmod.api.calculation.prototype.Prototype;

public class CustomPrototypes {
    public static final Prototype<AbstractSpell> SPELL = Prototype.create(PUA.location("spell"));
    public static final Prototype<SchoolType> SCHOOL = Prototype.create(PUA.location("school"));
    public static final Prototype<String> STRING = Prototype.create(PUA.location("string"));

    static {
        SPELL.registerOperation(PUA.location("min_level"), BuiltinPrototypes.NUMBER, OperationFactory.create(spell -> (double) spell.getMinLevel()));
        SPELL.registerOperation(PUA.location("max_level"), BuiltinPrototypes.NUMBER, OperationFactory.create(spell -> (double) spell.getMaxLevel()));
        SPELL.registerOperation(PUA.location("cast_type"), CustomPrototypes.STRING, OperationFactory.create(spell -> spell.getCastType().name()));
    }
}
