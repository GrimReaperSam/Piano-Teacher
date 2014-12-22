package midi.common.service;

import midi.common.security.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Midi implements Serializable {

    @Id
    @Column(name="midi_id")
    @GeneratedValue
    private Long midiId;
    @Column(unique = true, nullable = false)
    private String name;
    private Long length;
    private String composer;
    private String genre;
    private String album;
    private String year;
    @Enumerated(EnumType.ORDINAL)
    private Difficulty difficulty;

    @Lob private String data;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @ManyToMany(mappedBy="midis")
    private Set<User> users = new HashSet<>(0);

    public Midi(String name) {
        this.name = name;
    }

    protected Midi() {}

    protected Midi(Long midiId, String name, Long length, String data, String composer, String genre, String album, String year, Difficulty difficulty) {
        this.midiId = midiId;
        this.name = name;
        this.length = length;
        this.data = data;
        this.composer = composer;
        this.genre = genre;
        this.album = album;
        this.year = year;
        this.difficulty = difficulty;
    }

    public Long getMidiId() {
        return midiId;
    }

    public void setMidiId(Long id) {
        this.midiId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLength() {
        return length;
    }

    public String getData() {
        return data;
    }


    public String getComposer() {
        return composer;
    }

    public String getGenre() {
        return genre;
    }

    public String getAlbum() {
        return album;
    }

    public String getYear() {
        return year;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    @PrePersist
    protected void onCreate() {
        updated = created = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
