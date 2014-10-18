package midi.common.service;

public class MidiBuilder {
    private String name;
    private Long length;
    private String data;
    private Long id;
    private String composer;
    private String genre;
    private String album;
    private String year;
    private Integer difficulty;

    public static MidiBuilder newInstance() {
        return new MidiBuilder();
    }

    private MidiBuilder() {}

    public MidiBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public MidiBuilder setLength(Long length) {
        this.length = length;
        return this;
    }

    public MidiBuilder setData(String data) {
        this.data = data;
        return this;
    }

    public MidiBuilder setComposer(String composer) {
        this.composer = composer;
        return this;
    }

    public MidiBuilder setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public MidiBuilder setAlbum(String album) {
        this.album = album;
        return this;
    }

    public MidiBuilder setYear(String year) {
        this.year = year;
        return this;
    }

    public MidiBuilder setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public Midi createMidi() {
        return new Midi(null, name, length, data, composer, genre, album, year, difficulty);
    }
}