public class NonSingleAnnotationException extends Exception {
    String message;

    public NonSingleAnnotationException(String message) {
        this.message = message;
    }
}
