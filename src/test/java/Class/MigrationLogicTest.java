package Class;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MigrationLogicTest {

    // Simulação simplificada do isIdentical que está na MainApp
    private boolean simulateIsIdentical(Incident n, Incident s) {
        return java.util.Objects.equals(n.state, s.state) &&
                java.util.Objects.equals(ExcelUtils.canonical(n.priority), ExcelUtils.canonical(s.priority));
    }

    @Test
    void testComparisonLogic() {
        Incident source = new Incident();
        source.state = "Closed";
        source.priority = "2 - High";

        Incident snapshot = new Incident();
        snapshot.state = "Closed";
        snapshot.priority = "2 - High";

        // Devem ser idênticos
        assertTrue(simulateIsIdentical(source, snapshot));

        // Se mudar a prioridade, não deve ser idêntico
        source.priority = "1 - Critical";
        assertFalse(simulateIsIdentical(source, snapshot));
    }
}