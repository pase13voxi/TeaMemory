package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RecyclerItemOverview {
    final String category;
    final Long teaId;
    final String teaName;
    final String variety;
    boolean favorite;
}
