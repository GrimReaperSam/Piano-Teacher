package midi.common.service;

public interface MidiService {

    Midi getMidi(Long midiId);

    Midi updateMidi(Midi updatedMidi);
}
