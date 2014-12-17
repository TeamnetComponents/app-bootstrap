package ro.teamnet.bootstrap.security;

import ro.teamnet.bootstrap.config.Constants;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;import ro.teamnet.bootstrap.security.SecurityUtils;import java.lang.String;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    public String getCurrentAuditor() {
        String userName = SecurityUtils.getCurrentLogin();
        return (userName != null ? userName : Constants.SYSTEM_ACCOUNT);
    }
}
