package hooks;

import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.Map;

public class Gist {
    private String id;
    private String description;
    private String content;
    private Map<String, Gist> files;
    @SerializedName("public")
    private boolean isPublic;

    public Gist() {
    }

    public Gist(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Gist> getFiles() {
        return files;
    }

    public void setFiles(Map<String, Gist> files) {
        this.files = files;
    }

    public void setFile(String name, Gist file) {
        Map<String, Gist> files = new HashMap<>();
        files.put(name, file);
        setFiles(files);
    }
}
