package ro.teamnet.bootstrap.repository;

/**
 * @author cristian.uricec
 */
import java.math.BigInteger;
import java.security.SecureRandom;

public final class TokenGenerator {
    private SecureRandom random = new SecureRandom();

    public String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }
}
