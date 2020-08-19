package coolpharaoh.tee.speicher.tea.timer.core.tea;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.database.TeaMemoryDatabase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TeaRepositoryTest {
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    TeaDao teaDao;

    private TeaRepository teaRepository;

    @Before
    public void setUp() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getTeaDao()).thenReturn(teaDao);

        teaRepository = new TeaRepository(null);
    }

    @Test
    public void insertTea() {
        Tea tea = new Tea();

        teaRepository.insertTea(tea);

        verify(teaDao).insert(tea);
    }

    @Test
    public void updateTea() {
        Tea tea = new Tea();

        teaRepository.updateTea(tea);

        verify(teaDao).update(tea);
    }

    @Test
    public void deleteTea() {
        Tea tea = new Tea();

        teaRepository.deleteTea(tea);

        verify(teaDao).delete(tea);
    }

    @Test
    public void deleteAllTeas() {
        teaRepository.deleteAllTeas();

        verify(teaDao).deleteAll();
    }

    @Test
    public void getTeas() {
        when(teaDao.getTeas()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        List<Tea> teas = teaRepository.getTeas();

        assertThat(teas).hasSize(2);
    }


    @Test
    public void getTeasOrderByActivity() {
        when(teaDao.getTeasOrderByActivity()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        List<Tea> teas = teaRepository.getTeasOrderByActivity();

        assertThat(teas).hasSize(2);
    }


    @Test
    public void getTeasOrderByAlphabetic() {
        when(teaDao.getTeasOrderByAlphabetic()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        List<Tea> teas = teaRepository.getTeasOrderByAlphabetic();

        assertThat(teas).hasSize(2);
    }


    @Test
    public void getTeasOrderByVariety() {
        when(teaDao.getTeasOrderByVariety()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        List<Tea> teas = teaRepository.getTeasOrderByVariety();

        assertThat(teas).hasSize(2);
    }

    @Test
    public void getTeaById() {
        long teaId = 1;
        Tea tea = new Tea();
        when(teaDao.getTeaById(teaId)).thenReturn(tea);

        Tea teaById = teaRepository.getTeaById(teaId);

        assertThat(tea).isEqualTo(teaById);
    }

    @Test
    public void getTeasBySearchString() {
        String searchString = "search";
        when(teaDao.getTeasBySearchString(searchString)).thenReturn(Arrays.asList(new Tea(), new Tea()));

        List<Tea> teas = teaRepository.getTeasBySearchString(searchString);

        assertThat(teas).hasSize(2);
    }


}
