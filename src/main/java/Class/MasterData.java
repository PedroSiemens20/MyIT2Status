package Class;

public class MasterData {
    // --- PLANILHAS ---
    public static final String STATUS_SHEET = "VIM";
    public static final String DEFAULT_INCIDENT_SHEET = "Page 1";

    // --- FORMATAÇÃO ---
    public static final String DATE_PATTERN_DISPLAY = "dd/MM/yyyy";
    public static final String DEFAULT_START_DATE = "01/12/2025";
    public static final String DEFAULT_END_DATE = "04/01/2026";
    public static final String OUTPUT_FOLDER = "excelFiles";
    public static final String DRAFT_FILENAME = "MyIT_Status.xlsx";



    // --- COLUNAS DO EXCEL Incident (IMAGE) ---
    public static final String COL_NUMBER = "Number";
    public static final String COL_APP_INFO = "Application-Specific Info";
    public static final String COL_OPENED = "Opened";
    public static final String COL_OPENED_BY = "Opened by";
    public static final String COL_UPDATED = "Updated";
    public static final String COL_INCIDENT_PRIORITY = "Priority";
    public static final String COL_STATE = "State";
    public static final String COL_SHORT_DESC = "Short description";
    public static final String COL_INCIDENT_DESCRIPTION = "Description";

    // --- COLUNAS VIM (Status) ---
    public static final String COL_CONTROL_DATE = "Control Date";
    public static final String COL_ID = "Incident";
    public static final String COL_TICKET_NR = "Ticket Nr.";
    public static final String COL_POLARION = "Polarion\nID";
    public static final String COL_CATEGORY = "Category";
    public static final String COL_OSS_NR = "OSS Nr";
    public static final String COL_REC_TECH = "Recurrent Issue\n(tech)";
    public static final String COL_REC_BUSINESS = "Recurrent Issue\n(Business)";
    public static final String COL_REPORTED_BY = "Reported By";
    public static final String COL_STATUS_DESCRIPTION = "Description";
    public static final String COL_DP_NR = "DP/nNr.";
    public static final String COL_ARE = "ARE";
    public static final String COL_CREATED = "Created";
    public static final String COL_STATUS_PRIORITY = "Priority";
    public static final String COL_STATUS = "Status";
    public static final String COL_WHO_IS_WORKING = "Who is working on it";
    public static final String COL_DUE_BY = "Due By";

    public static final String COL_COMMENTS = "Comments in TaskForce Meeting / Daily follow-ups";
    public static final String COL_SRE = "SRE";
    public static final String COL_BUZ = "Buzias";


    public static final String[] STATUS_HEADERS = {
            COL_CONTROL_DATE, COL_ID, COL_TICKET_NR, COL_POLARION, COL_CATEGORY,
            COL_OSS_NR, COL_REC_TECH, COL_REC_BUSINESS, COL_REPORTED_BY,
            COL_STATUS_DESCRIPTION, COL_DP_NR, COL_ARE, COL_CREATED,
            COL_STATUS_PRIORITY, COL_STATUS, COL_WHO_IS_WORKING,COL_DUE_BY, COL_COMMENTS,
            COL_SRE, COL_BUZ
    };

    public static final String INCIDENT_RESOLVED = "Resolved";
    public static final String INCIDENT_IN_PROGRESS = "In Progress";
    public static final String INCIDENT_PENDING = "Pending";

    public static final String STATUS_IN_PROCESS = "In Process";
    public static final String STATUS_IN_PROCESS_T_LINE = "In Process (Testing T-Line)";
    public static final String STATUS_IN_PROCESS_S_LINE = "In Process (Testing S-Line)";
    public static final String STATUS_FORWARDED = "Forwarded";

    public static final String STATUS_CLOSED = "Closed";


    //AREs Transformation
    public static final String ARE_BUZ = "BUZ - Buzias";
    public static final String ARE_SMO_DK = "SMO DK - Mobility Denmark";
    public static final String ARE_3368 = "3368 STSPL India";
    public static final String ARE_ORA = "ORA - Oradea";
    public static final String ARE_0000 = "0000 - Placeholder for all not listed ARE";
    public static final String ARE_SRE = "SRE Siemens Real Estate";
    public static final String ARE_SIB = "SIB - Sibiu";




    public static final String NEW_ARE_SMO_DK = "3012 - Siemens Mobility A/S";
    public static final String NEW_ARE_BUZ = "3014 - SIMEA BUZ";
    public static final String NEW_ARE_3368 = "3368 – STSPL India";
    public static final String NEW_ARE_SRE = "7998 - Siemens AG - SRE HQ";

}