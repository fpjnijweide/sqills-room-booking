package nl.utwente.resource;

import com.google.gson.Gson;
import nl.utwente.dao.BookingDao;
import nl.utwente.dao.RoomDao;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RoomResourceTest extends JerseyTest {

    @Override
    public Application configure(){
        return new ResourceConfig(RoomResource.class);
    }

    @Test
    public void listRoom(){
        Response res = target().path("/room/list").request().get();
        assertEquals("Should return status 200", 200, res.getStatus());
        assertNotNull(res);
        assertEquals(RoomDao.getAllRoomsIDs().toString().replaceAll("\\s",""), res.readEntity(String.class));
    }

    @Test
    public void getBookingsForSpecificRoomToday(){
        String jsonCheck = new Gson().toJson(BookingDao.getBookingsForRoomToday(1));
        System.out.println(jsonCheck);
//        for (int i = 0; i < 10; i++) {
//            Response res = target().path("/room/"+i).request().get();
//            String json = new Gson().toJson(res);
//            assertEquals("Should return status 200", 200, res.getStatus());
//            String jsonCheck = new Gson().toJson(BookingDao.getBookingsForRoomToday(i));
//            System.out.println(jsonCheck);
//            assertEquals(BookingDao.getBookingsForRoomToday(i).toString(), json);
//
//        }
    }



}
