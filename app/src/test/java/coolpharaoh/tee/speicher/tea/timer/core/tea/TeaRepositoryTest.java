package coolpharaoh.tee.speicher.tea.timer.core.tea;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;

@ExtendWith(MockitoExtension.class)
class TeaRepositoryTest {
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    TeaDao teaDao;

    private TeaRepository teaRepository;

    @BeforeEach
    void setUp() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getTeaDao()).thenReturn(teaDao);

        teaRepository = new TeaRepository(new Application());
    }

    @Test
    void insertTea() {
        final Tea tea = new Tea();

        teaRepository.insertTea(tea);

        verify(teaDao).insert(tea);
    }

    @Test
    void updateTea() {
        final Tea tea = new Tea();

        teaRepository.updateTea(tea);

        verify(teaDao).update(tea);
    }

    @Test
    void deleteTeaById() {
        final long teaId = 1;

        teaRepository.deleteTeaById(teaId);

        verify(teaDao).deleteTeaById(teaId);
    }

    @Test
    void deleteAllTeas() {
        teaRepository.deleteAllTeas();

        verify(teaDao).deleteAll();
    }

    @Test
    void getTeas() {
        when(teaDao.getTeas()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        final List<Tea> teas = teaRepository.getTeas();

        assertThat(teas).hasSize(2);
    }

    @Test
    void getTeasOrderByActivity() {
        when(teaDao.getTeasOrderByActivity()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        final List<Tea> teas = teaRepository.getTeasOrderByActivity(false);

        assertThat(teas).hasSize(2);
    }

    @Test
    void getTeasInStockOrderByActivity() {
        when(teaDao.getTeasInStockOrderByActivity()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        final List<Tea> teas = teaRepository.getTeasOrderByActivity(true);

        assertThat(teas).hasSize(2);
    }

    @Test
    void getTeasOrderByAlphabetic() {
        when(teaDao.getTeasOrderByAlphabetic()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        final List<Tea> teas = teaRepository.getTeasOrderByAlphabetic(false);

        assertThat(teas).hasSize(2);
    }

    @Test
    void getTeasInStockOrderByAlphabetic() {
        when(teaDao.getTeasInStockOrderByAlphabetic()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        final List<Tea> teas = teaRepository.getTeasOrderByAlphabetic(true);

        assertThat(teas).hasSize(2);
    }

    @Test
    void getTeasOrderByVariety() {
        when(teaDao.getTeasOrderByVariety()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        final List<Tea> teas = teaRepository.getTeasOrderByVariety(false);

        assertThat(teas).hasSize(2);
    }

    @Test
    void getTeasStockOrderByVariety() {
        when(teaDao.getTeasInStockOrderByVariety()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        final List<Tea> teas = teaRepository.getTeasOrderByVariety(true);

        assertThat(teas).hasSize(2);
    }

    @Test
    void getTeasOrderByRating() {
        when(teaDao.getTeasOrderByRating()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        final List<Tea> teas = teaRepository.getTeasOrderByRating(false);

        assertThat(teas).hasSize(2);
    }

    @Test
    void getTeasInStockOrderByRating() {
        when(teaDao.getTeasInStockOrderByRating()).thenReturn(Arrays.asList(new Tea(), new Tea()));

        final List<Tea> teas = teaRepository.getTeasOrderByRating(true);

        assertThat(teas).hasSize(2);
    }

    @Test
    void getTeaById() {
        final long teaId = 1;
        final Tea tea = new Tea();
        when(teaDao.getTeaById(teaId)).thenReturn(tea);

        final Tea teaById = teaRepository.getTeaById(teaId);

        assertThat(teaById).isEqualTo(tea);
    }

    @Test
    void getRandomTeaInStock() {
        final Tea tea = new Tea();
        when(teaDao.getRandomTeaInStock()).thenReturn(tea);

        final Tea randomTea = teaRepository.getRandomTeaInStock();

        assertThat(randomTea).isEqualTo(tea);
    }

    @Test
    void getTeasBySearchString() {
        final String searchString = "search";
        when(teaDao.getTeasBySearchString(searchString)).thenReturn(Arrays.asList(new Tea(), new Tea()));

        final List<Tea> teas = teaRepository.getTeasBySearchString(searchString);

        assertThat(teas).hasSize(2);
    }
}
