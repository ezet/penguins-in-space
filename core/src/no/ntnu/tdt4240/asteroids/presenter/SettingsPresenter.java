package no.ntnu.tdt4240.asteroids.presenter;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.ISettingsService;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.audio.AudioService;
import no.ntnu.tdt4240.asteroids.view.IView;
import no.ntnu.tdt4240.asteroids.view.SettingsView;


public class SettingsPresenter extends BasePresenter {

    @SuppressWarnings("unused")
    private static final String TAG = MainMenuPresenter.class.getSimpleName();
    private static final int MAX_VOLUME = 100;
    private static final int VOLUME_STEP = 10;
    private static final int MIN_VOLUME = 0;
    private final Asteroids game;
    private final ISettingsView view;
    private final ISettingsService settingsService;
    private Screen parent;
    private int musicVolume;
    private AudioService audioService;

    SettingsPresenter(final Asteroids game, final Screen parent) {
        this.parent = parent;
        this.game = game;
        this.view = new SettingsView(game.getBatch(), new ViewHandler());
        this.audioService = ServiceLocator.getAppComponent().getAudioService();
        this.settingsService = ServiceLocator.appComponent.getSettingsService();
        musicVolume = settingsService.getInt(ISettingsService.MUSIC_VOLUME, 100);
    }

    @Override
    public IView getView() {
        return view;
    }

    @Override
    public void show() {
        super.show();
        String appearanceKey = settingsService.getString(ISettingsService.PLAYER_APPEARANCE, null);
        if (appearanceKey == null) {
            Array<String> characters = ServiceLocator.getAppComponent().getAssetService().getCharacters();
            appearanceKey = characters.get(0);
            settingsService.setString(ISettingsService.PLAYER_APPEARANCE, appearanceKey);
        }
        view.setCurrentCharacter(appearanceKey);
        boolean musicMuted = !settingsService.getBoolean(ISettingsService.MUSIC_ENABLED);
        view.setMusicMuted(musicMuted);
    }

    public interface ISettingsView extends IView {
        void setCurrentCharacter(String playerAppearance);

        void setMusicMuted(boolean muted);
    }

    public class ViewHandler {
        public void onBack() {
            game.setScreen(parent);
            dispose();
        }

        public void previousCharacter() {
            Array<String> characters = ServiceLocator.getAppComponent().getAssetService().getCharacters();
            String currentAppearance = settingsService.getString(ISettingsService.PLAYER_APPEARANCE, null);
            int index = characters.indexOf(currentAppearance, false);
            if (index <= 0) return;
            String newAppearance = characters.get(index - 1);
            settingsService.setString(ISettingsService.PLAYER_APPEARANCE, newAppearance);
            view.setCurrentCharacter(newAppearance);
        }

        public void nextCharacter() {
            Array<String> characters = ServiceLocator.getAppComponent().getAssetService().getCharacters();
            String currentAppearance = settingsService.getString(ISettingsService.PLAYER_APPEARANCE, null);
            int index = characters.indexOf(currentAppearance, false);
            if (index == characters.size - 1 || index == -1) return;
            String newAppearance = characters.get(index + 1);
            settingsService.setString(ISettingsService.PLAYER_APPEARANCE, newAppearance);
            view.setCurrentCharacter(newAppearance);
        }

        public void toggleMute(boolean muteToggled) {
            if (muteToggled) audioService.stopMusic();
            else audioService.startMusic();
            settingsService.setBoolean(ISettingsService.MUSIC_ENABLED, !muteToggled);
        }

        public void onMuteSoundClick(boolean muted) {
            if (muted) {
                audioService.setSoundVolume(0);
            }

        }

        public void increaseVolume() {
            if (musicVolume < MAX_VOLUME) {
                musicVolume += VOLUME_STEP;
                audioService.setMusicVolume(musicVolume);
                settingsService.setInt(ISettingsService.MUSIC_VOLUME, musicVolume);
            }
        }

        public void decreaseVolume() {
            if (musicVolume > MIN_VOLUME) {
                musicVolume -= VOLUME_STEP;
                audioService.setMusicVolume(musicVolume);
                settingsService.setInt(ISettingsService.MUSIC_VOLUME, musicVolume);
            }
        }
    }
}
