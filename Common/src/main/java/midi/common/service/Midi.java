package midi.common.service;

import javax.persistence.*;

@Entity
public class Midi {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @Lob
    private String data;

    public Midi() {}

    public Midi(Long id, String name, String data) {
        this.id = id;
        this.name = name;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }
}
