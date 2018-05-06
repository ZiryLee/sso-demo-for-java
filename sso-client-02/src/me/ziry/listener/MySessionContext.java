package me.ziry.listener;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * Created by Ziry on 2018/5/6.
 */
public class MySessionContext {

    private static HashMap mymap = new HashMap();

    public static synchronized void addSession(HttpSession session) {
        if (session != null) {
            mymap.put(session.getId(), session);
        }
    }

    public static synchronized void delSession(HttpSession session) {
        if (session != null) {
            mymap.remove(session.getId());
            session.invalidate();
        }
    }

    public static synchronized void delSessionById(String session_id) {
        HttpSession session = MySessionContext.getSession(session_id);
        if (session != null) {
            mymap.remove(session_id);
            session.invalidate();
        }
    }

    public static synchronized HttpSession getSession(String session_id) {
        if (session_id == null)
            return null;
        return (HttpSession) mymap.get(session_id);
    }

}
