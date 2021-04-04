package coolpharaoh.tee.speicher.tea.timer.core.infusion;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class InfusionRepositoryTest {
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    InfusionDao infusionDao;

    private InfusionRepository infusionRepository;

    @Before
    public void setUp() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getInfusionDao()).thenReturn(infusionDao);

        infusionRepository = new InfusionRepository(null);
    }

    @Test
    public void insertInfusion() {
        Infusion infusion = new Infusion();

        infusionRepository.insertInfusion(infusion);

        verify(infusionDao).insert(infusion);
    }

    @Test
    public void getInfusions() {
        when(infusionDao.getInfusions()).thenReturn(Arrays.asList(new Infusion(), new Infusion()));

        List<Infusion> infusions = infusionRepository.getInfusions();

        verify(infusionDao).getInfusions();
        assertThat(infusions).hasSize(2);
    }

    @Test
    public void getInfusionsByTeaId() {
        long teaId = 2;
        when(infusionDao.getInfusionsByTeaId(teaId)).thenReturn(Arrays.asList(new Infusion(), new Infusion()));

        List<Infusion> infusions = infusionRepository.getInfusionsByTeaId(teaId);

        assertThat(infusions).hasSize(2);
    }

    @Test
    public void deleteInfusionsByTeaId() {
        long teaId = 2;

        infusionRepository.deleteInfusionsByTeaId(teaId);

        verify(infusionDao).deleteInfusionsByTeaId(teaId);
    }
}
