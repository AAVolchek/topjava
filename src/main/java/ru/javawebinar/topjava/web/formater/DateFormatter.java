package ru.javawebinar.topjava.web.formater;

import org.springframework.format.Formatter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateFormatter implements Formatter<LocalDate> {
    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return DateTimeUtil.parseLocalDate(text);
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        //return object.format(DateTimeFormatter.ISO_LOCAL_DATE);
        return object.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
