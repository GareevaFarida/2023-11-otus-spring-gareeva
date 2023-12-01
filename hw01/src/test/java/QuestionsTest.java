import org.junit.jupiter.api.Test;
import ru.otus.spring.hw.config.AppProperties;
import ru.otus.spring.hw.config.TestFileNameProvider;
import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.dao.QuestionDaoCsv;
import ru.otus.spring.hw.service.IOService;
import ru.otus.spring.hw.service.IOStreamsService;
import ru.otus.spring.hw.service.TestService;
import ru.otus.spring.hw.service.TestServiceImpl;

public class QuestionsTest {
    @Test
    public void testWithoutSpring() {
        TestFileNameProvider testFileNameProvider = new AppProperties("dao/test-for-students.csv");
        QuestionDao questionDao = new QuestionDaoCsv(testFileNameProvider);
        IOService ioService = new IOStreamsService(System.out);
        TestService testService = new TestServiceImpl(ioService, questionDao);
        testService.executeTest();
    }
}
