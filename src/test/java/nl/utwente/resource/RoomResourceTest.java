 package nl.utwente.resource;

 import com.google.gson.Gson;
 import nl.utwente.dao.BookingDao;
 import nl.utwente.dao.RoomDao;
 import nl.utwente.exceptions.InvalidRoomNameException;
 import org.glassfish.jersey.server.ResourceConfig;
 import org.glassfish.jersey.test.JerseyTest;
 import org.junit.Test;

 import javax.ws.rs.core.Application;
 import javax.ws.rs.core.Response;

 import static nl.utwente.dao.RoomDao.getRoomName;
 import static org.junit.Assert.*;

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
        for (int nr = 0; nr < 10; nr++) {
            String i=getRoomName(nr);
            Response res = target().path("/room/"+i).request().get();
            String json = new Gson().toJson(res);
            assertEquals("Should return status 200", 200, res.getStatus());
            String jsonCheck = null;
            try {
                jsonCheck = new Gson().toJson(BookingDao.getBookingsForRoomToday(i));
            } catch (InvalidRoomNameException e) {
                fail(e.getMessage());
            }
            System.out.println(jsonCheck);
            try {
                assertEquals(BookingDao.getBookingsForRoomToday(i).toString(), json);
            } catch (InvalidRoomNameException e) {
                fail(e.getMessage());
            }
        }
    }
}
