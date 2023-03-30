package sk.tuke.gamestudio.client.game.tiktaktoe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TiktaktoeWebField {
    private final Map<Integer, StateTile> tikTakToeField;
    private final int TILES_COUNT = 9;
    public TiktaktoeWebField() {
        tikTakToeField = new HashMap<>();
        for (int i = 0; i < TILES_COUNT; i++) {
            tikTakToeField.put(i, StateTile.EMPTY);
        }
    }

    public Map<Integer, StateTile> getTikTakToeField() {
        return tikTakToeField;
    }

    public List<StateTile> getData() {
        return tikTakToeField.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public int getIndexEmptyTile() {
        return tikTakToeField.entrySet().stream()
                .filter(e -> e.getValue() == StateTile.EMPTY)
                .map(Map.Entry::getKey).findFirst().orElse(-1);
    }

    public StateTile checkWin() {
        List<List<Integer>> winPossibilities = List.of(
                List.of(0, 1, 2),
                List.of(3, 4, 5),
                List.of(6, 7, 8),
                List.of(0, 3, 6),
                List.of(1, 4, 7),
                List.of(2, 5, 8),
                List.of(0, 4, 8),
                List.of(2, 4, 6)
        );

        for (List<Integer> win : winPossibilities) {
            if (tikTakToeField.get(win.get(0)) == tikTakToeField.get(win.get(1)) &&
                    tikTakToeField.get(win.get(0)) == tikTakToeField.get(win.get(2))) {
                return tikTakToeField.get(win.get(0));
            }
        }
        return StateTile.EMPTY;
    }


}

