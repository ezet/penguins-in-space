package no.ntnu.tdt4240.asteroids;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.example.games.basegameutils.GameHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import no.ntnu.tdt4240.asteroids.model.PlayerData;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

import static com.google.android.gms.games.GamesActivityResultCodes.RESULT_INVALID_ROOM;
import static com.google.android.gms.games.GamesActivityResultCodes.RESULT_LEFT_ROOM;


public class PlayService implements INetworkService, RoomUpdateListener, RealTimeMessageReceivedListener, RoomStatusUpdateListener, OnInvitationReceivedListener {

    public static final int RC_ACHIEVEMENTS = 1;
    public static final int RC_SELECT_PLAYERS = 10000;
    public static final int RC_LEADERBOARD = 2;
    public static final int RC_INVITATION_INBOX = 10001;
    public final static int RC_WAITING_ROOM = 10002;
    private static final String TAG = "PlayService";
    private static final int MIN_PLAYERS = 1;
    private static final int MAX_PLAYERS = 1;
    private final AndroidLauncher activity;
    private final GameHelper gameHelper;
    private String incomingInvitationId;
    private IGameListener gameListener;
    private INetworkListener networkListener;
    private String roomId = null;

    public PlayService(AndroidLauncher activity) {
        this.activity = activity;
        gameHelper = new GameHelper(activity, GameHelper.CLIENT_GAMES);
        gameHelper.enableDebugLog(true);
        gameHelper.setShowErrorDialogs(true);
    }

    public void setup(GameHelper.GameHelperListener listener) {
        gameHelper.setup(listener);
    }

    public GameHelper getGameHelper() {
        return gameHelper;
    }

    @Override
    public void signIn() {
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameHelper.beginUserInitiatedSignIn();
                }
            });
        } catch (Exception e) {
            Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public String getDisplayName() {
        if (!getGameHelper().getApiClient().isConnected()) return null;
        return Games.Players.getCurrentPlayer(getGameHelper().getApiClient()).getDisplayName();
    }

    @Override
    public void signOut() {
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameHelper.signOut();
                }
            });
        } catch (Exception e) {
            Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void rateGame() {
        String str = "Your PlayStore Link";
        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
    }

    @Override
    public void unlockAchievement() {
        Games.Achievements.unlock(gameHelper.getApiClient(),
                activity.getString(R.string.achievement_dum_dum));
    }

    @Override
    public void submitScore(int highScore) {
        if (isSignedIn()) {
            Games.Leaderboards.submitScore(gameHelper.getApiClient(),
                    activity.getString(R.string.leaderboard_highest), highScore);
        }
    }

    @Override
    public void showAchievement() {
        if (isSignedIn()) {
            activity.startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), RC_ACHIEVEMENTS);
        } else {
            signIn();
        }
    }

    @Override
    public void showScore() {
        if (isSignedIn()) {
            activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
                    activity.getString(R.string.leaderboard_highest)), RC_LEADERBOARD);
        } else {
            signIn();
        }
    }

    @Override
    public void sendUnreliableMessageToOthers(byte[] messageData) {
        // TODO: 03-Apr-17 do this properly
        if (roomId == null) return;
        Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(gameHelper.getApiClient(), messageData, roomId);
    }

    @Override
    public void setMessageReceivedListener(IGameListener listener) {
        // TODO: create adapter between NetworkMessageReceivedListener and RealTimeMessageReceivedListener
    }

    @Override
    public boolean isSignedIn() {
        return gameHelper.isSignedIn();
    }


    @Override
    public void startQuickGame() {
        // quick-start a game with 1 randomly selected opponent
        final int MIN_OPPONENTS = 1, MAX_OPPONENTS = 1;
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_OPPONENTS,
                MAX_OPPONENTS, 0);
        RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
        rtmConfigBuilder.setMessageReceivedListener(this);
        rtmConfigBuilder.setRoomStatusUpdateListener(this);
        rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
