import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.spring.hw.config.AppProperties;
import ru.otus.spring.hw.config.TestFileNameProvider;
import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.dao.QuestionDaoCsv;

public class QuestionDaoTest {
    //Знаю, что надо удалять, но мне так нагляднее, какой сервис от какого зависит, поэтому просто закомментировала.
    //    @Test
    //    public void testWithoutSpring() {
    //        TestFileNameProvider testFileNameProvider = new AppProperties("dao/test-for-students.csv");
    //        QuestionDao questionDao = new QuestionDaoCsv(testFileNameProvider);
    //        IOService ioService = new IOStreamsService(System.out);
    //        QuestionFormatter formatter = QuestionFormatterImpl.builder()
    //                .questionDelimiter(System.lineSeparator()
    //                        + "----------------------------------------------------------")
    //                .answerTabulation("    ")
    //                .answerArabicNumeration(true)
    //                .build();
    //        TestService testService = new TestServiceImpl(ioService, questionDao, formatter);
    //        testService.executeTest();
    //    }

    @Test
    public void testParsingFile() {
        TestFileNameProvider testFileNameProvider = Mockito.mock(AppProperties.class);
        Mockito.when(testFileNameProvider.getTestFileName()).thenReturn("test-for-students.csv");
        QuestionDao questionDao = new QuestionDaoCsv(testFileNameProvider);
        Assertions.assertEquals(5, questionDao.findAll().size());
    }
}
