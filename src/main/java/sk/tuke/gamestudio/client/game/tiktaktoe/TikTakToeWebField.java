package sk.tuke.gamestudio.client.game.tiktaktoe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TikTakToeWebField {
    private final Map<Integer, TileState> tikTakToeField;
    private final int TILES_COUNT = 9;
    public TikTakToeWebField() {
        tikTakToeField = new HashMap<>();
        for (int i = 0; i < TILES_COUNT; i++) {
            tikTakToeField.put(i, TileState.EMPTY);
        }
    }

    public Map<Integer, TileState> getTikTakToeField() {
        return tikTakToeField;
    }

    public List<TileState> getData() {
        return tikTakToeField.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public int getIndexEmptyTile() {
        return tikTakToeField.entrySet().stream()
                .filter(e -> e.getValue() == TileState.EMPTY)
                .map(Map.Entry::getKey).findFirst().orElse(-1);
    }

    public TileState checkWin() {
        List<List<Integer>> winPossibilities = List.of(
                List.of(0, 1, 2),
                List.of(3, 4, 5),
                List.of(6, 7, 8),
                List.of(0, 3, 6),
                List.of(1, 4, 7),
                List.of(2, 5, 8),
                List.of(0, 4, 8),
                List.of(2, 4, 6)
        ); /*zaujimave riesenie, zjednodusila si si tym algoritmizaciu,
        nevyhodou vsak je, ze extenzivne plnis heap,
        kedze pri kazdom checku vznikaju nove docasne listy,
        ktore sa hned po checku zahodia.
        Ak by si teda mala vacsie pole a klikala by si velmi rychlo,
        garbage collector by to nestihal zahadzovat,
         a teda mohlo by ti to sposobit heap overflow.

         Az na tuto malu vec je ale cela trieda velmi pekny kratky kod, paci sa mi to :)
        */

        for (List<Integer> win : winPossibilities) {
            if (tikTakToeField.get(win.get(0)) == tikTakToeField.get(win.get(1)) &&
                    tikTakToeField.get(win.get(0)) == tikTakToeField.get(win.get(2))) {
                return tikTakToeField.get(win.get(0));
            }
        }
        return TileState.EMPTY;
    }
}

