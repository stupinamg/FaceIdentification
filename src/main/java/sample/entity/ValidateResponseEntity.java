package sample.entity;

public class ValidateResponseEntity {

    private String confidence;

    private Boolean isIdentical;

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public boolean isIdentical() {
        return isIdentical;
    }

    public void setIsIdentical(boolean identical) {
        isIdentical = identical;
    }
}
