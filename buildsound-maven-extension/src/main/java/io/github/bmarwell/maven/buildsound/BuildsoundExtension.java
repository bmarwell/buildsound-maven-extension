package io.github.bmarwell.maven.buildsound;

import io.github.bmarwell.maven.buildsound.player.HighCPlayer;
import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.execution.ExecutionListener;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenSession;
import org.codehaus.plexus.component.annotations.Component;

@Component(role = AbstractMavenLifecycleParticipant.class, hint = "buildsound")
public class BuildsoundExtension extends AbstractMavenLifecycleParticipant {
  @Override
  public void afterProjectsRead(MavenSession session) throws MavenExecutionException {
    super.afterProjectsRead(session);

    if (isSkipped(session)) {
      return;
    }

    registerExecutionListener(session);
  }

  private boolean isSkipped(MavenSession session) {
    // command line property to disable
    return "true".equals(session.getUserProperties().get("buildsound.disable"));
  }

  private void registerExecutionListener(MavenSession session) {
    MavenExecutionRequest request = session.getRequest();
    final ExecutionListener originalExecListener = request.getExecutionListener();
    final ExecutionListener buildsoundExecListener = new BuildsoundExecutionListener(new HighCPlayer());

    final DecoratedExecutionListener decoratedExecutionListener =
        new DecoratedExecutionListener(buildsoundExecListener, originalExecListener);

    request.setExecutionListener(decoratedExecutionListener);
  }
}
