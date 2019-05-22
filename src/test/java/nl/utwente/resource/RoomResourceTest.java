package nl.utwente.resource;

import nl.utwente.dao.RoomDao;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
public class RoomResourceTest extends JerseyTest {

    @Override
    public Application configure(){
        return new ResourceConfig(RoomResource.class);
    }

    @Test
    public void listRoom(){
        Response res = target().path("/room/list").request().get();
        assertEquals("Should return status 200", 200, res.getStatus());
        assertEquals(RoomDao.getAllRooms().toString().replaceAll("\\s",""), res.readEntity(String.class));
    }




}
