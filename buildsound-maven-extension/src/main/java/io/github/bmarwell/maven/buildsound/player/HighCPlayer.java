package io.github.bmarwell.maven.buildsound.player;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;
import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.execution.ExecutionEvent.Type;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecution;

public class HighCPlayer implements BuildSoundPlayer {

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
    try {
      Sequencer sequencer = MidiSystem.getSequencer();
      Transmitter trans = sequencer.getTransmitter();
      Synthesizer synth = MidiSystem.getSynthesizer();
      Receiver rcvr = synth.getReceiver();
      final int PPQS = 16;
      Sequence seq = new Sequence(Sequence.PPQ, PPQS);
      Track track = seq.createTrack();
      long currentTick = 0;
      final int STAKKATO = 0;

      ShortMessage msg = new ShortMessage();
      msg.setMessage(ShortMessage.NOTE_ON, 0, DATA[0][0], 64);
      track.add(new MidiEvent(msg, currentTick));
      currentTick += (long) PPQS * DATA[0][1] - STAKKATO;

      msg = new ShortMessage();
      msg.setMessage(ShortMessage.NOTE_OFF, 0, DATA[0][0], 0);
      track.add(new MidiEvent(msg, currentTick));
      currentTick += STAKKATO;

      sequencer.open();
      synth.open();
      trans.setReceiver(rcvr);

      sequencer.setSequence(seq);
      sequencer.setTempoInBPM(60);
      sequencer.start();
      while (true) {
        try {
          Thread.sleep(100);
        } catch (Exception e) {
          // nothing
        }
        if (!sequencer.isRunning()) {
          break;
        }
      }
      sequencer.stop();
      sequencer.close();
      synth.close();
    } catch (MidiUnavailableException javaxSoundMidiMidiUnavailableException) {
      // TODO: implement
      throw new UnsupportedOperationException("not yet implemented: [${CLASS_NAME}::${METHOD_NAME}].");
    } catch (InvalidMidiDataException javaxSoundMidiInvalidMidiDataException) {
      // TODO: implement
      throw new UnsupportedOperationException("not yet implemented: [${CLASS_NAME}::${METHOD_NAME}].");
    }
  }
}
