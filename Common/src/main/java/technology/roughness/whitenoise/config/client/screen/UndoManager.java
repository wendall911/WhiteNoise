package technology.roughness.whitenoise.config.client.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;

/**
 * A class representing an undo/redo buffer.<p>
 * <p>
 * Every undo step is represented as 2 actions, one to initially execute when the step is added and
 * to redo after an undo, and one to execute to undo the step. Both get a captured parameter to make
 * defining them inline or reusing the code portion easier.
 */
public final class UndoManager {

    public record Step<T>(Consumer<T> run, T newValue, Consumer<T> undo, T oldValue) {

        private void runUndo() {
            undo.accept(oldValue);
        }

        private void runRedo() {
            run.accept(newValue);
        }

    }

    ;

    private final List<Step<?>> undos = new ArrayList<>();
    private final List<Step<?>> redos = new ArrayList<>();

    public void undo() {
        if (canUndo()) {
            Step<?> step = undos.removeLast();
            step.runUndo();
            redos.add(step);
        }
    }

    public void redo() {
        if (canRedo()) {
            Step<?> step = redos.removeLast();
            step.runRedo();
            undos.add(step);
        }
    }

    private void add(Step<?> step, boolean execute) {
        undos.add(step);
        redos.clear();
        if (execute) {
            step.runRedo();
        }
    }

    public <T> Step<T> step(Consumer<T> run, T newValue, Consumer<T> undo, T oldValue) {
        return new Step<>(run, newValue, undo, oldValue);
    }

    public <T> void add(Consumer<T> run, T newValue, Consumer<T> undo, T oldValue) {
        add(step(run, newValue, undo, oldValue), true);
    }

    public <T> void addNoExecute(Consumer<T> run, T newValue, Consumer<T> undo, T oldValue) {
        add(step(run, newValue, undo, oldValue), false);
    }

    public void add(Step<?>... steps) {
        add(ImmutableList.copyOf(steps));
    }

    public void add(final List<Step<?>> steps) {
        add(new Step<>(
                        n -> steps.forEach(Step::runRedo),
                        null,
                        n -> steps.forEach(Step::runUndo),
                        null
                ),
                true
        );
    }

    public boolean canUndo() {
        return !undos.isEmpty();
    }

    public boolean canRedo() {
        return !redos.isEmpty();
    }

}
