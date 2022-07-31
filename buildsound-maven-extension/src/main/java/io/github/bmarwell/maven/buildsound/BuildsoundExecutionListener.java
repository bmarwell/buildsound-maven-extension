package io.github.bmarwell.maven.buildsound;

import io.github.bmarwell.maven.buildsound.player.BuildSoundPlayer;
import org.apache.maven.execution.AbstractExecutionListener;
import org.apache.maven.execution.ExecutionEvent;
import org.codehaus.plexus.component.annotations.Component;

@Component(role = AbstractExecutionListener.class)
public class BuildsoundExecutionListener extends AbstractExecutionListener {

  private final BuildSoundPlayer player;

  public BuildsoundExecutionListener(BuildSoundPlayer player) {
    this.player = player;
  }

  @Override
  public void projectStarted(ExecutionEvent event) {
    super.projectStarted(event);
    player.play(event);
  }

  @Override
  public void projectSucceeded(ExecutionEvent event) {
    super.projectSucceeded(event);
    player.play(event);
  }

  @Override
  public void mojoStarted(ExecutionEvent event) {
    super.mojoStarted(event);
    player.play(event);
  }

  @Override
  public void mojoSucceeded(ExecutionEvent event) {
    super.mojoSucceeded(event);
    player.play(event);
  }
}
