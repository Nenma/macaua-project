package sample;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mihai on 8/22/2017.
 */
public class PlayersState implements Serializable{
    private static final long serialVersionUID = 7526472295622776147L;
    private List<String> LobbyPlayers;
    private List<String> ReadyPlayer;

    public PlayersState(){
        LobbyPlayers = new ArrayList<>();
        ReadyPlayer = new ArrayList<>();
    }

    public  void removeFrom(String name, List<String> list){
        if(list.contains(name))
            list.remove(name);
    }

    public  void addTo(String name, List<String> list){
        list.add(name);
    }

    public  List<String> getLobbyPlayers() {
        return LobbyPlayers;
    }

    public  void setLobbyPlayers(List<String> lobbyPlayers) {
        LobbyPlayers = lobbyPlayers;
    }

    public  List<String> getReadyPlayer() {
        return ReadyPlayer;
    }

    public  void setReadyPlayer(List<String> readyPlayer) {
        ReadyPlayer = readyPlayer;
    }
}
