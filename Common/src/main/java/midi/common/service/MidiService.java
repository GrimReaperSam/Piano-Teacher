package midi.common.service;

import java.util.List;

public interface MidiService {

    Iterable<Midi> getAll();

    Midi getMidi(Long midiId);

    Midi updateMidi(Midi updatedMidi);

    void addAll(Iterable<Midi> midis);

    List<Midi> getCurrentUserMidis();

    boolean existsByName(String midiName);
}
