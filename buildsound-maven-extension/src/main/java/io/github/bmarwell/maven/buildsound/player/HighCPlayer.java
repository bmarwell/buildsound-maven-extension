package io.github.bmarwell.maven.buildsound.player;

import java.util.Arrays;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.execution.ExecutionEvent.Type;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecution;

public class HighCPlayer extends AbstractMidiSingleNotePlayer implements BuildSoundPlayer {

  // Partitur {{Tonhoehe, DauerInViertelNoten, AnzahlWdh},...}
  final int DATA[][] = {
      {80, 1, 1}
  };

  public HighCPlayer() {

  }

  @Override
  public void play(ExecutionEvent executionEvent) {
    if (executionEvent.getType() == Type.MojoSucceeded) {
      return;
    }

    final Optional<Plugin> plugin = Optional.ofNullable(executionEvent.getMojoExecution())
        .map(MojoExecution::getPlugin);
    System.out.println("Playing soundevent for executionEvent " + plugin.orElse(null) + " type = " + executionEvent.getType());

    CompletableFuture.runAsync(() -> playMidi(executionEvent));
  }

  private void playMidi(ExecutionEvent executionEvent) {
    midiNotePlay(DATA[0][0], DATA[0][1]);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", HighCPlayer.class.getSimpleName() + "[", "]")
        .add("super=" + super.toString())
        .add("DATA=" + Arrays.toString(DATA))
        .toString();
  }
}
