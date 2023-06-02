import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Test {
    public static void main(String[] args) {
        String pattern = "YYYY-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date datum;
        datum = Date.from((LocalDate.now()).atStartOfDay(ZoneId.systemDefault()).plusMonths(1).toInstant());
        String query = "UPDATE `clanovi` SET `datum_isteka`= '" + dateFormat.format(datum) +"' ,`datum_izmene`= CURRENT_TIMESTAMP WHERE `idclanovi` = " + 1;

        System.out.println(query);
    }
}
