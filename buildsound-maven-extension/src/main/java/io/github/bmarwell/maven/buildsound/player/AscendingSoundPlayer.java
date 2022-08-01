package io.github.bmarwell.maven.buildsound.player;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.execution.ExecutionEvent.Type;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

public class AscendingSoundPlayer extends AbstractMidiSingleNotePlayer implements BuildSoundPlayer {

  private Map<String, Integer> currentNoteByProject = new ConcurrentHashMap<>();

  public AscendingSoundPlayer(MavenSession session) {
    final List<MavenProject> allProjects = session.getAllProjects();
    allProjects.forEach(
        proj -> currentNoteByProject.put(proj.getId(), 60));
  }

  @Override
  public void play(ExecutionEvent event) {
    final String projectId = event.getProject().getId();
    switch (Type.MojoSucceeded) {
      case MojoSucceeded:
        final Integer noteToPlay = currentNoteByProject.merge(projectId, 1, Integer::sum);
        playNote(noteToPlay);
        break;
      default:
        break;
    }
  }

  private void playNote(Integer soundToPlay) {
    CompletableFuture.runAsync(() -> midiNotePlay(soundToPlay, 1));
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", AscendingSoundPlayer.class.getSimpleName() + "[", "]")
        .add("super=" + super.toString())
        .add("currentNoteByProject=" + currentNoteByProject.size())
        .toString();
  }
}
