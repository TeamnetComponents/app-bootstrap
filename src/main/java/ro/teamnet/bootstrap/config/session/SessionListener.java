package ro.teamnet.bootstrap.config.session;


import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

    private Integer maxInactiveInterval = -1;

    public SessionListener(Integer maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        event.getSession().setMaxInactiveInterval(maxInactiveInterval);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {}
}
