package APIwiz.SocialMedia.Exeptions;

public class taskserviceexception extends RuntimeException {

    public taskserviceexception(String message) {
        super(message);
    }

    public taskserviceexception(String message, Throwable cause) {
        super(message, cause);
    }

}
