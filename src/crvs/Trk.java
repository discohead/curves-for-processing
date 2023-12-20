package crvs;

import processing.core.PVector;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Trk {
    protected Sqnc sqnc;
    protected Track track;
    public int channel;
    public Crv pitch;
    public Crv velocity;
    public NR rhythm;
    public Map<Integer, Crv> cntrls = new HashMap<Integer, Crv>();


    public Trk(Sqnc sqnc, int channel, Crv pitch, Crv velocity, NR rhythm) {
        this.sqnc = sqnc;
        this.track = sqnc.sequence.createTrack();
        this.channel = channel;
        this.pitch = pitch;
        this.velocity = velocity;
        this.rhythm = rhythm;
    }

    public void setCntrl(int controller, Crv crv) {
        cntrls.put(controller, crv);
    }

    public void render() throws InvalidMidiDataException {
        for (int tick = 0; tick < this.sqnc.songLengthTicks; tick++) {
            float x = (float) (tick % 32) / 31;
            if (this.rhythm.next()) {
                float pitchValue = this.pitch.yAt(x);
                float velocityValue = this.velocity.yAt(x);
                PVector vector = new PVector(pitchValue, velocityValue);
                Mdvnt event = new Mdvnt(this, vector, ShortMessage.NOTE_ON, tick);
                System.out.println(event);
                event.render();
            }
            this.renderCntrlsAtTick(tick);
        }
    }

    public void renderCntrlsAtTick(int tick) throws InvalidMidiDataException {
        for (Map.Entry<Integer, Crv> entry : cntrls.entrySet()) {
            int controller = entry.getKey();
            Crv crv = entry.getValue();
            float value = crv.yAt((float) tick / this.sqnc.songLengthTicks - 1);
            PVector vector = new PVector(controller, value);
            Mdvnt event = new Mdvnt(this, vector, ShortMessage.CONTROL_CHANGE, tick);
            event.render();
        }
    }

}
