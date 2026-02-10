package Class;

import java.util.Map;

public class IncidentMapper {

    // Map from ServiceNow (Source)
    public Incident mapFromSource(Map<String, Object> row) {
        Incident inc = new Incident();
        inc.number = ExcelUtils.canonical(row.get(MasterData.COL_NUMBER));
        inc.applicationSpecificInfo = ExcelUtils.canonical(row.get(MasterData.COL_APP_INFO));
        inc.opened = row.get(MasterData.COL_OPENED);
        inc.openedBy = ExcelUtils.canonical(row.get(MasterData.COL_OPENED_BY));
        inc.updated = row.get(MasterData.COL_UPDATED);
        inc.priority = ExcelUtils.canonical(row.get(MasterData.COL_INCIDENT_PRIORITY));
        inc.shortDescription = ExcelUtils.canonical(row.get(MasterData.COL_SHORT_DESC));
        inc.description = ExcelUtils.canonical(row.get(MasterData.COL_INCIDENT_DESCRIPTION));

        String rawState = ExcelUtils.canonical(row.get(MasterData.COL_STATE));
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
        inc.state = ExcelUtils.canonical(row.get(MasterData.COL_STATUS));
       // inc.updated = row.get(MasterData.COL_CONTROL_DATE); // In the status file, Control Date is often the last check
        inc.priority = ExcelUtils.canonical(row.get(MasterData.COL_STATUS_PRIORITY));
        return inc;
    }
}