package midi.server.service;

import midi.common.service.Midi;
import midi.common.service.MidiService;
import midi.server.service.repository.MidiRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class MidiServiceImpl implements MidiService {

    @Inject
    private MidiRepository midiRepository;

    @Override
    public Midi getMidi(Long midiId) {
        return midiRepository.findOne(midiId);
    }

    @Override
    public Midi updateMidi(Midi updatedMidi) {
        return midiRepository.save(updatedMidi);
    }
}
