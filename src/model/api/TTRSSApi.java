package model.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.SocketException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Date;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;

import model.Article;
import model.Category;
import model.Feed;
import preferences.ArticleOmitter;
import preferences.Constants;
import preferences.StringSupport;
import model.Label;

/**
 * Class that accesses the API for TT-RSS so that the API actions can be
 * preformed
 *
 * @author Sam
 *
 */
public class TTRSSApi extends JSONObject {

    private static final String TAG = TTRSSApi.class.getSimpleName();

    protected static String lastError = "";
    protected static boolean hasLastError = false;

    private static final String PARAM_OP = "op";
    private static final String PARAM_USER = "user";
    private static final String PARAM_PW = "password";
    private static final String PARAM_CAT_ID = "cat_id";
    private static final String PARAM_CATEGORY_ID = "category_id";
    private static final String PARAM_FEED_ID = "feed_id";
    private static final String PARAM_FEED_URL = "feed_url";
    private static final String PARAM_ARTICLE_IDS = "article_ids";
    private static final String PARAM_LIMIT = "limit";
    private static final int PARAM_LIMIT_API_5 = 60;
    private static final String PARAM_VIEWMODE = "view_mode";
    private static final String PARAM_SHOW_CONTENT = "show_content";
    // include_attachments available since 1.5.3 but is ignored on older
    // versions
    private static final String PARAM_INC_ATTACHMENTS = "include_attachments";
    private static final String PARAM_SINCE_ID = "since_id";
    private static final String PARAM_SEARCH = "search";
    private static final String PARAM_SKIP = "skip";
    private static final String PARAM_MODE = "mode";
    // 0-starred, 1-published, 2-unread, 3-article note (since api level 1)
    private static final String PARAM_FIELD = "field";
    // optional data parameter when setting note field
    private static final String PARAM_DATA = "data";
    private static final String PARAM_IS_CAT = "is_cat";
    private static final String PARAM_PREF = "pref_name";

    private static final String VALUE_LOGIN = "login";
    private static final String VALUE_GET_CATEGORIES = "getCategories";
    private static final String VALUE_GET_FEEDS = "getFeeds";
    private static final String VALUE_GET_HEADLINES = "getHeadlines";
    private static final String VALUE_UPDATE_ARTICLE = "updateArticle";
    private static final String VALUE_CATCHUP = "catchupFeed";
    private static final String VALUE_UPDATE_FEED = "updateFeed";
    private static final String VALUE_GET_PREF = "getPref";
    private static final String VALUE_SET_LABELS = "setArticleLabel";
    private static final String VALUE_SHARE_TO_PUBLISHED = "shareToPublished";
    private static final String VALUE_FEED_SUBSCRIBE = "subscribeToFeed";
    private static final String VALUE_FEED_UNSUBSCRIBE = "unsubscribeFeed";

    private static final String VALUE_LABEL_ID = "label_id";
    private static final String VALUE_ASSIGN = "assign";

    private static final String ERROR = "error";
    private static final String LOGIN_ERROR = "LOGIN_ERROR";
    private static final String NOT_LOGGED_IN = "NOT_LOGGED_IN";
    private static final String UNKNOWN_METHOD = "UNKNOWN_METHOD";
    private static final String API_DISABLED = "API_DISABLED";
    private static final String INCORRECT_USAGE = "INCORRECT_USAGE";

    private static final String STATUS = "status";
    private static final String API_LEVEL = "api_level";

    // session id as an OUT parameter
    private static final String SESSION_ID = "session_id";
    private static final String ID = "id";

    private static final String TITLE = "title";
    private static final String UNREAD = "unread";

    private static final String CAT_ID = "cat_id";

    private static final String CONTENT = "content";

    private static final String URL_SHARE = "url";
    private static final String FEED_URL = "feed_url";

    private static final String CONTENT_URL = "content_url";

    private static final String VALUE = "value";

    private static final int MAX_ID_LIST_LENGTH = 100;

    // session id as an IN parameter
    protected static final String SID = "sid";

    protected boolean httpAuth = false;
    protected String httpUsername;
    protected String httpPassword;
    protected String base64NameAndPw = null;

    protected String sessionId = null;

    private final Object lock = new Object();
    private int apiLevel = -1;

    public static final int PARAM_LIMIT_MAX_VALUE = 200;

