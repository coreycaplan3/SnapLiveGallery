package caplaninnovations.com.livesnapgallery.utilities;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Corey Caplan on 5/4/17.
 * Project: SnapLiveGallery
 * <p></p>
 * Purpose of Class:
 */

public final class DateUtility {

    private DateUtility() {
    }

    public static String getDateForUi(long dateAsMillis) {
        return DateFormat.getDateInstance(DateFormat.MEDIUM)
                .format(new Date(dateAsMillis));
    }

}
