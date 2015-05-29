package Shootan.Bullets;

import Shootan.Utils.IndexWrapper;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class BulletSerializationTests {

    @Test
    public void testCreateSerialized() {
        Bullet b=new Rocket(0, 10, 10, 1.34f);

        Bullet l=Bullet.createDeserialized(b.serialize(), new IndexWrapper());

        assertTrue(Math.abs(b.getX()-l.getX())<=0.01);
        assertTrue(Math.abs(b.getY()-l.getY())<=0.01);

        assertEquals(b.getAuthor(), l.getAuthor());
    }

}
