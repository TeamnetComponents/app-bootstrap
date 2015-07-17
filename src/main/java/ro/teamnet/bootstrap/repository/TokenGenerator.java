package ro.teamnet.bootstrap.repository;

/**
 * Created by cristian.uricec on 3/16/2015.
 */
import java.math.BigInteger;
import java.security.SecureRandom;

public final class TokenGenerator {
    private SecureRandom random = new SecureRandom();

    public String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }
}
