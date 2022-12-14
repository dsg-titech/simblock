import org.junit.Assert;
import org.junit.Test;
import  simblock.simulator.Timer;
public class TimerTest {
    @Test
    public void getTaskTest() {
        Assert.assertEquals(null, Timer.getTask());
    }

}
