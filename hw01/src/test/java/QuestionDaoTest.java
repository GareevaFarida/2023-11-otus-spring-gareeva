import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.spring.hw.config.AppProperties;
import ru.otus.spring.hw.config.TestFileNameProvider;
import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.dao.QuestionDaoCsv;

public class QuestionDaoTest {

    @Test
    public void testParsingFile() {
        TestFileNameProvider testFileNameProvider = Mockito.mock(AppProperties.class);
        Mockito.when(testFileNameProvider.getTestFileName()).thenReturn("test-for-students.csv");
        QuestionDao questionDao = new QuestionDaoCsv(testFileNameProvider);
        Assertions.assertEquals(5, questionDao.findAll().size());
    }
}
