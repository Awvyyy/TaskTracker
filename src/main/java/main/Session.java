package main;


public class Session {
    static String currentUser;
    static boolean currentSession;

    public static String getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(String currentUser) {
        Session.currentUser = currentUser;
    }

    public static boolean isCurrentSession() {
        return currentSession;
    }

    public static void setCurrentSession(boolean currentSession) {
        Session.currentSession = currentSession;
    }
    public static void logOut(){
            setCurrentUser("");
            setCurrentSession(false);
    }
}
