package Database;

import androidx.room.TypeConverter;

import java.lang.reflect.Type;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import Model.Course;

public class DateConverter {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    @TypeConverter
    public static LocalDate toLocalDate(String date){
        return (date == null ? null : LocalDate.parse(date, formatter));
    }

    @TypeConverter
    public static String fromLocalDate(LocalDate date){
        return date == null ? null : date.format(formatter);
    }

}
