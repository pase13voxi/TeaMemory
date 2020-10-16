package coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.pojo;

// duplicates between entities and Json Object are tolerated
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotePOJO {
    private int position;
    private String header;
    private String description;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
