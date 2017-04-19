package no.ntnu.tdt4240.asteroids;

public interface ISettingsService {

    String MUSIC_VOLUME = "key_volume";
    String MUSIC_ENABLED = "key_music_enabled";
    String SOUND_ENABLED = "key_sound_enabled";
    String PLAYER_APPEARANCE = "key_player_appearance";
    String SOUND_VOLUME = "key_sound_volume";

    boolean getBoolean(String id);

    void setBoolean(String id, boolean value);

    String getString(String id, String defaultValue);

    void setString(String id, String value);

    int getInt(String id, int defaultValue);

    void setInt(String id, int value);


}
