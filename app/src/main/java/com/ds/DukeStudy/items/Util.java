package com.ds.DukeStudy.items;

//  Utility fields and methods

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;

public class Util {

    // Fields

    public static final String STUDENT_ROOT = "students";
    public static final String COURSE_ROOT = "courses";
    public static final String GROUP_ROOT = "groups";
    public static final String EVENT_ROOT = "events";
    public static final String POST_ROOT = "posts";
    public static final String COMMENT_ROOT = "post-comments";

    private static final String EMPTY_FIELD = "Required!";
    private static final String INCORRECT_EMAIL = "Email must contain @duke.edu";
    private static final String INCORRECT_PASSWORD = "Password too short! Enter at least 6 characters.";

    // Text fields

    public static Boolean validateString(String input, EditText textField) {
        if (TextUtils.isEmpty(input)) {
            textField.setError(EMPTY_FIELD);
            return false;
        }
        return true;
    }

    public static Boolean validateEmail(String email, EditText textField) {
        if (!validateString(email, textField)) {
            return false;
        } else if (!email.contains("@duke.edu")) {
            textField.setError(INCORRECT_EMAIL);
            return false;
        }
        return true;
    }

    public static Boolean validatePassword(String password, EditText textField) {
        if (!validateString(password, textField)) {
            return false;
        } else if (password.length() < 6) {
            textField.setError(INCORRECT_PASSWORD);
            return false;
        }
        return true;
    }

    public static Boolean validateNumber(String input, EditText textField) {
        if (TextUtils.isEmpty(input)) {
            textField.setError(EMPTY_FIELD);
            return false;
        }
        return true;
    }

    // Database

    public static ArrayList<String> splitPath(String path) {
        return new ArrayList<String>(Arrays.asList(path.split("/")));
    }

    public static String removeFromPath(String path, int index) {
        ArrayList<String> pathList = splitPath(path);
        String newPath = new String();
        if (index >= 0 && index < pathList.size()) {
            pathList.remove(index);
            for (String s : pathList) {
                newPath += ("/" + s);
            }
            return newPath;
        }
        return path;
    }

    // Pictures

    public static Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
}
