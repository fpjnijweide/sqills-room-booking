//package nl.utwente.resource;
//
//import junit.framework.TestCase;
//import nl.utwente.dao.RoomDao;
//import org.glassfish.jersey.server.ResourceConfig;
//import org.glassfish.jersey.test.JerseyTest;
//import org.glassfish.jersey.test.TestProperties;
//import org.junit.Test;
//
//import javax.ws.rs.core.Application;
//
//public class BookingResourceTest extends JerseyTest {
//
//    @Override
//    public Application BookingResourceTest(){
//        enable(TestProperties.LOG_TRAFFIC);
//        enable(TestProperties.DUMP_ENTITY);
//        return new ResourceConfig(BookingResource.class);
//    }
//
//    @Test
//    public void test(){
//        RoomDao roomDao = new RoomDao();
//
//
//
//
//        TestCase.assertEquals(, roomDao.getAllRooms());
//    }
//
//}
