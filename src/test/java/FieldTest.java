import sk.tuke.gamestudio.client.game.minesweeper.core.Field;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldTest {

    @Test
    public void generate() {
        System.out.println("test");
        final int testRows = 10, testCols = 10, testMines = 10;

        Field f = new Field(testRows, testCols, testMines);

        assertEquals(testRows, f.getRowCount(), "Row count should be the value from the constructor.");
    }
}
