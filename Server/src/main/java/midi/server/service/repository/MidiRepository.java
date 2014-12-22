package midi.server.service.repository;

import midi.common.service.Midi;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MidiRepository extends CrudRepository<Midi, Long> {

    @Query("select distinct m from Midi m join m.users u where u.userId = :userId")
    List<Midi> findByUserId(@Param("userId") Long userId);

}
