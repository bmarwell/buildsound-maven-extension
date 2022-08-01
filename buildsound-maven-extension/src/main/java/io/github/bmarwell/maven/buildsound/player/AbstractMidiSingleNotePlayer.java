package io.github.bmarwell.maven.buildsound.player;

import java.util.Random;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
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

public abstract class AbstractMidiSingleNotePlayer {

  private final int PPQS = 16;
  private final int STAKKATO = 4;

  Random random = new Random();

  public AbstractMidiSingleNotePlayer() {

  }

  protected void midiNotePlay(int note, int length) {
    final int channel = random.nextInt(9);
    try {
      Sequencer sequencer = MidiSystem.getSequencer();
      Transmitter trans = sequencer.getTransmitter();
      Synthesizer synth = MidiSystem.getSynthesizer();
      Receiver rcvr = synth.getReceiver();

      sequencer.open();
      synth.open();
      Sequence seq = new Sequence(Sequence.PPQ, PPQS);
      Track track = seq.createTrack();
      long currentTick = 0;

      ShortMessage msg = new ShortMessage();
      msg.setMessage(ShortMessage.NOTE_ON, channel, note, 64);
      track.add(new MidiEvent(msg, currentTick));
      currentTick += (long) PPQS * length - STAKKATO;

      msg = new ShortMessage();
      msg.setMessage(ShortMessage.NOTE_OFF, channel, note, 0);
      track.add(new MidiEvent(msg, currentTick));

      trans.setReceiver(rcvr);

      sequencer.setSequence(seq);
      sequencer.setTempoInBPM(144);
      sequencer.setLoopCount(0);
      sequencer.start();

      try {
        do {
          TimeUnit.MILLISECONDS.sleep(100L);
        } while (sequencer.isRunning());
        TimeUnit.MILLISECONDS.sleep(20L);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }

      sequencer.stop();
      sequencer.close();
      synth.close();
    } catch (MidiUnavailableException | InvalidMidiDataException invalidMidiDataException) {
      throw new IllegalStateException(invalidMidiDataException);
    }
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", AbstractMidiSingleNotePlayer.class.getSimpleName() + "[", "]")
        .toString();
  }

}
