package no.hvl.dat250.voting.driver;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.Before;
import org.junit.Test;

public class VotingMainTest {
    private EntityManagerFactory factory;

    @Before
    public void setUp() {
        factory = Persistence.createEntityManagerFactory(VotingMain.PERSISTENCE_UNIT_NAME);
    }


    @Test
    public void testDomainModelPersistence() {
        // TODO: Test modal persistence
    }
}
