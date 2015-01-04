import com.zhiyi.schedule.service.TaskDetailInfoService;
import com.zhiyi.schedule.service.TaskInfoService;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by chanedi on 2014/12/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring/applicationContext*.xml"})
public class SchedulerTest extends TestCase {

    protected Logger logger = Logger.getLogger(getClass().getName());

    @Resource
    private TaskInfoService taskInfoService;
    @Resource
    private TaskDetailInfoService taskDetailInfoService;

    @Test
    public void test() throws InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Thread.sleep(10000);
    }

}
