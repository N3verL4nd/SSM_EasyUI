import cn.bjut.entity.Person;
import cn.bjut.mapper.PersonMapper;
import com.github.pagehelper.PageHelper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by N3verL4nd on 2017/7/4.
 */
public class T {
    private ApplicationContext ctx;

    @Before
    public void setUp() {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    @Test
    public void test() {
        PersonMapper personMapper = ctx.getBean(PersonMapper.class);
        PageHelper.startPage(1, 5);
        List<Person> personList = personMapper.selectAllPersons();
        personList.forEach(System.out::println);
    }
}
