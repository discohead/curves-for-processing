package crvs;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Sqnc {
    protected Sequence sequence;

    public long songLengthTicks;
    public ArrayList<Trk> trks = new ArrayList<Trk>();

    public Sqnc(long songLengthTicks) throws InvalidMidiDataException {
        this.songLengthTicks = songLengthTicks;
        this.sequence = new Sequence(Sequence.PPQ, 4);
    }

    public void createTrack(int channel, Crv pitch, Crv velocity, NR rhythm) {
       Trk trk = new Trk(this, channel, pitch, velocity, rhythm);
       this.trks.add(trk);
       System.out.println("Created track " + this.trks.size());
    }

    public void render() throws IOException, InvalidMidiDataException {
        for (Trk trk : this.trks) {
            System.out.println("Rendering track " + trk);
            trk.render();
        }
        MidiSystem.write(this.sequence, 1, new File("output.mid"));
    }

}