    /**
     * Returns the last error-message and resets the error-state of the
     * connector.
     *
     * @return a string with the last error-message.
     */
    public String pullLastError() {
        String ret = new String(lastError);
        lastError = "";
        hasLastError = false;
        return ret;
    }

    private int parseArticleArray(final Set<Article> articles, JsonReader reader, ArticleOmitter filter) {
        long time = System.currentTimeMillis();
        int count = 0;

        try {
            reader.beginArray();
            while (reader.hasNext()) {
                Article article = new Article();

                reader.beginObject();
                boolean skipObject = parseArticle(article, reader, filter);
                reader.endObject();

                if (!skipObject && article.id != -1 && article.title != null) {
                    articles.add(article);
                }
                count++;
            }
            reader.endArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    private boolean parseArticle(final Article a, final JsonReader reader, final ArticleOmitter filter)
            throws IOException {

        boolean skipObject = false;
        while (reader.hasNext() && reader.peek().equals(JsonToken.NAME)) {
            if (skipObject) {
                // field name
                reader.skipValue();
                // field value
                reader.skipValue();
                continue;
            }

            String name = reader.nextName();
            Article.ArticleField field = Article.ArticleField.valueOf(name);

            try {

                switch (field) {
                    case id:
                        a.id = reader.nextInt();
                        break;
                    case title:
                        a.title = reader.nextString();
                        break;
                    case unread:
                        a.isUnread = reader.nextBoolean();
                        break;
                    case updated:
                        a.updated = new Date(reader.nextLong() * 1000);
                        break;
                    case feed_id:
                        if (reader.peek() == JsonToken.NULL) {
                            reader.nextNull();
                        } else {
                            a.feedId = reader.nextInt();
                        }
                        break;
                    case content:
                        a.content = reader.nextString().replaceAll("(<(?:img|video)[^>]+?src=[\"'])//([^\"']*)",
                                "$1https://$2");
                        break;
                    case link:
                        a.url = reader.nextString();
                        // Some URLs may start with // to indicate that both, http
                        // and https can be used
                        if (a.url.startsWith("//")) {
                            a.url = "https:" + a.url;
                        }
                        break;
                    case comments:
                        a.commentUrl = reader.nextString();
                        // Some URLs may start with // to indicate that both, http
                        // and https can be used
                        if (a.commentUrl.startsWith("//")) {
                            a.commentUrl = "https:" + a.commentUrl;
                        }
                        break;
                    case attachments:
                        a.attachments = parseAttachments(reader);
                        break;
                    case marked:
                        a.isStarred = reader.nextBoolean();
                        break;
                    case published:
                        a.isPublished = reader.nextBoolean();
                        break;
                    case labels:
                        a.labels = parseLabels(reader);
                        break;
                    case author:
                        a.author = reader.nextString();
                        break;
                    case note:
                        if (reader.peek() == JsonToken.NULL) {
                            reader.nextNull();
                        } else {
                            a.note = reader.nextString();
                        }
                        break;
                    default:
                        reader.skipValue();
                        continue;
                }

                if (filter != null) {
                    skipObject = filter.omitArticle(field, a);
                }

            } catch (IllegalArgumentException | IOException e) {

                reader.skipValue();
            }
        }
        return skipObject;
    }

    private Set<String> parseAttachments(JsonReader reader) throws IOException {
        Set<String> ret = new HashSet<>();
        reader.beginArray();
        while (reader.hasNext()) {

            String attId = null;
            String attUrl = null;

            reader.beginObject();
            while (reader.hasNext()) {

                try {
                    switch (reader.nextName()) {
                        case CONTENT_URL:
                            attUrl = reader.nextString();
                            // Some URLs may start with // to indicate that both,
                            // http and https can be used
                            if (attUrl.startsWith("//")) {
                                attUrl = "https:" + attUrl;
                            }
                            break;
                        case ID:
                            attId = reader.nextString();
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    reader.skipValue();
                }

            }
            reader.endObject();

            if (attId != null && attUrl != null) {
                ret.add(attUrl);
            }
        }
        reader.endArray();
        return ret;
    }

    private Set<Label> parseLabels(final JsonReader reader) throws IOException {
        Set<Label> ret = new HashSet<>();

        if (reader.peek().equals(JsonToken.BEGIN_ARRAY)) {
            reader.beginArray();
        } else {
            reader.skipValue();
            return ret;
        }

        try {
            while (reader.hasNext()) {

                Label label = new Label();
                reader.beginArray();
                try {
                    label.id = Integer.parseInt(reader.nextString());
                    label.caption = reader.nextString();
                    label.foregroundColor = reader.nextString();
                    label.backgroundColor = reader.nextString();
                    label.checked = true;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    reader.skipValue();
                    continue;
                }
                ret.add(label);
                reader.endArray();
            }
            reader.endArray();
        } catch (Exception e) {
            // Ignore exceptions here
            try {
                if (reader.peek().equals(JsonToken.END_ARRAY)) {
                    reader.endArray();
                }
            } catch (Exception ee) {
                // Empty!
            }
        }

        return ret;
    }

    /**
     * logs a user into their account
     *
     * @return false boolean if logged in successful
     * @return true boolean if not logged in
     * @throws UnsupportedEncodingException if nothing works
     *
     */
    public boolean login() throws UnsupportedEncodingException {
        float time = System.currentTimeMillis();
        String username = Constants.username;
        String password = Constants.password;

        if (sessionId != null && !lastError.equals(NOT_LOGGED_IN)) {
            return true;
        }

        synchronized (lock) {

            if (sessionId != null && !lastError.equals(NOT_LOGGED_IN)) {
                return true;
            }

            // Login done while we were waiting for the lock
            Map<String, String> params = new HashMap<>();
            params.put(PARAM_OP, VALUE_LOGIN);
            params.put(PARAM_USER, username);
            params.put(PARAM_PW, password);

            try {
                sessionId = readResult(params, true, false);
                if (sessionId != null) {
                    System.out.println("login: " + (System.currentTimeMillis() - time) + "ms");
                    return true;
                }
            } catch (IOException e) {
                if (!hasLastError) {
                    hasLastError = true;
                    lastError = formatException(e);
                }
            }

            if (!hasLastError) {
                // Login didnt succeed, write message
                hasLastError = true;
            }
            return false;
        }
    }

    private String readResult(Map<String, String> params, boolean login) throws IOException {
        return readResult(params, login, true);
    }

    private String readResult(Map<String, String> params, boolean login, boolean retry) throws IOException {
        InputStream in = doRequest(params);
        if (in == null) {
            return null;
        }

        JsonReader reader = null;
        String ret = "";
        try {
            reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            // Check if content contains array or object, array indicates
            // login-response or error, object is content

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (!name.equals("content")) {
                    reader.skipValue();
                    continue;
                }

                JsonToken t = reader.peek();
                if (!t.equals(JsonToken.BEGIN_OBJECT)) {
                    continue;
                }

                JsonObject object = new JsonObject();
                reader.beginObject();
                while (reader.hasNext()) {
                    object.addProperty(reader.nextName(), reader.nextString());
                }
                reader.endObject();

                if (object.get(SESSION_ID) != null) {
                    ret = object.get(SESSION_ID).getAsString();
                }
                if (object.get(STATUS) != null) {
                    ret = object.get(STATUS).getAsString();
                }
                if (this.apiLevel == -1 && object.get(API_LEVEL) != null) {
                    this.apiLevel = object.get(API_LEVEL).getAsInt();
                }
                if (object.get(VALUE) != null) {
                    ret = object.get(VALUE).getAsString();
                }
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        if (ret.startsWith("\"")) {
            ret = ret.substring(1, ret.length());
        }
        if (ret.endsWith("\"")) {
            ret = ret.substring(0, ret.length() - 1);
        }

        return ret;
    }

    protected InputStream doRequest(Map<String, String> params) {
        try {
            if (sessionId != null) {
                params.put(SID, sessionId);
            }
            String userurl = Constants.URL;

            JSONObject json = new JSONObject(params);
            byte[] outputBytes = json.toString().getBytes("UTF-8");

            logRequest(json);

            URL url = new URL(userurl + "/api/index.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);

            // Content
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-Length", Integer.toString(outputBytes.length));

            // HTTP-Basic Authentication
            if (base64NameAndPw != null) {
                con.setRequestProperty("Authorization", "Basic " + base64NameAndPw);
            }

            // Add POST data
            con.getOutputStream().write(outputBytes);

            // Try to check for HTTP Status codes
            int code = con.getResponseCode();
            if (code >= 400 && code < 600) {
                hasLastError = true;
                lastError = "Server returned status: " + code + " (Message: " + con.getResponseMessage() + ")";
                return null;
            }

            // Everything is fine!
            return con.getInputStream();

        } catch (SSLPeerUnverifiedException e) {

        } catch (SSLException e) {
            if ("No peer certificate".equals(e.getMessage())) {
                // Handle this by ignoring it, this occurrs very often when the
                // connection is instable.
                // Log.w(TAG, "SSLException in doRequest(): " +
                // formatException(e));
            } else {
                hasLastError = true;
                lastError = "SSLException in doRequest(): " + formatException(e);
            }
        } catch (InterruptedIOException e) {
            System.out.println("InterruptedIOException in doRequest(): " + formatException(e));
        } catch (SocketException e) {
            System.out.println("SocketException in doRequest(): " + formatException(e));
        } catch (Exception e) {
            hasLastError = true;
            lastError = "Exception in doRequest(): " + formatException(e);
        }

        return null;
    }

    protected void logRequest(final JSONObject json) throws JSONException {
        // Filter password and session-id
        Object paramPw = json.remove(PARAM_PW);
        Object paramSID = json.remove(SID);
        // Log.i(TAG, json.toString());
        json.put(PARAM_PW, paramPw);
        json.put(SID, paramSID);
    }

    protected static String formatException(Exception e) {
        return e.getMessage() + (e.getCause() != null ? "(" + e.getCause() + ")" : "");
    }

    /**
     * Returns a set of all the feeds that a user is subscribed to
     *
     * @return ret: Set
     * @throws UnsupportedEncodingException if nothing works
     *
     */
    public Set<Feed> getFeeds() throws UnsupportedEncodingException {
        Set<Feed> ret = new LinkedHashSet<>();
        if (sessionNotAlive()) {
            return ret;
        }

        Map<String, String> params = new HashMap<>();
        params.put(PARAM_OP, VALUE_GET_FEEDS);
        params.put(PARAM_CAT_ID, "-4" + ""); // Hardcoded -4 fetches all feeds.

        JsonReader reader = null;
        try {
            reader = prepareReader(params);

            if (reader == null) {
                return ret;
            }

            reader.beginArray();
            while (reader.hasNext()) {

                int categoryId = -1;
                int id = 0;
                String title = null;
                String feedUrl = null;
                int unread = 0;

                reader.beginObject();
                while (reader.hasNext()) {

                    try {
                        switch (reader.nextName()) {
                            case ID:
                                id = reader.nextInt();
                                break;
                            case CAT_ID:
                                categoryId = reader.nextInt();
                                break;
                            case TITLE:
                                title = reader.nextString();
                                break;
                            case FEED_URL:
                                feedUrl = reader.nextString();
                                break;
                            case UNREAD:
                                unread = reader.nextInt();
                                break;
                            default:
                                reader.skipValue();
                                break;
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        reader.skipValue();
                    }

                }
                reader.endObject();

                if (id != -1 || categoryId == -2) { // normal feed (>0) or label
                    // (-2)
                    if (title != null) {
                        Feed f = new Feed();
                        f.id = id;
                        f.categoryId = categoryId;
                        f.title = title;
                        f.url = feedUrl;
                        f.unread = unread;
                        ret.add(f);
                    }
                }

            }
            reader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    // Empty!
                }
            }
        }

        final long time = System.currentTimeMillis();
        System.out.println("getFeeds: " + (System.currentTimeMillis() - time) + "ms");
        return ret;
    }

    /**
     * checks if the session_id is still logged in
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    private boolean sessionNotAlive() throws UnsupportedEncodingException {
        // Make sure we are logged in
        if (sessionId == null || lastError.equals(NOT_LOGGED_IN)) {
            if (!login()) {
                return true;
            }
        }
        return hasLastError;
    }

    private JsonReader prepareReader(Map<String, String> params, boolean firstCall) throws IOException {
        InputStream in = doRequest(params);
        if (in == null) {
            return null;
        }
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        // Check if content contains array or object, array indicates
        // login-response or error, object is content
        try {
            reader.beginObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("content")) {
                JsonToken t = reader.peek();

                if (t.equals(JsonToken.BEGIN_ARRAY)) {
                    return reader;
                } else if (t.equals(JsonToken.BEGIN_OBJECT)) {

                    JsonObject object = new JsonObject();
                    reader.beginObject();

                    String nextName = reader.nextName();
                    // We have a BEGIN_OBJECT here but its just the response to
                    // call "subscribeToFeed"
                    if ("status".equals(nextName)) {
                        return reader;
                    }

                    // Handle error
                    while (reader.hasNext()) {
                        if (nextName != null) {
                            object.addProperty(nextName, reader.nextString());
                            nextName = null;
                        } else {
                            object.addProperty(reader.nextName(), reader.nextString());
                        }
                    }
                    reader.endObject();
                }

            } else {
                reader.skipValue();
            }
        }
        return null;
    }

    private JsonReader prepareReader(Map<String, String> params) throws IOException {
        return prepareReader(params, true);
    }

    /**
     * Retrieves all categories.
     *
     * @return ret: Set
     * @throws UnsupportedEncodingException if nothing works just in case
     */
    public Set<Category> getCategories() throws UnsupportedEncodingException {
        Set<Category> ret = new LinkedHashSet<>();
        if (sessionNotAlive()) {
            return ret;
        }

        Map<String, String> params = new HashMap<>();
        params.put(PARAM_OP, VALUE_GET_CATEGORIES);

        JsonReader reader = null;
        try {
            reader = prepareReader(params);

            if (reader == null) {
                return ret;
            }

            reader.beginArray();
            while (reader.hasNext()) {

                int id = -1;
                String title = null;
                int unread = 0;

                reader.beginObject();
                while (reader.hasNext()) {

                    try {
                        switch (reader.nextName()) {
                            case ID:
                                id = reader.nextInt();
                                break;
                            case TITLE:
                                title = reader.nextString();
                                break;
                            case UNREAD:
                                unread = reader.nextInt();
                                break;
                            default:
                                reader.skipValue();
                                break;
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        reader.skipValue();
                    }

                }
                reader.endObject();

                if (id > 0 && title != null) {
                    ret.add(new Category(id, title, unread));
                }
            }
            reader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    // Empty!
                }
            }
        }
        return ret;
    }

    /**
     * Class to deal with messages
     *
     * @author Sam
     *
     */
    public class SubscriptionResponse {

        public int code = -1;
        public String message = null;
    }

    /**
     * Subscribe to a feed with the URL and the category of choice
     *
     * @param feed_url String
     * @param category_id int
     * @return ret: SubscriptionRespomse
     * @throws UnsupportedEncodingException if nothing works just in case
     */
    public SubscriptionResponse feedSubscribe(String feed_url, int category_id) throws UnsupportedEncodingException {
        SubscriptionResponse ret = new SubscriptionResponse();
        if (sessionNotAlive()) {
            return ret;
        }

        Map<String, String> params = new HashMap<>();
        params.put(PARAM_OP, VALUE_FEED_SUBSCRIBE);
        params.put(PARAM_FEED_URL, feed_url);
        params.put(PARAM_CATEGORY_ID, category_id + "");

        String code = "";
        String message = null;
        JsonReader reader = null;
        try {
            reader = prepareReader(params);
            if (reader == null) {
                return ret;
            }

            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "code":
                        code = reader.nextString();
                        break;
                    case "message":
                        message = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            if (!code.contains(UNKNOWN_METHOD)) {
                ret.code = Integer.parseInt(code);
                ret.message = message;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    // Empty!
                }
            }
        }

        return ret;
    }

    private boolean serverWork(Integer feedId) throws UnsupportedEncodingException {
        if (Constants.isOnline) {
            Map<String, String> taskParams = new HashMap<>();
            taskParams.put(PARAM_OP, VALUE_UPDATE_FEED);
            taskParams.put(PARAM_FEED_ID, String.valueOf(feedId));
            return doRequestNoAnswer(taskParams);
        }
        return true;
    }

    private long noTaskUntil = 0;

    /**
     * Retrieves the specified articles.
     *
     * @param articles container for retrieved articles
     * @param id the id of the feed/category
     * @param limit the maximum number of articles to be fetched
     * @param viewMode indicates whether only unread articles should be included
     * (Possible values: all_articles, unread, adaptive, marked, updated)
     * @param isCategory indicates if we are dealing with a category or a feed
     * @param sinceId the first ArticleId which is to be retrieved.
     * @param search search query
     * @param filter filter for articles, defining which articles should be
     * omitted while parsing (may be {@code
     *                   null})
     * @throws UnsupportedEncodingException if nothing works
     */
    public void getHeadlines(final Set<Article> articles, Integer id, int limit, String viewMode, boolean isCategory,
            Integer sinceId, String search, ArticleOmitter filter) throws UnsupportedEncodingException {
        int offset = 0;
        int count;
        int maxSize = articles.size() + limit;

        if (sessionNotAlive()) {
            return;
        }

        serverWork(id);

        int limitParam = Math.min((apiLevel < 6) ? PARAM_LIMIT_API_5 : PARAM_LIMIT_MAX_VALUE, limit);

        while (articles.size() < maxSize) {

            Map<String, String> params = new HashMap<>();
            params.put(PARAM_OP, VALUE_GET_HEADLINES);
            params.put(PARAM_FEED_ID, id + "");
            params.put(PARAM_LIMIT, limitParam + "");
            params.put(PARAM_SKIP, offset + "");
            params.put(PARAM_VIEWMODE, viewMode);
            params.put(PARAM_IS_CAT, (isCategory ? "1" : "0"));
            params.put(PARAM_SHOW_CONTENT, "1");
            params.put(PARAM_INC_ATTACHMENTS, "1");
            if (sinceId > 0) {
                params.put(PARAM_SINCE_ID, sinceId + "");
            }
            if (search != null) {
                params.put(PARAM_SEARCH, search);
            }

            JsonReader reader = null;
            try {
                reader = prepareReader(params);

                if (hasLastError) {
                    return;
                }
                if (reader == null) {
                    continue;
                }

                count = parseArticleArray(articles, reader, filter);

                if (count < limitParam) {
                    break;
                } else {
                    offset += count;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ignored) {
                        // Empty!
                    }
                }
            }
        }
    }

    /**
     * Gets the set of articles
     *
     * @param id the id of the feed/category
     * @param limit the maximum number of articles to be fetched
     * @param viewMode indicates whether only unread articles should be included
     * (Possible values: all_articles, unread, adaptive, marked, updated)
     * @param isCategory indicates if we are dealing with a category or a feed
     * @param sinceId the first ArticleId which is to be retrieved.
     * @param search search query
     * @param filter filter for articles, defining which articles should be
     * omitted while parsing (may be {@code
     *                   null})
     * @return ret: Set
     * @throws UnsupportedEncodingException if nothing works
     */
    public Set<Article> getArticles(Integer id, int limit, String viewMode, boolean isCategory, Integer sinceId,
            String search, ArticleOmitter filter) throws UnsupportedEncodingException {
        Set<Article> ret = new LinkedHashSet<>();

        getHeadlines(ret, id, limit, viewMode, isCategory, sinceId, search, filter);
        return ret;
    }

    private boolean doRequestNoAnswer(Map<String, String> params) throws UnsupportedEncodingException {
        if (sessionNotAlive()) {
            return false;
        }

        try {
            String result = readResult(params, false);

            // Reset error, this is only for an api-bug which returns an empty
            // result for updateFeed
            if (result == null) {
                pullLastError();
            }

            return "OK".equals(result);
        } catch (MalformedJsonException mje) {
            // Reset error, this is only for an api-bug which returns an empty
            // result for updateFeed
            pullLastError();
        } catch (IOException e) {
            e.printStackTrace();
            if (!hasLastError) {
                hasLastError = true;
                lastError = formatException(e);
            }
        }

        return false;
    }

    /**
     * Marks the given Article as "starred"/"not starred" depending on int
     * articleState.
     *
     * @param ids a list of article-ids.
     * @param articleState the new state of the article (0 - not starred; 1 -
     * starred; 2 - toggle).
     * @return true if the operation succeeded.
     * @throws UnsupportedEncodingException if nothing works
     */
    public boolean setArticleStarred(Set<Integer> ids, int articleState) throws UnsupportedEncodingException {
        boolean ret = true;
        if (ids.size() == 0) {
            return true;
        }

        for (String idList : StringSupport.convertListToString(ids, MAX_ID_LIST_LENGTH)) {
            Map<String, String> params = new HashMap<>();
            params.put(PARAM_OP, VALUE_UPDATE_ARTICLE);
            params.put(PARAM_ARTICLE_IDS, idList);
            params.put(PARAM_MODE, articleState + "");
            params.put(PARAM_FIELD, "0");
            ret = ret && doRequestNoAnswer(params);
        }
        return ret;
    }

    /**
     * Marks a feed or a category with all its feeds as read.
     *
     * @param id the feed-id/category-id.
     * @param isCategory indicates whether id refers to a feed or a category.
     * @return true if the operation succeeded.
     * @throws UnsupportedEncodingException if nothing works
     */
    public boolean setRead(int id, boolean isCategory) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_OP, VALUE_CATCHUP);
        params.put(PARAM_FEED_ID, id + "");
        params.put(PARAM_IS_CAT, (isCategory ? "1" : "0"));
        return doRequestNoAnswer(params);
    }

    /**
     * Marks the given Articles as "published"/"not published" depending on
     * articleState.
     *
     * @param ids a list of article-ids.
     * @param articleState the new state of the articles (0 - not published; 1 -
     * published; 2 - toggle).
     * @return true if the operation succeeded.
     * @throws UnsupportedEncodingException if nothing works
     */
    public boolean setArticlePublished(Set<Integer> ids, int articleState) throws UnsupportedEncodingException {
        if (ids.size() == 0) {
            return true;
        }
        boolean ret = true;
        for (String idList : StringSupport.convertListToString(ids, MAX_ID_LIST_LENGTH)) {
            Map<String, String> params = new HashMap<>();
            params.put(PARAM_OP, VALUE_UPDATE_ARTICLE);
            params.put(PARAM_ARTICLE_IDS, idList);
            params.put(PARAM_MODE, articleState + "");
            params.put(PARAM_FIELD, "1");
            ret = ret && doRequestNoAnswer(params);
        }
        return ret;
    }

    /**
     * Adds a note to the given articles
     *
     * @param ids a list of article-id's with corresponding notes (may be null).
     * @return true if the operation succeeded.
     * @throws UnsupportedEncodingException if nothing works
     */
    public boolean setArticleNote(Map<Integer, String> ids) throws UnsupportedEncodingException {
        if (ids.size() == 0) {
            return true;
        }
        boolean ret = true;
        for (Integer id : ids.keySet()) {
            String note = ids.get(id);
            if (note == null) {
                continue;
            }

            Map<String, String> params = new HashMap<>();
            params.put(PARAM_OP, VALUE_UPDATE_ARTICLE);
            params.put(PARAM_ARTICLE_IDS, id + "");
            params.put(PARAM_FIELD, "3"); // Field 3 is the "Add note" field
            params.put(PARAM_DATA, note);
            ret = ret && doRequestNoAnswer(params);
        }
        return ret;
    }

    /**
     * Sets the article label
     *
     * @param articleIds: Set
     * @param labelId: int
     * @param assign: boolean
     * @return true if the operation succeeded.
     * @throws UnsupportedEncodingException if nothing works
     */
    public boolean setArticleLabel(Set<Integer> articleIds, int labelId, boolean assign)
            throws UnsupportedEncodingException {
        boolean ret = true;
        if (articleIds.size() == 0) {
            return true;
        }

        for (String idList : StringSupport.convertListToString(articleIds, MAX_ID_LIST_LENGTH)) {
            Map<String, String> params = new HashMap<>();
            params.put(PARAM_OP, VALUE_SET_LABELS);
            params.put(PARAM_ARTICLE_IDS, idList);
            params.put(VALUE_LABEL_ID, labelId + "");
            params.put(VALUE_ASSIGN, (assign ? "1" : "0"));
            ret = ret && doRequestNoAnswer(params);
        }

        return ret;
    }

    /**
     * Unsubscribe's from a feed
     *
     * @param feed_id: int
     * @return true if the operation succeeded.
     * @throws UnsupportedEncodingException if nothing works
     */
    public boolean feedUnsubscribe(int feed_id) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_OP, VALUE_FEED_UNSUBSCRIBE);
        params.put(PARAM_FEED_ID, feed_id + "");
        return doRequestNoAnswer(params);
    }

    /**
     * Creates an article with specified data in the Published feed
     *
     * @param title: String
     * @param url: String
     * @param content: String
     * @return true if the operation succeeded.
     * @throws UnsupportedEncodingException if nothing works
     */
    public boolean shareToPublished(String title, String url, String content) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_OP, VALUE_SHARE_TO_PUBLISHED);
        params.put(TITLE, title);
        params.put(URL_SHARE, url);
        params.put(CONTENT, content);
        return doRequestNoAnswer(params);
    }
}