//        switchToScreen(R.id.screen_wait);
//        keepScreenOn();
//        resetGameVars();
        Games.RealTimeMultiplayer.create(gameHelper.getApiClient(), rtmConfigBuilder.build());
    }

    @Override
    public void startSelectOpponents() {
        Intent intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(gameHelper.getApiClient(), MIN_PLAYERS, MAX_PLAYERS);
        activity.startActivityForResult(intent, RC_SELECT_PLAYERS);
    }

    @Override
    public void onRoomCreated(int statusCode, Room room) {
        Log.d(TAG, "onRoomCreated: ");
        roomId = room.getRoomId();
        showWaitingRoom(room);
    }

    @Override
    public void onJoinedRoom(int statusCode, Room room) {
        Log.d(TAG, "onJoinedRoom: ");
        showWaitingRoom(room);
    }

    @Override
    public void onLeftRoom(int statusCode, String s) {
        Log.d(TAG, "onLeftRoom: ");
    }

    @Override
    public void onRoomConnected(int statusCode, Room room) {
        Log.d(TAG, "onRoomConnected: statusCode: " + statusCode);
        Log.d(TAG, "onRoomConnected(" + statusCode + ", " + room + ")");
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
            activity.showGameError();
            return;
        }
    }

    @Override
    public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) {
        if (networkListener == null) {
            Gdx.app.debug(TAG, "onRealTimeMessageReceived: NetworkListener is null");
            return;
        }
        byte[] messageData = realTimeMessage.getMessageData();
        String senderParticipantId = realTimeMessage.getSenderParticipantId();
        int describeContents = realTimeMessage.describeContents();
        if (realTimeMessage.isReliable()) {
            networkListener.onReliableMessageReceived(senderParticipantId, describeContents, messageData);
        } else {
            networkListener.onUnreliableMessageReceived(senderParticipantId, describeContents, messageData);
        }
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_ACHIEVEMENTS:
                Log.d(TAG, "onActivityResult: RC_ACHIEVEMENTS");
                break;
            case RC_LEADERBOARD:
                Log.d(TAG, "onActivityResult: RC_LEADERBOARD");
                break;
            case RC_SELECT_PLAYERS:
                Log.d(TAG, "onActivityResult: RC_SELECT_PLAYERS");
                handleSelectPlayersResult(resultCode, data);
                break;
            case RC_INVITATION_INBOX:
                handleInvitationInboxResult(resultCode, data);
                break;
            case RC_WAITING_ROOM:
                handleWaitingRoomResult(resultCode, data);
                break;
            default:
                getGameHelper().onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleWaitingRoomResult(int resultCode, Intent intent) {
        Room room = intent.getParcelableExtra(Multiplayer.EXTRA_ROOM);
        Log.d(TAG, "handleWaitingRoomResult: ");
        switch (resultCode) {
            case Activity.RESULT_OK:
                Log.d(TAG, "handleWaitingRoomResult: OK");
                gameListener.onMultiplayerGameStarting();
                List<PlayerData> playerList = new ArrayList<>();
                String currentPlayerId = Games.Players.getCurrentPlayerId(gameHelper.getApiClient());
                for (Participant participant : room.getParticipants()) {
                    String playerId = participant.getPlayer().getPlayerId();
                    PlayerData playerData = new PlayerData(playerId, participant.getParticipantId(), participant.getDisplayName());
                    if (Objects.equals(currentPlayerId, playerId)) {
                        playerData.isSelf = true;
                    }
                    playerList.add(playerData);
                }
                networkListener.onRoomReady(playerList);
                break;
            case Activity.RESULT_CANCELED:
                Log.d(TAG, "handleWaitingRoomResult: CANCEL");
                // TODO: 02-Apr-17 leave room
                break;
            case RESULT_LEFT_ROOM:
                // TODO: 02-Apr-17 leave room
                Log.d(TAG, "handleWaitingRoomResult: LEFT");
                break;
            case RESULT_INVALID_ROOM:
                // TODO: 02-Apr-17 handle invalid room
                Log.d(TAG, "handleWaitingRoomResult: INVALID");
                break;
        }
    }

    private void handleInvitationInboxResult(int resultCode, Intent data) {
        Log.d(TAG, "handleInvitationInboxResult: ");
    }

    private void handleSelectPlayersResult(int response, Intent data) {
        Log.d(TAG, "handleSelectPlayersResult: ");
        if (response != Activity.RESULT_OK) {
            Log.w(TAG, "*** select players UI cancelled, " + response);
            return;
        }

        Log.d(TAG, "Select players UI succeeded.");

        // get the invitee list
        final ArrayList<String> invitees = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);
        Log.d(TAG, "Invitee count: " + invitees.size());

        // get the automatch criteria
        Bundle autoMatchCriteria = null;
        int minAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
        int maxAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);
        if (minAutoMatchPlayers > 0 || maxAutoMatchPlayers > 0) {
            autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
                    minAutoMatchPlayers, maxAutoMatchPlayers, 0);
            Log.d(TAG, "Automatch criteria: " + autoMatchCriteria);
        }

        // create the room
        Log.d(TAG, "Creating room...");
        RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
        rtmConfigBuilder.addPlayersToInvite(invitees);
        rtmConfigBuilder.setMessageReceivedListener(this);
        rtmConfigBuilder.setRoomStatusUpdateListener(this);
        if (autoMatchCriteria != null) {
            rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
        }
