package nl.utwente.resource;

import nl.utwente.dao.BookingDao;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
public class BookingResourceTest extends JerseyTest {

    @Override
    public Application configure(){
        return new ResourceConfig(BookingResource.class);
    }

    @Test
    public void bookingIDBooking(){
        Response res = target().path("/booking/bookingID").request().get();
        assertEquals("Should return status 200", 200, res.getStatus());
        assertEquals(BookingDao.getSpecificBooking(1).toString().replaceAll("\\s",""), res.readEntity(String.class));
    }

}
