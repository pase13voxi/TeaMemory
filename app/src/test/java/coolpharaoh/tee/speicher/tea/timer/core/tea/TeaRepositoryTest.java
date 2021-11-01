package coolpharaoh.tee.speicher.tea.timer.core.tea;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;


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
        final Tea tea = new Tea();

        teaRepository.insertTea(tea);

        verify(teaDao).insert(tea);
    }

    @Test
    public void updateTea() {
        final Tea tea = new Tea();

        teaRepository.updateTea(tea);

        verify(teaDao).update(tea);
    }

    @Test
    public void deleteTeaById() {
        final long teaId = 1;

        teaRepository.deleteTeaById(teaId);

        verify(teaDao).deleteTeaById(teaId);
    }

    @Test
    public void deleteAllTeas() {
        teaRepository.deleteAllTeas();

        verify(teaDao).deleteAll();
    }

    @Test
    public void getTeas() {
        when(teaDao.getTeas()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        final List<Tea> teas = teaRepository.getTeas();

        assertThat(teas).hasSize(2);
    }

    @Test
    public void getTeasOrderByActivity() {
        when(teaDao.getTeasOrderByActivity()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        final List<Tea> teas = teaRepository.getTeasOrderByActivity(false);

        assertThat(teas).hasSize(2);
    }

    @Test
    public void getFavoriteTeasOrderByActivity() {
        when(teaDao.getFavoriteTeasOrderByActivity()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        final List<Tea> teas = teaRepository.getTeasOrderByActivity(true);

        assertThat(teas).hasSize(2);
    }

    @Test
    public void getTeasOrderByAlphabetic() {
        when(teaDao.getTeasOrderByAlphabetic()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        final List<Tea> teas = teaRepository.getTeasOrderByAlphabetic(false);

        assertThat(teas).hasSize(2);
    }

    @Test
    public void getFavoriteTeasOrderByAlphabetic() {
        when(teaDao.getFavoriteTeasOrderByAlphabetic()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        final List<Tea> teas = teaRepository.getTeasOrderByAlphabetic(true);

        assertThat(teas).hasSize(2);
    }

    @Test
    public void getTeasOrderByVariety() {
        when(teaDao.getTeasOrderByVariety()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        final List<Tea> teas = teaRepository.getTeasOrderByVariety(false);

        assertThat(teas).hasSize(2);
    }

    @Test
    public void getFavoriteTeasOrderByVariety() {
        when(teaDao.getFavoriteTeasOrderByVariety()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        final List<Tea> teas = teaRepository.getTeasOrderByVariety(true);

        assertThat(teas).hasSize(2);
    }

    @Test
    public void getTeasOrderByRating() {
        when(teaDao.getTeasOrderByRating()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        final List<Tea> teas = teaRepository.getTeasOrderByRating(false);

        assertThat(teas).hasSize(2);
    }

    @Test
    public void getFavoriteTeasOrderByRating() {
        when(teaDao.getFavoriteTeasOrderByRating()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        final List<Tea> teas = teaRepository.getTeasOrderByRating(true);

        assertThat(teas).hasSize(2);
    }

    @Test
    public void getTeaById() {
        final long teaId = 1;
        final Tea tea = new Tea();
        when(teaDao.getTeaById(teaId)).thenReturn(tea);

        final Tea teaById = teaRepository.getTeaById(teaId);

        assertThat(tea).isEqualTo(teaById);
    }

    @Test
    public void getTeasBySearchString() {
        final String searchString = "search";
        when(teaDao.getTeasBySearchString(searchString)).thenReturn(Arrays.asList(new Tea(), new Tea()));

        final List<Tea> teas = teaRepository.getTeasBySearchString(searchString);

        assertThat(teas).hasSize(2);
    }


}
