package midi.common.service;

public interface MidiService {

    Iterable<Midi> getAll();

    Midi getMidi(Long midiId);

    Midi updateMidi(Midi updatedMidi);

    void addAll(Midi... midis);
}
