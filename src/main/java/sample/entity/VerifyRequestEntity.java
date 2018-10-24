package sample.entity;

import org.codehaus.jackson.annotate.JsonProperty;

public class VerifyRequestEntity {

    @JsonProperty("faceId1")
    private String faceIdDownload;
    @JsonProperty("faceId2")
    private String faceIdSnapshot;

    public String getFaceIdDownload() {
        return faceIdDownload;
    }

    public String getFaceIdSnapshot() {
        return faceIdSnapshot;
    }

    public void setFaceIdDownload(String faceIdDownload) {
        this.faceIdDownload = faceIdDownload;
    }

    public void setFaceIdSnapshot(String faceIdSnapshot) {
        this.faceIdSnapshot = faceIdSnapshot;
    }
}