//        switchToScreen(R.id.screen_wait);
        activity.keepScreenOn();
//        resetGameVars();
        Games.RealTimeMultiplayer.create(getGameHelper().getApiClient(), rtmConfigBuilder.build());
        Log.d(TAG, "Room created, waiting for it to be ready...");
    }

    @Override
    public void onRoomConnecting(Room room) {
        Log.d(TAG, "onRoomConnecting: ");
    }

    @Override
    public void onRoomAutoMatching(Room room) {
        Log.d(TAG, "onRoomAutoMatching: ");
    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> list) {
        Log.d(TAG, "onPeerInvitedToRoom: ");
    }

    @Override
    public void onPeerDeclined(Room room, List<String> list) {
        Log.d(TAG, "onPeerDeclined: ");
    }

    @Override
    public void onPeerJoined(Room room, List<String> list) {
        Log.d(TAG, "onPeerJoined: ");

    }

    @Override
    public void onPeerLeft(Room room, List<String> list) {
        Log.d(TAG, "onPeerLeft: ");
    }

    @Override
    public void onConnectedToRoom(Room room) {
        Log.d(TAG, "onConnectedToRoom: ");
        if (roomId == null) roomId = room.getRoomId();
    }

    @Override
    public void onDisconnectedFromRoom(Room room) {
        Log.d(TAG, "onDisconnectedFromRoom: ");
        roomId = null;
    }

    @Override
    public void onPeersConnected(Room room, List<String> list) {
        Log.d(TAG, "onPeersConnected: ");
    }

    @Override
    public void onPeersDisconnected(Room room, List<String> list) {
        Log.d(TAG, "onPeersDisconnected: ");
    }

    @Override
    public void onP2PConnected(String s) {
        Log.d(TAG, "onP2PConnected: ");
    }

    @Override
    public void onP2PDisconnected(String s) {
        Log.d(TAG, "onP2PDisconnected: ");
    }

    @Override
    public void onInvitationReceived(Invitation invitation) {
        // We got an invitation to play a game! So, store it in
        // incomingInvitationId
        // and show the popup on the screen.
        Log.d(TAG, "onInvitationReceived: ");
        incomingInvitationId = invitation.getInvitationId();
//        ((TextView) findViewById(R.id.incoming_invitation_text)).setText(
//                invitation.getInviter().getDisplayName() + " " +
//                        getString(R.string.is_inviting_you));
//        switchToScreen(mCurScreen); // This will show the invitation popup

    }

    @Override
    public void onInvitationRemoved(String s) {
        Log.d(TAG, "onInvitationRemoved: ");
    }

    public void setGameListener(IGameListener gameListener) {
        this.gameListener = gameListener;
    }

    @Override
    public void setNetworkListener(INetworkListener networkListener) {
        this.networkListener = networkListener;
    }

    // Show the waiting room UI to track the progress of other players as they enter the
    // room and get connected.
    private void showWaitingRoom(Room room) {
        Log.d(TAG, "showWaitingRoom: " + room);
        if (room == null) {
            Log.w(TAG, "showWaitingRoom: room is null, returning");
            return;
        }
        // minimum number of players required for our game
        // For simplicity, we require everyone to join the game before we start it
        // (this is signaled by Integer.MAX_VALUE).
        final int MIN_PLAYERS = Integer.MAX_VALUE;
        Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(gameHelper.getApiClient(), room, MIN_PLAYERS);
        // show waiting room UI
        activity.startActivityForResult(i, RC_WAITING_ROOM);
    }

    public void acceptInviteToRoom(String invId) {
        Log.d(TAG, "Accepting invitation: " + invId);
        RoomConfig.Builder roomConfigBuilder = RoomConfig.builder(this);
        roomConfigBuilder.setInvitationIdToAccept(invId)
                .setMessageReceivedListener(this)
                .setRoomStatusUpdateListener(this);
        activity.keepScreenOn();
        Games.RealTimeMultiplayer.join(getGameHelper().getApiClient(), roomConfigBuilder.build());
    }

    // Leave the room.
    void leaveRoom() {
        Log.d(TAG, "Leaving room.");
//        mSecondsLeft = 0;
//        stopKeepingScreenOn();
//        if (mRoomId != null) {
//            Games.RealTimeMultiplayer.leave(mGoogleApiClient, this, mRoomId);
//            mRoomId = null;
//            switchToScreen(R.id.screen_wait);
//        } else {
//            switchToMainScreen();
//        }
    }


}
