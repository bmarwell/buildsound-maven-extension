package io.github.bmarwell.maven.buildsound.player;

import org.apache.maven.execution.ExecutionEvent;

public interface BuildSoundPlayer {

  void play(ExecutionEvent event);
}
