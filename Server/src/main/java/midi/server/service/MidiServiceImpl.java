package midi.server.service;

import midi.common.service.Midi;
import midi.common.service.MidiService;
import midi.server.security.dao.UserRepository;
import midi.server.service.repository.MidiRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MidiServiceImpl implements MidiService {

    @Inject private MidiRepository midiRepository;
    @Inject private UserRepository userRepository;

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
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        Long userId = userRepository.findByUsername(username).getUserId();
        return midiRepository.findByUserId(userId);
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
    public void addAll(Midi... midis) {
        for (Midi midi : midis) {
            midiRepository.save(midi);
        }
    }
}
