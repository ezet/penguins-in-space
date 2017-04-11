package no.ntnu.tdt4240.asteroids;

public interface ISettingsService {

    String MUSIC_VOLUME = "key_volume";
    String MUSIC_ENABLED = "key_music_enabled";
    String SOUND_ENABLED = "key_sound_enabled";
    String PLAYER_APPEARANCE = "key_player_appearance";

    boolean getBoolean(String id);

    void setBoolean(String id, boolean value);

    String getString(String id);

    void setString(String id, String value);

    int getInt(String id);

    void setInt(String id, int value);


}
