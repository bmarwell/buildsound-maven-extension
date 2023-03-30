package io.github.bmarwell.maven.buildsound.player;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

public class SineWaveSoundPlayer implements BuildSoundPlayer, AutoCloseable {

  private static final int SAMPLE_RATE = 16 * 1024;
  private static final AudioFormat AF = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);

  private final Map<String, Integer> currentNoteByProject = new ConcurrentHashMap<>();

  private final MavenSession session;
  private final SourceDataLine line;

  public SineWaveSoundPlayer(MavenSession session) {
    this.session = session;
    final List<MavenProject> allProjects = session.getAllProjects();
    allProjects.forEach(
        proj -> currentNoteByProject.put(proj.getId(), 140));

    try {
      line = AudioSystem.getSourceDataLine(AF);
      line.open(AF, SAMPLE_RATE);
    } catch (LineUnavailableException javaxSoundSampledLineUnavailableException) {
      throw new UnsupportedOperationException("Cannot play sound.");
    }

  }

  @Override
  public void play(ExecutionEvent event) throws InterruptedException {
    String projectId = event.getProject().getId();
    switch (event.getType()) {
      // case MojoStarted:
      // fall-through
      case MojoSucceeded:
        final int noteToPlay = currentNoteByProject.merge(projectId, 20, Integer::sum);
        CompletableFuture.runAsync(() -> playSound(noteToPlay));
        TimeUnit.MILLISECONDS.sleep(20L);
        break;
      default:
        break;
    }

  }

  private void playSound(int freq) {
    line.start();

    byte[] toneBuffer = createSinWaveBuffer(freq, 50);
    int count = line.write(toneBuffer, 0, toneBuffer.length);

    line.drain();
  }

  public static byte[] createSinWaveBuffer(double freq, int ms) {
    int samples = (ms * SAMPLE_RATE) / 1000;
    byte[] output = new byte[samples];
    double period = (double) SAMPLE_RATE / freq;

    for (int i = 0; i < output.length; i++) {
      double angle = 2.0 * Math.PI * i / period;
      output[i] = (byte) (Math.sin(angle) * 127f);
    }

    return output;
  }

  @Override
  public void close() {
    line.close();
  }
}
