package midi.server.service;

import midi.common.security.SecurityService;
import midi.common.service.Midi;
import midi.common.service.MidiService;
import midi.server.service.repository.MidiRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MidiServiceImpl implements MidiService {

    @Inject private MidiRepository midiRepository;
    @Inject private SecurityService securityService;

    @Override
    public Iterable<Midi> getAll() {
        return midiRepository.findAll();
    }

    @Override
    public Midi getMidi(Long midiId) {
        return midiRepository.findOne(midiId);
    }

    @Override
    public List<Midi> getCurrentUserMidis() {
        return midiRepository.findByUserId(securityService.getCurrentUser().getUserId());
    }

    @Override
    public boolean existsByName(String midiName) {
        return midiRepository.findByName(midiName) != null;
    }

    @Override
    @Transactional(readOnly = false)
    @Secured("ROLE_ADMIN")
    public Midi updateMidi(Midi updatedMidi) {
        return midiRepository.save(updatedMidi);
    }

    @Override
    @Transactional(readOnly = false)
    @Secured("ROLE_ADMIN")
    public void addAll(Iterable<Midi> midis) {
        midiRepository.save(midis);
    }
}
