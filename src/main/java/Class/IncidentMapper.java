package Class;

import java.util.Map;
import java.util.Objects;

public class IncidentMapper {

    // Map from ServiceNow (Source)
    public Incident mapFromSource(Map<String, Object> row) {
        Incident inc = new Incident();
        inc.number = ExcelUtils.canonical(row.get(MasterData.COL_NUMBER));
        inc.applicationSpecificInfo = aresNormalization(ExcelUtils.canonical(row.get(MasterData.COL_APP_INFO)));
        inc.opened = row.get(MasterData.COL_OPENED);
        inc.openedBy = removeParentheses(ExcelUtils.canonical(row.get(MasterData.COL_OPENED_BY)));
        inc.updated = row.get(MasterData.COL_UPDATED);
        inc.priority = ExcelUtils.canonical(row.get(MasterData.COL_INCIDENT_PRIORITY));
        inc.shortDescription = ExcelUtils.canonical(row.get(MasterData.COL_SHORT_DESC));
        inc.description = ExcelUtils.canonical(row.get(MasterData.COL_INCIDENT_DESCRIPTION));
        String rawState = ExcelUtils.canonical(row.get(MasterData.COL_STATE));

        System.out.println(inc.openedBy);

        //Rules

        if (rawState.equals(MasterData.INCIDENT_RESOLVED)) {
            inc.state = MasterData.STATUS_CLOSED;
        } else if (rawState.equals(MasterData.INCIDENT_PENDING) || rawState.equals(MasterData.INCIDENT_IN_PROGRESS)) {
            inc.state = MasterData.STATUS_IN_PROCESS;
        } else {
            inc.state = rawState;
        }
        return inc;


    }

    // Map from your MyIT_Status Excel (Status) for comparison
    public Incident mapFromStatus(Map<String, Object> row) {
        Incident inc = new Incident();
        inc.number = ExcelUtils.canonical(row.get(MasterData.COL_ID));
        String rawState = ExcelUtils.canonical(row.get(MasterData.COL_STATUS));
        if (rawState.equals(MasterData.STATUS_IN_PROCESS_S_LINE) || rawState.equals(MasterData.STATUS_IN_PROCESS_T_LINE) || rawState.equals(MasterData.STATUS_FORWARDED)) {
            inc.state = MasterData.STATUS_IN_PROCESS;
        } else {
            inc.state = rawState;

        }

        // inc.updated = row.get(MasterData.COL_CONTROL_DATE); // In the status file, Control Date is often the last check
        inc.priority = ExcelUtils.canonical(row.get(MasterData.COL_STATUS_PRIORITY));
        return inc;
    }


    public String aresNormalization(String oldAre) {

        switch (oldAre) {

            case MasterData.ARE_BUZ, MasterData.ARE_ORA, MasterData.ARE_SIB:
                return MasterData.NEW_ARE_BUZ;

            case MasterData.ARE_SMO_DK:
                return MasterData.NEW_ARE_SMO_DK;

            // 3368
            case MasterData.ARE_3368:
                return MasterData.NEW_ARE_3368;

            case MasterData.ARE_SRE:
                return MasterData.NEW_ARE_SRE;

            default:
                return "XXFLAGXX - " + oldAre;

        }
    }
        public String removeParentheses (String openedBy){
            if (openedBy == null || openedBy.isEmpty()) {
                return "";
            }
            // O regex "\\s*\\(.*?\\)" significa:
            // \\s*    -> Encontra zero ou mais espaços antes do parênteses
            // \\(     -> Encontra o caractere literal '('
            // .*?     -> Encontra qualquer caractere dentro (de forma não ambiciosa)
            // \\)     -> Encontra o caractere literal ')'
            return openedBy.replaceAll("\\s*\\(.*?\\)", "").trim();
        }


}
