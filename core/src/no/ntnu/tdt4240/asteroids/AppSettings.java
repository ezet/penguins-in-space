package no.ntnu.tdt4240.asteroids;

class AppSettings implements IAppSettings {

    private static String playerAppearance = "playerBlack.png";

    @Override
    public String getPlayerAppearance() {
        return playerAppearance;
    }

    @Override
    public void setPlayerAppearance(String playerAppearance) {
        AppSettings.playerAppearance = playerAppearance;
    }
}
