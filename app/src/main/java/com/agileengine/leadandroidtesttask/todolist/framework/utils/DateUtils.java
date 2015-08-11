/*
 * Copyright (c) 2015. Dmitriy Manzhosov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.agileengine.leadandroidtesttask.todolist.framework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * User: mda
 * Date: 11.04.13
 * Time: 19:16
 */
public class DateUtils {

    public class Constants {
        public static final String ROUND_DATE_FORMAT = "EEEE, MMMM d";
        public static final String SIMPLE_DATE_FORMAT = "d MMM. yyyy";
        public static final String ROUND_TIME_FORMAT = "h:mm a";
        public static final String ROUND_TIME_DATE_PATTERN = "h:mma EE. MMM. d";
        //public static final String ROUND_MONTH_DATE_TIME_PATTERN = "MMM. d, hh:mma ";
        public static final String ISO_8601_TIMESTAMP_PATTERN = "yyyy-MM-dd'T'hh:mm:ssz";
        public static final String BIRTH_DATE_FORMAT = "y/MM/dd";
        public static final String NOT_AVAILABLE = "--";
    }

    private static SimpleDateFormat iso8601formatter = new SimpleDateFormat(Constants.ISO_8601_TIMESTAMP_PATTERN, Locale.ENGLISH);

    public static Date parseIso8601Timestamp(String timeStamp) {
        if (timeStamp == null) {
            return null;
        }
        timeStamp = timeStamp.replaceFirst("Z", "+00:00");
        Date date = null;
        try {
            date = iso8601formatter.parse(timeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getDateAsString(Date date, String dateFormat) {
        if (date == null) {
            return Constants.NOT_AVAILABLE;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        return simpleDateFormat.format(date);
    }

    public static String formatIso8601Timestamp(String timeStamp, String dateFormat) {
        return getDateAsString(parseIso8601Timestamp(timeStamp), dateFormat);
    }

    public static Date getNearestSaturday() {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);

        int current = calendar.get(Calendar.DAY_OF_WEEK);
        int last = Calendar.DAY_OF_WEEK;
        calendar.add(Calendar.DAY_OF_MONTH, (last - current));

        return calendar.getTime();
    }
}
