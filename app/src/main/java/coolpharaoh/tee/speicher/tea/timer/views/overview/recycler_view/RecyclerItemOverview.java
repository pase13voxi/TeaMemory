package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RecyclerItemOverview {
    final String category;
    final Long teaId;
    final String teaName;
    final String variety;
    final boolean favorite;
}
