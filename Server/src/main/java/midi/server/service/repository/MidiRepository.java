package midi.server.service.repository;

import midi.common.service.Midi;
import org.springframework.data.repository.CrudRepository;

public interface MidiRepository extends CrudRepository<Midi, Long> {

}
