package coolpharaoh.tee.speicher.tea.timer.core.infusion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;


@ExtendWith(MockitoExtension.class)
class InfusionRepositoryTest {
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    InfusionDao infusionDao;

    private InfusionRepository infusionRepository;

    @BeforeEach
    void setUp() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getInfusionDao()).thenReturn(infusionDao);

        infusionRepository = new InfusionRepository(null);
    }

    @Test
    void insertInfusion() {
        final Infusion infusion = new Infusion();

        infusionRepository.insertInfusion(infusion);

        verify(infusionDao).insert(infusion);
    }

    @Test
    void getInfusions() {
        when(infusionDao.getInfusions()).thenReturn(Arrays.asList(new Infusion(), new Infusion()));

        final List<Infusion> infusions = infusionRepository.getInfusions();

        verify(infusionDao).getInfusions();
        assertThat(infusions).hasSize(2);
    }

    @Test
    void getInfusionsByTeaId() {
        final long teaId = 2;
        when(infusionDao.getInfusionsByTeaId(teaId)).thenReturn(Arrays.asList(new Infusion(), new Infusion()));

        final List<Infusion> infusions = infusionRepository.getInfusionsByTeaId(teaId);

        assertThat(infusions).hasSize(2);
    }

    @Test
    void deleteInfusionsByTeaId() {
        final long teaId = 2;

        infusionRepository.deleteInfusionsByTeaId(teaId);

        verify(infusionDao).deleteInfusionsByTeaId(teaId);
    }
}
