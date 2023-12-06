package de.cadentem.pufferfish_unofficial_additions.conditions;

import net.puffish.skillsmod.api.config.ConfigContext;
import net.puffish.skillsmod.api.experience.calculation.condition.Condition;
import net.puffish.skillsmod.api.experience.calculation.condition.ConditionFactory;
import net.puffish.skillsmod.api.json.JsonElementWrapper;
import net.puffish.skillsmod.api.json.JsonObjectWrapper;
import net.puffish.skillsmod.api.utils.Result;
import net.puffish.skillsmod.api.utils.failure.Failure;
import net.puffish.skillsmod.api.utils.failure.ManyFailures;

import java.util.ArrayList;
import java.util.Optional;

public final class StringCondition implements Condition<String> {
    private final String string;

    private StringCondition(final String string) {
        this.string = string;
    }

    public static ConditionFactory<String> factory() {
        return ConditionFactory.withData(StringCondition::parse);
    }

    public static Result<StringCondition, Failure> parse(final JsonElementWrapper rootElement, final ConfigContext context) {
        return rootElement.getAsObject().andThen(StringCondition::parse);
    }

    public static Result<StringCondition, Failure> parse(final JsonObjectWrapper rootObject) {
        ArrayList<Failure> failures = new ArrayList<>();
        Optional<String> optValue = rootObject.getString("value").ifFailure(failures::add).getSuccess();
        return failures.isEmpty() ? Result.success(new StringCondition(optValue.orElseThrow())) : Result.failure(ManyFailures.ofList(failures));
    }

    public boolean test(final String string) {
        return this.string.equals(string);
    }
}
