package coolpharaoh.tee.speicher.tea.timer.views.overview;

import static android.os.Looper.getMainLooper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.BLACK_TEA;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.convertStoredVarietyToText;
import static coolpharaoh.tee.speicher.tea.timer.views.overview.RandomChoiceDialog.TAG;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.ShowTea;
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageController;

@RunWith(RobolectricTestRunner.class)
public class RandomChoiceDialogTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    OverviewViewModel overviewViewModel;
    @Mock
    ImageController imageController;

    private RandomChoiceDialog dialogFragment;
    private FragmentManager fragmentManager;

    @Before
    public void setUp() {
        final FragmentActivity activity = Robolectric.buildActivity(FragmentActivity.class).create().start().resume().get();
        fragmentManager = activity.getSupportFragmentManager();
        dialogFragment = new RandomChoiceDialog(overviewViewModel, imageController);
    }

    @Test
    public void showDialogAndExpectFilledViewWithoutImage() {
        final Tea tea = new Tea("TeaName", BLACK_TEA.getCode(), 1.0,
                AmountKind.TEA_SPOON.getText(), 1, 1, CurrentDate.getDate());
        tea.setId(1L);
        when(overviewViewModel.getRandomTeaInStock()).thenReturn(tea);

        dialogFragment.show(fragmentManager, TAG);
        shadowOf(getMainLooper()).idle();

        final AlertDialog dialog = getLatestAlertDialog();
        final TextView textViewTeaName = dialog.findViewById(R.id.text_view_random_choice_dialog_tea_name);
        assertThat(textViewTeaName.getText()).isEqualTo(tea.getName());

        final TextView textViewVariety = dialog.findViewById(R.id.text_view_random_choice_dialog_variety);
        assertThat(textViewVariety.getText()).isEqualTo(convertStoredVarietyToText(tea.getVariety(),
                ApplicationProvider.getApplicationContext()));

        final TextView textViewImage = dialog.findViewById(R.id.text_view_random_choice_dialog_image);
        assertThat(textViewImage)
                .extracting(View::getVisibility, TextView::getText)
                .contains(View.VISIBLE, "T");

        final ImageView imageViewImage = dialog.findViewById(R.id.image_view_random_tea_choice_image);
        assertThat(imageViewImage.getTag()).isNull();

        final TextView textViewNoTea = dialog.findViewById(R.id.text_view_random_choice_no_tea);
        assertThat(textViewNoTea.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void showDialogAndExpectImage() {
        final String uri = "image/uri";
        when(imageController.getImageUriByTeaId(1L)).thenReturn(Uri.parse(uri));

        final Tea tea = new Tea("TeaName", BLACK_TEA.getCode(), 1.0,
                AmountKind.TEA_SPOON.getText(), 1, 1, CurrentDate.getDate());
        tea.setId(1L);
        when(overviewViewModel.getRandomTeaInStock()).thenReturn(tea);

        dialogFragment.show(fragmentManager, TAG);
        shadowOf(getMainLooper()).idle();

        final AlertDialog dialog = getLatestAlertDialog();
        final TextView textViewImage = dialog.findViewById(R.id.text_view_random_choice_dialog_image);
        assertThat(textViewImage.getVisibility()).isEqualTo(View.INVISIBLE);

        final ImageView imageViewImage = dialog.findViewById(R.id.image_view_random_tea_choice_image);
        assertThat(imageViewImage.getTag()).isEqualTo(uri);
    }

    @Test
    public void refreshDialogAndExpectOtherTea() {
        final String teaName1 = "TeaName 1";
        final String teaName2 = "TeaName 2";
        final Tea tea = new Tea();
        tea.setId(1L);
        tea.setName(teaName1);
        when(overviewViewModel.getRandomTeaInStock()).thenReturn(tea);

        dialogFragment.show(fragmentManager, TAG);
        shadowOf(getMainLooper()).idle();

        final AlertDialog dialog = getLatestAlertDialog();
        final TextView textViewTeaName = dialog.findViewById(R.id.text_view_random_choice_dialog_tea_name);
        assertThat(textViewTeaName.getText()).isEqualTo(teaName1);

        tea.setName(teaName2);

        final ImageButton buttonRefresh = dialog.findViewById(R.id.button_random_choice_dialog_refresh);
        buttonRefresh.performClick();

        assertThat(textViewTeaName.getText()).isEqualTo(teaName2);
    }

    @Test
    public void whenNoRandomChoiceIsAvailableShowMessage() {
        dialogFragment.show(fragmentManager, TAG);
        shadowOf(getMainLooper()).idle();

        final AlertDialog dialog = getLatestAlertDialog();
        final TextView textViewNoTea = dialog.findViewById(R.id.text_view_random_choice_no_tea);
        assertThat(textViewNoTea.getVisibility()).isEqualTo(View.VISIBLE);

        final RelativeLayout layoutTeaAvailable = dialog.findViewById(R.id.layout_random_choice_tea_available);
        assertThat(layoutTeaAvailable.getVisibility()).isEqualTo(View.GONE);

        final TextView textViewHint = dialog.findViewById(R.id.text_view_random_choice_hint);
        assertThat(textViewHint.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void chooseRandomTeaAndExpectShowTeaActivity() {
        final Tea tea = new Tea("TeaName", BLACK_TEA.getCode(), 1.0,
                AmountKind.TEA_SPOON.getText(), 1, 1, CurrentDate.getDate());
        tea.setId(1L);
        when(overviewViewModel.getRandomTeaInStock()).thenReturn(tea);

        dialogFragment.show(fragmentManager, TAG);
        shadowOf(getMainLooper()).idle();

        final AlertDialog dialog = getLatestAlertDialog();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        shadowOf(getMainLooper()).idle();

        final Intent expected = new Intent(ApplicationProvider.getApplicationContext(), ShowTea.class);
        final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

        assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        assertThat(actual.getExtras().get("teaId")).isEqualTo((long) (tea.getId()));
    }
}
