package scenewise;

import ch.epfl.modularity.clustering.hierachy.AverageLinkageStrategy;
import ch.epfl.modularity.clustering.hierachy.CompleteLinkageStrategy;
import ch.epfl.modularity.clustering.hierachy.LinkageStrategy;
import ch.epfl.modularity.clustering.hierachy.SingleLinkageStrategy;
import ch.epfl.modularity.matrix.HammingMetric;
import ch.epfl.modularity.matrix.IMetric;
import ch.epfl.modularity.matrix.Jaccard1Metric;
import ch.epfl.modularity.matrix.Jaccard2Metric;
import ch.epfl.modularity.matrix.ManhattanMetric;

public class Constant {

    public final static boolean DEBUG = false;
    public static boolean EXPERIMENT = false;
    public final static int TOPK = 5;
    public final static String FILE_SEPARATOR = "/";
    //FILE SYSTEM
    public final static String PDF_CRAWL_RUNTIME_FOLDER = "./runtimepdf";
    public final static int MAX_CRAWLING_THREAD = 8;
//	// =====================PAIR TYPE================//
//	public static final int PAIR_TYPE_SCHEMA = 1;
//	public static final int PAIR_TYPE_ATTRIBUTE = 2;
//	// =====================MOUSE TYPE===============//
//	public static final int MOUSE_BUTTON_LEFT = 1;
//	public static final int MOUSE_BUTTON_MIDLE = 2;
//	public static final int MOUSE_BUTTON_RIGHT = 3;
    // ===================GAP====================
    public static final int DISTANCE_TYPE_ORIGINAL = 0;
    public static final int DISTANCE_TYPE_AVERAGE_LINKAGE = 1;
    // public static final int DISTANCE_TYPE_CENTROD_LINKAGE = 2;
    public static final int DISTANCE_TYPE_COMPLETE_LINKAGE = 3;
    public static final int DISTANCE_TYPE_SINGLE_LINKAGE = 4;
    public static final LinkageStrategy LS_AVERAGE = new AverageLinkageStrategy();
    public static final LinkageStrategy LS_COMPLETE = new CompleteLinkageStrategy();
    public static final LinkageStrategy LS_SINGLE = new SingleLinkageStrategy();
    public static final int METRIC_TYPE_JACCARD_1 = 1;
    public static final int METRIC_TYPE_JACCARD_2 = 2;
    public static final int METRIC_TYPE_MANHATTAN = 3;
    public static final int METRIC_TYPE_HAMMING = 4;
    public static final IMetric METRIC_JACCARD1 = new Jaccard1Metric();
    public static final IMetric METRIC_JACCARD2 = new Jaccard2Metric();
    public static final IMetric METRIC_MANHATTAN = new ManhattanMetric();
    public static final IMetric METRIC_HAMMING = new HammingMetric();
    public static final int METRIC_TYPE_JACARD = 1;
    // ====================MAP=====================//
    public static final int ALIGN_TOP = 1;
    public static final int ALIGN_LEFT = 2;
    public static final int ALIGN_RIGHT = 4;
    public static final int ALIGN_BOTTOM = 8;
    public static final int ALIGN_CENTER_VERTICAL = 16;
    public static final int ALIGN_CENTER_HORIZONTAL = 32;
    public static final int MOUSE_MODE_NONE = 0;
    public static final int MOUSE_MODE_MOVE_VIEW = 1;
    public static final int MOUSE_MODE_CHOOSE_ZONE_ZOOM = 2;
    public static final int MOUSE_MODE_SELECTING_COL_ZONE = 4;
    public static final int MOUSE_MODE_SELECTING_ROW_ZONE = 8;
    public static final int MOUSE_MODE_SELETING_COL_ROW_ZONE = MOUSE_MODE_SELECTING_COL_ZONE
            | MOUSE_MODE_SELECTING_ROW_ZONE;
    public static final int MOUSE_MODE_SELECTING_AREA = 16;
    public static final float MIN_SCALE_DISPLAY_ATTRIBUTE_NAME = 0.5f;
    public static final float MAX_MOUSE_DISTANCE_TO_LINE = 1;
    // =============EVENT KEY=============//
    public static final String KEY_HEADER = "KEY_HEADER"; // header of start
    // event
    public static final String VAL_HEADER_UPDATE = "VAL_HEADER_UPDATE";
    public static final String VAL_HEADER_CLEAR = "VAL_HEADER_CLEAR";
    // --
    public static final String KEY_MATRIX = "KEY__MATRIX";
    public static final String KEY_LIST_POINT = "KEY_LIST_POINT";
    public static final String KEY_FILE = "KEY_FILE";
    public static final String KEY_PATH = "KEY_PATH";
    public static final String KEY_TYPE = "KEY_TYPE";
    public static final String KEY_FONT_MANAGER = "KEY_FONT_MANAGER";
    // ===============TRANSFER TYPE===============//
    public static final int TYPE_DATA = 1;
    public static final int TYPE_ORDER = 2;
    public static final int TYPE_SUGGESTION_SEARCH = 3;
    public static final int TYPE_SUGGESTION_SEARCH_FAIL = 4;
    public static final int TYPE_SUGGESTION_SEARCH_OK = 5;
    public static final int TYPE_REQUEST_MATRIX_GRAPH = 6;
    public static final int TYPE_REQUEST_STORED_GRAPH = 7;
    public static final int TYPE_REQUEST_STORED_GRAPH_ERROR = 8;
    public static final int TYPE_STORE_GRAPH = 9;
    public static final int TYPE_STORE_GRAPH_ERROR = 10;
    public static final int TYPE_SEARCH_REQUEST = 11;
    public static final int TYPE_SEARCH_RESPOND = 12;
    public static final int TYPE_SEARCH_CHANGE_TYPE_GRAPH = 13;
    public static final int TYPE_SEARCH_STEP_REQUEST = 14;
    public static final int TYPE_SEARCH_RESPOND_INFO = 15;
    public static final int TYPE_SEARCH_RESPOND_KEYWORD = 16;
    public static final int TYPE_SEARCH_THREE_STEP_REQUEST = 17;
    public static final int TYPE_SEARCH_RESPOND_DBLP_INFO = 18;
    public static final int TYPE_SEARCH_RESPOND_DETAIL_INFO = 19;
    public static final int TYPE_END_SERVER = -1000;
    // ---
    public static final int TYPE_DATA_PAPER = 1;
    public static final int TYPE_DATA_AUTHORS = 2;
    public static final int TYPE_DATA_CATEGORIES = 3;
    public static final int TYPE_DATA_CONCEPTS = 4;
    public static final int TYPE_DATA_USERS = 5;
    // --------------------------------------------
    public static final int TYPE_METRIC_JACCARD = 1;
    public static final int TYPE_METRIC_MANHATTAN = 2;
    public static final int TYPE_METRIC_HAMMING = 3;
    public static final int TYPE_LINKAGE_AVERAGE = 1;
    public static final int TYPE_LINKAGE_SINGLE = 2;
    public static final int TYPE_LINKAGE_COMPLETE = 3;
}
