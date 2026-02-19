package Class;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class IncidentMapperTest {

    private IncidentMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new IncidentMapper();
    }

    @Test
    void testAresNormalization() {
        // Testando mapeamentos conhecidos (Baseado no seu switch case)
        assertEquals(MasterData.NEW_ARE_BUZ, mapper.aresNormalization(MasterData.ARE_BUZ));
        assertEquals(MasterData.NEW_ARE_BUZ, mapper.aresNormalization(MasterData.ARE_ORA));
        assertEquals(MasterData.NEW_ARE_SRE, mapper.aresNormalization(MasterData.ARE_SRE));

        // Testando caso default (flag)
        String desconhecido = "AREA_QUALQUER";
        assertTrue(mapper.aresNormalization(desconhecido).contains("XXFLAGXX"));
    }

    @Test
    void testMapFromSource_StatusConversion() {
        Map<String, Object> row = new HashMap<>();
        row.put(MasterData.COL_NUMBER, "INC123");
        row.put(MasterData.COL_STATE, MasterData.INCIDENT_RESOLVED);
        row.put(MasterData.COL_APP_INFO, MasterData.ARE_SRE);

        Incident result = mapper.mapFromSource(row);

        // Verifica se RESOLVED virou CLOSED conforme a regra no IncidentMapper
        assertEquals(MasterData.STATUS_CLOSED, result.state);
        assertEquals("INC123", result.number);
    }

    @Test
    void testMapFromStatus_StatusNormalization() {
        Map<String, Object> row = new HashMap<>();
        row.put(MasterData.COL_ID, "INC456");
        row.put(MasterData.COL_STATUS, MasterData.STATUS_IN_PROCESS_S_LINE);

        Incident result = mapper.mapFromStatus(row);

        // Verifica se o status de linha (S_LINE) foi normalizado para IN_PROCESS
        assertEquals(MasterData.STATUS_IN_PROCESS, result.state);
    }


    @Test
    void testRemoveParentheses() {
        IncidentMapper mapper = new IncidentMapper();

        // Caso padrão
        assertEquals("Kostovski, Simon",
                mapper.removeParentheses("Kostovski, Simon (GBS CEE FPS CZ AS FSD 3)"));

        // Caso sem parênteses
        assertEquals("John Doe",
                mapper.removeParentheses("John Doe"));

        // Caso com parênteses colado no nome improvavel mas fica aqui
        assertEquals("John Doe",
                mapper.removeParentheses("John Doe(GBS CEE FPS CZ AS FSD 3)"));

        // Caso vazio ou nulo
        assertEquals("", mapper.removeParentheses(""));
        assertEquals("", mapper.removeParentheses(null));
    }
}