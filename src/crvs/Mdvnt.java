package crvs;

import processing.core.PApplet;
import processing.core.PVector;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;

public class Mdvnt {
    protected Trk trk;

    public PVector vector;
    public int command;
    public long tick;

    public Mdvnt(Trk trk, PVector vector, int command, long tick) {
        this.trk = trk;
        this.vector = vector;
        this.command = command;
        this.tick = tick;
    }

    public void render() throws InvalidMidiDataException {
        ShortMessage message = new ShortMessage();
        try {
            message.setMessage(command, this.trk.channel, this.getData1(), this.getData2());
        } catch (Exception e) {
            e.printStackTrace();
        }
        MidiEvent event = new MidiEvent(message, this.tick);
        System.out.println(event);
        this.trk.track.add(event);
        if (this.command == ShortMessage.NOTE_ON) {
            ShortMessage noteOff = new ShortMessage();
            noteOff.setMessage(ShortMessage.NOTE_OFF, this.trk.channel, this.getData1(), 0);
            this.trk.track.add(new MidiEvent(noteOff, this.tick + this.trk.sqnc.sequence.getResolution()/4));
        }
    }

    public int getData1() {
        return (int) this.vector.x;
    }

    public int getData2() {
        return (int) this.vector.y;
    }

    public String toString() {
        return "Mdvnt(data1: " + this.vector.x + ", data2: " + this.vector.y + ", cmd: " + this.command + ", tick: " + this.tick + ")";
    }
}
