package coolpharaoh.tee.speicher.tea.timer.models.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.models.daos.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Infusion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "coolpharaoh.tee.speicher.tea.timer.models.*")
public class InfusionRepositoryTest {
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    InfusionDao infusionDao;

    private InfusionRepository infusionRepository;

    @Before
    public void setUp() {
        mockInfusionDao();
        infusionRepository = new InfusionRepository(null);
    }

    private void mockInfusionDao() {
        initMocks(TeaMemoryDatabase.class);
        mockStatic(TeaMemoryDatabase.class);
        when(TeaMemoryDatabase.getDatabaseInstance(any())).thenReturn(teaMemoryDatabase);
        when(teaMemoryDatabase.getInfusionDao()).thenReturn(infusionDao);
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
