package nl.utwente.resource;

import nl.utwente.dao.BookingDao;
import nl.utwente.exceptions.DAOException;
import nl.utwente.exceptions.InvalidBookingIDException;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BookingResourceTest extends JerseyTest {

    @Override
    public Application configure() {
        return new ResourceConfig(BookingResource.class);
    }

    @Test
    public void bookingIDBooking() {
        for (int i = 0; i < 10; i++) {
            Response res = target().path("/booking/bookingID").request().get();
            assertEquals("Should return status 200", 200, res.getStatus());
            try {
                assertEquals(BookingDao.getOutputBooking(i).toString().replaceAll("\\s", ""), res.readEntity(String.class));
            } catch (InvalidBookingIDException e) {
                fail("Invalid booking ID");
            } catch (DAOException e) {
                e.printStackTrace();
                fail();
            }
        }

    }

}
