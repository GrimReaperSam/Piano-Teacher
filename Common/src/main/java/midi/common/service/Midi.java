package midi.common.service;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Midi implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    private Long length;
    private String composer;
    private String genre;
    private String album;
    private String year;
    private Integer difficulty;

    @Lob private String data;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    protected Midi() {}

    protected Midi(Long id, String name, Long length, String data, String composer, String genre, String album, String year, Integer difficulty) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.data = data;
        this.composer = composer;
        this.genre = genre;
        this.album = album;
        this.year = year;
        this.difficulty = difficulty;
    }

    public Long getId() {
        return id;
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

    public Integer getDifficulty() {
        return difficulty;
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

}