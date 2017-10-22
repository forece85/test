package com.ganapathy.cricscorer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.ViewCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Utility {
    public static final int BUFFER_SIZE = 1024;
    public static Bitmap plottedBitmap;

    public static boolean IsSDCardPresent() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static void CheckDirs(String strFile) {
        File file = new File(strFile);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void DeleteFileIfExists(String strFile) {
        File file = new File(strFile);
        if (file.exists()) {
            file.delete();
        }
    }

    public static File getDefaultExternalFilesPath(Activity activity) {
        File path = null;
        try {
            if (VERSION.SDK_INT <= 21) {
                path = Environment.getExternalStoragePublicDirectory("");
            } else {
                path = Environment.getExternalStorageDirectory();
            }
        } catch (Exception e) {
        }
        if (path == null) {
            return activity.getApplicationContext().getFilesDir();
        }
        return path;
    }

    public static void cleanupTempFiles(Activity activity) {
        String strFile = "";
        try {
            if (IsSDCardPresent()) {
                strFile = getDefaultExternalFilesPath(activity).getAbsolutePath() + File.separator + activity.getString(C0252R.string.sdcard_temp);
                CheckDirs(strFile);
                DeleteFileIfExists(strFile + File.separator + "Scorecard.html");
                DeleteFileIfExists(strFile + File.separator + "BallByBallReport.html");
                DeleteFileIfExists(strFile + File.separator + "ClassicScorecard.html");
                DeleteFileIfExists(strFile + File.separator + "BowlStats.html");
                DeleteFileIfExists(strFile + File.separator + "BatStats.html");
                DeleteFileIfExists(strFile + File.separator + "WagonWheel.jpg");
                DeleteFileIfExists(strFile + File.separator + "BallSpot.jpg");
                DeleteFileIfExists(strFile + File.separator + "RPO.jpg");
                DeleteFileIfExists(strFile + File.separator + "RunRate.jpg");
                DeleteFileIfExists(strFile + File.separator + "InningsScore.jpg");
                DeleteFileIfExists(strFile + File.separator + "ClassicScorecard.jpg");
                DeleteFileIfExists(strFile + File.separator + "BatsmenStats.jpg");
                DeleteFileIfExists(strFile + File.separator + "BowlersStats.jpg");
            }
            strFile = activity.getApplicationContext().getFilesDir().getPath();
            DeleteFileIfExists(strFile + File.separator + "Scorecard.html");
            DeleteFileIfExists(strFile + File.separator + "BallByBallReport.html");
            DeleteFileIfExists(strFile + File.separator + "ClassicScorecard.html");
            DeleteFileIfExists(strFile + File.separator + "BowlStats.html");
            DeleteFileIfExists(strFile + File.separator + "BatStats.html");
            DeleteFileIfExists(strFile + File.separator + "WagonWheel.jpg");
            DeleteFileIfExists(strFile + File.separator + "BallSpot.jpg");
            DeleteFileIfExists(strFile + File.separator + "RPO.jpg");
            DeleteFileIfExists(strFile + File.separator + "RunRate.jpg");
            DeleteFileIfExists(strFile + File.separator + "InningsScore.jpg");
            DeleteFileIfExists(strFile + File.separator + "ClassicScorecard.jpg");
            DeleteFileIfExists(strFile + File.separator + "BatsmenStats.jpg");
            DeleteFileIfExists(strFile + File.separator + "BowlersStats.jpg");
        } catch (Exception e) {
            Toast.makeText(activity.getApplicationContext(), e.getMessage(), 0).show();
        }
    }

    public static String CreateJpegFromBitmap(Activity activity, Bitmap bitmap, String filename, boolean hiResImage) {
        BufferedOutputStream bos;
        String strFile = "";
        try {
            FileOutputStream ostream;
            if (IsSDCardPresent()) {
                strFile = getDefaultExternalFilesPath(activity).getAbsolutePath() + File.separator + activity.getString(C0252R.string.sdcard_temp);
                CheckDirs(strFile);
                strFile = strFile + File.separator + filename;
                DeleteFileIfExists(strFile);
                ostream = new FileOutputStream(strFile);
            } else {
                strFile = filename;
                DeleteFileIfExists(activity.getApplication().getFilesDir().getAbsolutePath() + File.separator + strFile);
                ostream = activity.openFileOutput(strFile, 0);
            }
            bos = new BufferedOutputStream(ostream);
            bitmap.compress(CompressFormat.JPEG, hiResImage ? 100 : 75, bos);
            bos.flush();
            if (bos != null) {
                bos.close();
            }
            if (IsSDCardPresent()) {
                return strFile;
            }
            return activity.getApplication().getFilesDir().getAbsolutePath() + File.separator + filename;
        } catch (Exception e) {
            Toast.makeText(activity.getApplicationContext(), e.getMessage(), 0).show();
            return strFile;
        } catch (Throwable th) {
            if (bos != null) {
                bos.close();
            }
        }
    }

    public static String CreateFileFromString(Activity activity, String content, String filename) {
        String strFile = "";
        Writer out;
        try {
            FileOutputStream fos;
            if (IsSDCardPresent()) {
                strFile = getDefaultExternalFilesPath(activity).getAbsolutePath() + File.separator + activity.getString(C0252R.string.sdcard_temp);
                CheckDirs(strFile);
                strFile = strFile + File.separator + filename;
                DeleteFileIfExists(strFile);
                fos = new FileOutputStream(strFile);
            } else {
                strFile = filename;
                fos = activity.openFileOutput(strFile, 0);
            }
            if (fos != null) {
                out = new OutputStreamWriter(fos, "UTF-8");
                out.write(content);
                out.flush();
                if (out != null) {
                    out.close();
                }
            }
            if (IsSDCardPresent()) {
                return strFile;
            }
            return activity.getApplication().getFilesDir().getAbsolutePath() + File.separator + filename;
        } catch (Exception e) {
            Toast.makeText(activity.getApplicationContext(), e.getMessage(), 0).show();
            return strFile;
        } catch (Throwable th) {
            if (out != null) {
                out.close();
            }
        }
    }

    public static String GetMailIds(Context context) {
        DatabaseHandler db = new DatabaseHandler(context);
        dtoConfiguration config = db.getConfiguration();
        db.close();
        return "" + config.getMailIds();
    }

    public static int getTheme(Context context) {
        DatabaseHandler db = new DatabaseHandler(context);
        dtoConfiguration config = db.getConfiguration();
        db.close();
        return config.getThemeId();
    }

    public static boolean isOrientationPortrait(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        if (displaymetrics.widthPixels > displaymetrics.heightPixels) {
            return false;
        }
        return true;
    }

    public static dtoScoreBall getLastScoreBall(Context context, String matchID) {
        DatabaseHandler db = new DatabaseHandler(context);
        dtoScoreBall ball = db.getLastScoreBall(matchID);
        db.close();
        return ball;
    }

    public static boolean ArchiveMatch(Context context, List<String> matchIDs, String storagePath, String fullFileName) throws Exception {
        Throwable th;
        FileOutputStream fileOutputStream = null;
        ArrayList<String> fileNames = new ArrayList();
        FileOutputStream fOut = null;
        for (String matchID : matchIDs) {
            try {
                String rawFileName = storagePath + File.separator + matchID + ".bcs";
                File myFile = new File(rawFileName);
                myFile.createNewFile();
                fileOutputStream = new FileOutputStream(myFile);
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = fOut;
            }
            BufferedOutputStream bos;
            try {
                bos = new BufferedOutputStream(fileOutputStream);
                writeToFile(bos, "<BCS>");
                writeToFile(bos, "<Match ID='" + matchID + "'>");
                writeToFile(bos, getMatchAsXml(context, matchID));
                writeToFile(bos, "</Match>");
                writeToFile(bos, "</BCS>");
                bos.flush();
                if (bos != null) {
                    bos.close();
                }
                fileNames.add(rawFileName);
                fOut = fileOutputStream;
            } catch (Throwable th3) {
                th = th3;
            }
        }
        zip((String[]) fileNames.toArray(new String[fileNames.size()]), fullFileName);
        Iterator it = fileNames.iterator();
        while (it.hasNext()) {
            DeleteFileIfExists((String) it.next());
        }
        fileNames.clear();
        if (fOut != null) {
            fOut.close();
        }
        return true;
        if (fileOutputStream != null) {
            fileOutputStream.close();
        }
        throw th;
    }

    public static boolean ArchiveTeam(Context context, List<String> teamNames, String storagePath, String fullFileName) throws Exception {
        BufferedOutputStream bos;
        Throwable th;
        FileOutputStream fileOutputStream = null;
        ArrayList<String> fileNames = new ArrayList();
        FileOutputStream fOut = null;
        for (String teamName : teamNames) {
            try {
                String rawFileName = storagePath + File.separator + teamName.replace(" ", "~") + ".bts";
                File myFile = new File(rawFileName);
                myFile.createNewFile();
                fileOutputStream = new FileOutputStream(myFile);
                bos = new BufferedOutputStream(fileOutputStream);
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = fOut;
            }
            try {
                writeToFile(bos, "<BCS>");
                writeToFile(bos, "<Team Name='" + teamName + "'>");
                writeToFile(bos, getTeamPlayersAsXml(context, teamName));
                writeToFile(bos, "</Team>");
                writeToFile(bos, "</BCS>");
                bos.flush();
                if (bos != null) {
                    bos.close();
                }
                fileNames.add(rawFileName);
                fOut = fileOutputStream;
            } catch (Throwable th3) {
                th = th3;
            }
        }
        zip((String[]) fileNames.toArray(new String[fileNames.size()]), fullFileName);
        Iterator it = fileNames.iterator();
        while (it.hasNext()) {
            DeleteFileIfExists((String) it.next());
        }
        fileNames.clear();
        if (fOut != null) {
            fOut.close();
        }
        return true;
        if (fileOutputStream != null) {
            fileOutputStream.close();
        }
        throw th;
    }

    private static void writeToFile(BufferedOutputStream bos, String stg) throws IOException {
        bos.write(stg.getBytes());
    }

    private static String getMatchAsXml(Context context, String matchID) {
        DatabaseHandler db = new DatabaseHandler(context);
        String xml = db.getMatchAsXml(matchID);
        db.close();
        return xml;
    }

    private static String getTeamPlayersAsXml(Context context, String teamName) {
        DatabaseHandler db = new DatabaseHandler(context);
        String xml = db.getTeamPlayersAsXml(teamName);
        db.close();
        return xml;
    }

    public static String Tag(String tagName, String value) {
        return "<" + tagName + ">" + TextUtils.htmlEncode(value) + "</" + tagName + ">";
    }

    public static void drawLineWithShade(Paint p, Canvas c, float startX, float startY, float endX, float endY) {
        p.setShader(new LinearGradient(startX, startY, endX, endY, 0, Color.parseColor("#55555555"), TileMode.MIRROR));
        c.drawLine(startX, startY, endX, endY, p);
    }

    public static float sign(Point p1, Point p2, Point p3) {
        return (float) (((p1.x - p3.x) * (p2.y - p3.y)) - ((p2.x - p3.x) * (p1.y - p3.y)));
    }

    public static boolean PointInTriangle(Point pt, Point v1, Point v2, Point v3) {
        boolean b1;
        boolean b2;
        if (sign(pt, v1, v2) < 0.0f) {
            b1 = true;
        } else {
            b1 = false;
        }
        if (sign(pt, v2, v3) < 0.0f) {
            b2 = true;
        } else {
            b2 = false;
        }
        boolean b3;
        if (sign(pt, v3, v1) < 0.0f) {
            b3 = true;
        } else {
            b3 = false;
        }
        if (b1 == b2 && b2 == b3) {
            return true;
        }
        return false;
    }

    public static int getSectionInWagonWheel(Point pt, Canvas c) {
        if (PointInTriangle(pt, new Point(c.getWidth() / 2, c.getHeight() / 2), new Point(0, c.getHeight() / 2), new Point(0, 0))) {
            return 0;
        }
        if (PointInTriangle(pt, new Point(c.getWidth() / 2, c.getHeight() / 2), new Point(1, 0), new Point(c.getWidth() / 2, 0))) {
            return 1;
        }
        if (PointInTriangle(pt, new Point(c.getWidth() / 2, c.getHeight() / 2), new Point((c.getWidth() / 2) + 1, 0), new Point(c.getWidth(), 0))) {
            return 2;
        }
        if (PointInTriangle(pt, new Point(c.getWidth() / 2, c.getHeight() / 2), new Point(c.getWidth(), 1), new Point(c.getWidth(), c.getHeight() / 2))) {
            return 3;
        }
        if (PointInTriangle(pt, new Point(c.getWidth() / 2, c.getHeight() / 2), new Point(c.getWidth(), (c.getHeight() / 2) + 1), new Point(c.getWidth(), c.getHeight()))) {
            return 4;
        }
        if (PointInTriangle(pt, new Point(c.getWidth() / 2, c.getHeight() / 2), new Point(c.getWidth() - 1, c.getHeight()), new Point(c.getWidth() / 2, c.getHeight()))) {
            return 5;
        }
        if (PointInTriangle(pt, new Point(c.getWidth() / 2, c.getHeight() / 2), new Point((c.getWidth() / 2) - 1, c.getHeight()), new Point(0, c.getHeight()))) {
            return 6;
        }
        if (PointInTriangle(pt, new Point(c.getWidth() / 2, c.getHeight() / 2), new Point(0, c.getHeight() - 1), new Point(0, (c.getHeight() / 2) + 1))) {
            return 7;
        }
        return -1;
    }

    public static int getSectionInBallSpot(int pt, Canvas c) {
        float yunit = ((float) c.getHeight()) * 0.04f;
        if (((float) pt) <= 6.0f * yunit) {
            return 0;
        }
        if (((float) pt) <= 8.0f * yunit) {
            return 1;
        }
        if (((double) pt) <= ((double) yunit) * 11.9d) {
            return 2;
        }
        if (((double) pt) <= ((double) yunit) * 16.5d) {
            return 3;
        }
        if (((double) pt) <= ((double) yunit) * 23.7d) {
            return 4;
        }
        if (((float) pt) <= 25.0f * yunit) {
            return 5;
        }
        return -1;
    }

    public static Bitmap PlotWagonWheel(Activity activity, List<dtoWagonWheelDetail> wagonStat, Map<String, Boolean> showHideOptions, String playerName) {
        Options options = new Options();
        if (VERSION.SDK_INT < 21) {
            options.inPurgeable = true;
        }
        Bitmap output = BitmapFactory.decodeResource(activity.getResources(), C0252R.drawable.ic_ground_image, options).copy(Config.RGB_565, true);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint(65);
        paint.setStrokeJoin(Join.ROUND);
        paint.setStrokeCap(Cap.ROUND);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(4.0f);
        int bCenterX = canvas.getWidth() / 2;
        int bCenterY = canvas.getHeight() / 2;
        int total = 0;
        int[] iArr = new int[8];
        iArr = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        for (dtoWagonWheelDetail row : wagonStat) {
            int run = row.getRunsThisBall();
            total += run;
            if (row.getIsWicket() == 1) {
                if (!((Boolean) showHideOptions.get("wickets")).booleanValue()) {
                }
            } else if (run == 0) {
                if (!((Boolean) showHideOptions.get("dots")).booleanValue()) {
                }
            } else if (run == 1) {
                if (!((Boolean) showHideOptions.get("ones")).booleanValue()) {
                }
            } else if (run == 2) {
                if (!((Boolean) showHideOptions.get("twos")).booleanValue()) {
                }
            } else if (run == 3) {
                if (!((Boolean) showHideOptions.get("threes")).booleanValue()) {
                }
            } else if (run == 4) {
                if (!((Boolean) showHideOptions.get("fours")).booleanValue()) {
                }
            } else if (run == 6) {
                if (!((Boolean) showHideOptions.get("sixes")).booleanValue()) {
                }
            } else if (run == 5 || run == 7) {
                if (!((Boolean) showHideOptions.get("rest")).booleanValue()) {
                }
            }
            paint.setColor(getWagonWheelLineColor(row.getRunsThisBall()));
            if (row.getIsWicket() == 1) {
                paint.setColor(-16711936);
            }
            if (!(row.getXCord() == 0 || row.getYCord() == 0)) {
                int perpX = (int) ((((double) canvas.getWidth()) / 960.0d) * ((double) row.getXCord()));
                int perpY = (int) ((((double) canvas.getHeight()) / 960.0d) * ((double) row.getYCord()));
                canvas.drawLine((float) bCenterX, (float) bCenterY, (float) perpX, (float) perpY, paint);
                int section = getSectionInWagonWheel(new Point(perpX, perpY), canvas);
                if (section > -1) {
                    iArr[section] = iArr[section] + row.getRunsThisBall();
                }
            }
        }
        paint.setStrokeWidth(5.0f);
        float xunit = ((float) canvas.getWidth()) * 0.05f;
        float yunit = ((float) canvas.getHeight()) * 0.05f;
        drawLineWithShade(paint, canvas, ((float) (canvas.getWidth() / 2)) - (2.0f * xunit), ((float) (canvas.getHeight() / 2)) - (2.0f * yunit), 0.0f, 0.0f);
        drawLineWithShade(paint, canvas, ((float) (canvas.getWidth() / 2)) + (2.0f * xunit), ((float) (canvas.getHeight() / 2)) + (2.0f * yunit), (float) canvas.getWidth(), (float) canvas.getHeight());
        drawLineWithShade(paint, canvas, (float) (canvas.getWidth() / 2), ((float) (canvas.getHeight() / 2)) - (2.0f * yunit), (float) (canvas.getWidth() / 2), 0.0f);
        drawLineWithShade(paint, canvas, (float) (canvas.getWidth() / 2), ((float) (canvas.getHeight() / 2)) + (2.0f * yunit), (float) (canvas.getWidth() / 2), (float) canvas.getHeight());
        drawLineWithShade(paint, canvas, ((float) (canvas.getWidth() / 2)) + (2.0f * xunit), ((float) (canvas.getHeight() / 2)) - (2.0f * yunit), (float) canvas.getWidth(), 0.0f);
        drawLineWithShade(paint, canvas, ((float) (canvas.getWidth() / 2)) - (2.0f * xunit), ((float) (canvas.getHeight() / 2)) + (2.0f * yunit), 0.0f, (float) canvas.getHeight());
        drawLineWithShade(paint, canvas, ((float) (canvas.getWidth() / 2)) - (2.0f * xunit), (float) (canvas.getHeight() / 2), 0.0f, (float) (canvas.getHeight() / 2));
        drawLineWithShade(paint, canvas, ((float) (canvas.getWidth() / 2)) + (2.0f * xunit), (float) (canvas.getHeight() / 2), (float) canvas.getWidth(), (float) (canvas.getHeight() / 2));
        Paint paint2 = new Paint(65);
        paint2.setStrokeJoin(Join.ROUND);
        paint2.setStrokeCap(Cap.ROUND);
        paint2.setAntiAlias(true);
        paint2.setDither(true);
        paint2.setStyle(Style.FILL);
        paint2.setStrokeWidth(0.0f);
        float densityMultiplier = activity.getApplicationContext().getResources().getDisplayMetrics().density;
        paint2.setTextSize(30.0f * densityMultiplier);
        paint2.setColor(-1);
        paint2.setStyle(Style.STROKE);
        paint2.setStrokeWidth(6.0f);
        paint2.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawText("" + iArr[0], 3.0f * xunit, 8.0f * yunit, paint2);
        paint2.setStyle(Style.FILL);
        paint2.setStrokeWidth(0.0f);
        paint2.setColor(-1);
        canvas.drawText("" + iArr[0], 3.0f * xunit, 8.0f * yunit, paint2);
        paint2.setStyle(Style.STROKE);
        paint2.setStrokeWidth(6.0f);
        paint2.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawText("" + iArr[1], 7.0f * xunit, 4.0f * yunit, paint2);
        paint2.setStyle(Style.FILL);
        paint2.setStrokeWidth(0.0f);
        paint2.setColor(-1);
        canvas.drawText("" + iArr[1], 7.0f * xunit, 4.0f * yunit, paint2);
        paint2.setStyle(Style.STROKE);
        paint2.setStrokeWidth(6.0f);
        paint2.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawText("" + iArr[2], 12.0f * xunit, 4.0f * yunit, paint2);
        paint2.setStyle(Style.FILL);
        paint2.setStrokeWidth(0.0f);
        paint2.setColor(-1);
        canvas.drawText("" + iArr[2], 12.0f * xunit, 4.0f * yunit, paint2);
        paint2.setStyle(Style.STROKE);
        paint2.setStrokeWidth(6.0f);
        paint2.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawText("" + iArr[3], 16.0f * xunit, 8.0f * yunit, paint2);
        paint2.setStyle(Style.FILL);
        paint2.setStrokeWidth(0.0f);
        paint2.setColor(-1);
        canvas.drawText("" + iArr[3], 16.0f * xunit, 8.0f * yunit, paint2);
        paint2.setStyle(Style.STROKE);
        paint2.setStrokeWidth(6.0f);
        paint2.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawText("" + iArr[4], 16.0f * xunit, 13.0f * yunit, paint2);
        paint2.setStyle(Style.FILL);
        paint2.setStrokeWidth(0.0f);
        paint2.setColor(-1);
        canvas.drawText("" + iArr[4], 16.0f * xunit, 13.0f * yunit, paint2);
        paint2.setStyle(Style.STROKE);
        paint2.setStrokeWidth(6.0f);
        paint2.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawText("" + iArr[5], 12.0f * xunit, 18.0f * yunit, paint2);
        paint2.setStyle(Style.FILL);
        paint2.setStrokeWidth(0.0f);
        paint2.setColor(-1);
        canvas.drawText("" + iArr[5], 12.0f * xunit, 18.0f * yunit, paint2);
        paint2.setStyle(Style.STROKE);
        paint2.setStrokeWidth(6.0f);
        paint2.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawText("" + iArr[6], 7.0f * xunit, 18.0f * yunit, paint2);
        paint2.setStyle(Style.FILL);
        paint2.setStrokeWidth(0.0f);
        paint2.setColor(-1);
        canvas.drawText("" + iArr[6], 7.0f * xunit, 18.0f * yunit, paint2);
        paint2.setStyle(Style.STROKE);
        paint2.setStrokeWidth(6.0f);
        paint2.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawText("" + iArr[7], 3.0f * xunit, 13.0f * yunit, paint2);
        paint2.setStyle(Style.FILL);
        paint2.setStrokeWidth(0.0f);
        paint2.setColor(-1);
        canvas.drawText("" + iArr[7], 3.0f * xunit, 13.0f * yunit, paint2);
        String text = playerName + " ( " + total + " Runs )";
        paint = new Paint(1);
        paint.setStyle(Style.FILL);
        paint.setColor(-1);
        paint.setStrokeWidth(4.0f);
        paint.setTextSize(20.0f * densityMultiplier);
        int width = (int) paint.measureText(text);
        Rect bounds = new Rect();
        paint.getTextBounds("|", 0, 1, bounds);
        int letterHeight = bounds.height() + 20;
        int minLength = (canvas.getHeight() > canvas.getWidth() ? canvas.getWidth() : canvas.getHeight()) / 25;
        paint.setColor(getWagonWheelLineColor(1));
        canvas.drawRect(new Rect(5, 5, minLength, letterHeight), paint);
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawText("1", 8.0f, (float) minLength, paint);
        paint.setColor(getWagonWheelLineColor(2));
        canvas.drawRect(new Rect((minLength * 1) + 5, 5, minLength * 2, letterHeight), paint);
        paint.setColor(-1);
        canvas.drawText("2", (float) ((minLength * 1) + 8), (float) minLength, paint);
        paint.setColor(getWagonWheelLineColor(3));
        canvas.drawRect(new Rect((minLength * 2) + 5, 5, minLength * 3, letterHeight), paint);
        paint.setColor(-1);
        canvas.drawText("3", (float) ((minLength * 2) + 8), (float) minLength, paint);
        paint.setColor(getWagonWheelLineColor(4));
        canvas.drawRect(new Rect((minLength * 3) + 5, 5, minLength * 4, letterHeight), paint);
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawText("4", (float) ((minLength * 3) + 8), (float) minLength, paint);
        paint.setColor(getWagonWheelLineColor(6));
        canvas.drawRect(new Rect((minLength * 4) + 5, 5, minLength * 5, letterHeight), paint);
        paint.setColor(-1);
        canvas.drawText("6", (float) ((minLength * 4) + 8), (float) minLength, paint);
        paint.setColor(getWagonWheelLineColor(5));
        canvas.drawRect(new Rect((minLength * 5) + 5, 5, minLength * 6, letterHeight), paint);
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawText("R", (float) ((minLength * 5) + 8), (float) minLength, paint);
        paint.setColor(-16711936);
        canvas.drawRect(new Rect((minLength * 6) + 5, 5, minLength * 7, letterHeight), paint);
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawText("W", (float) ((minLength * 6) + 8), (float) minLength, paint);
        paint.setColor(-12303292);
        canvas.drawRect(new Rect((minLength * 7) + 5, 5, (minLength * 8) + width, letterHeight), paint);
        paint.setColor(-1);
        canvas.drawText(text, (float) ((minLength * 7) + 8), (float) minLength, paint);
        return output;
    }

    public static int getWagonWheelLineColor(int run) {
        switch (run) {
            case 0:
                return -7829368;
            case 1:
                return -1;
            case 2:
                return -65281;
            case 3:
                return -16776961;
            case 4:
                return InputDeviceCompat.SOURCE_ANY;
            case 6:
                return SupportMenu.CATEGORY_MASK;
            default:
                return -16711681;
        }
    }

    public static Bitmap PlotBallSpot(Activity activity, List<dtoBallSpotDetail> ballSpotStat, String playerName) {
        Options options = new Options();
        if (VERSION.SDK_INT < 21) {
            options.inPurgeable = true;
        }
        Bitmap output = BitmapFactory.decodeResource(activity.getResources(), C0252R.drawable.ic_bowl_pitch, options).copy(Config.RGB_565, true);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setStrokeWidth(7.0f);
        int total = 0;
        int[] iArr = new int[6];
        iArr = new int[]{0, 0, 0, 0, 0, 0};
        for (dtoBallSpotDetail row : ballSpotStat) {
            total += row.getRunsThisBall();
            paint.setStyle(Style.FILL_AND_STROKE);
            if (row.getIsWicket() == 1) {
                paint.setColor(SupportMenu.CATEGORY_MASK);
            } else if (row.getIsNoball() == 1) {
                paint.setColor(-16776961);
            } else if (row.getIsWide() == 1) {
                paint.setColor(-65281);
            }
            if (!(row.getBSXCord() == 0 || row.getBSYCord() == 0)) {
                int perpX = (int) (((((double) canvas.getWidth()) / 960.0d) * ((double) row.getBSXCord())) + 6.0d);
                int perpY = (int) ((((double) canvas.getHeight()) / 960.0d) * ((double) row.getBSYCord()));
                paint.setColor(ViewCompat.MEASURED_STATE_MASK);
                canvas.drawCircle((float) perpX, (float) perpY, 13.0f, paint);
                if (row.getIsWicket() == 1) {
                    paint.setColor(SupportMenu.CATEGORY_MASK);
                } else if (row.getIsWide() == 1) {
                    paint.setColor(-65281);
                } else if (row.getIsNoball() == 1) {
                    paint.setColor(-16776961);
                } else {
                    paint.setColor(getBallSpotLineColor(row.getRunsThisBall()));
                }
                canvas.drawCircle((float) perpX, (float) perpY, 12.0f, paint);
                int section = getSectionInBallSpot(perpY, canvas);
                if (section > -1) {
                    iArr[section] = iArr[section] + 1;
                }
            }
        }
        float xunit = ((float) canvas.getWidth()) * 0.05f;
        float yunit = ((float) canvas.getHeight()) * 0.05f;
        Paint paint2 = new Paint(65);
        paint2.setStrokeJoin(Join.ROUND);
        paint2.setStrokeCap(Cap.ROUND);
        paint2.setAntiAlias(true);
        paint2.setDither(true);
        paint2.setStyle(Style.FILL);
        paint2.setStrokeWidth(0.0f);
        float densityMultiplier = activity.getApplicationContext().getResources().getDisplayMetrics().density;
        paint2.setTextSize(30.0f * densityMultiplier);
        paint2.setColor(-1);
        if (((float) (((((iArr[0] + iArr[1]) + iArr[2]) + iArr[3]) + iArr[4]) + iArr[5])) * 1.0f > 0.0f) {
            paint2.setStyle(Style.STROKE);
            paint2.setStrokeWidth(6.0f);
            paint2.setColor(ViewCompat.MEASURED_STATE_MASK);
            canvas.drawText(String.format("%.0f", new Object[]{Float.valueOf((((float) iArr[0]) / totalBalls) * 100.0f)}) + "%", 18.0f * xunit, 2.0f * yunit, paint2);
            paint2.setStyle(Style.FILL);
            paint2.setStrokeWidth(0.0f);
            paint2.setColor(-1);
            canvas.drawText(String.format("%.0f", new Object[]{Float.valueOf((((float) iArr[0]) / totalBalls) * 100.0f)}) + "%", 18.0f * xunit, 2.0f * yunit, paint2);
            paint2.setStyle(Style.STROKE);
            paint2.setStrokeWidth(6.0f);
            paint2.setColor(ViewCompat.MEASURED_STATE_MASK);
            canvas.drawText(String.format("%.0f", new Object[]{Float.valueOf((((float) iArr[1]) / totalBalls) * 100.0f)}) + "%", 18.0f * xunit, 5.5f * yunit, paint2);
            paint2.setStyle(Style.FILL);
            paint2.setStrokeWidth(0.0f);
            paint2.setColor(-1);
            canvas.drawText(String.format("%.0f", new Object[]{Float.valueOf((((float) iArr[1]) / totalBalls) * 100.0f)}) + "%", 18.0f * xunit, 5.5f * yunit, paint2);
            paint2.setStyle(Style.STROKE);
            paint2.setStrokeWidth(6.0f);
            paint2.setColor(ViewCompat.MEASURED_STATE_MASK);
            canvas.drawText(String.format("%.0f", new Object[]{Float.valueOf((((float) iArr[2]) / totalBalls) * 100.0f)}) + "%", 18.0f * xunit, 8.0f * yunit, paint2);
            paint2.setStyle(Style.FILL);
            paint2.setStrokeWidth(0.0f);
            paint2.setColor(-1);
            canvas.drawText(String.format("%.0f", new Object[]{Float.valueOf((((float) iArr[2]) / totalBalls) * 100.0f)}) + "%", 18.0f * xunit, 8.0f * yunit, paint2);
            paint2.setStyle(Style.STROKE);
            paint2.setStrokeWidth(6.0f);
            paint2.setColor(ViewCompat.MEASURED_STATE_MASK);
            canvas.drawText(String.format("%.0f", new Object[]{Float.valueOf((((float) iArr[3]) / totalBalls) * 100.0f)}) + "%", 18.0f * xunit, 11.5f * yunit, paint2);
            paint2.setStyle(Style.FILL);
            paint2.setStrokeWidth(0.0f);
            paint2.setColor(-1);
            canvas.drawText(String.format("%.0f", new Object[]{Float.valueOf((((float) iArr[3]) / totalBalls) * 100.0f)}) + "%", 18.0f * xunit, 11.5f * yunit, paint2);
            paint2.setStyle(Style.STROKE);
            paint2.setStrokeWidth(6.0f);
            paint2.setColor(ViewCompat.MEASURED_STATE_MASK);
            canvas.drawText(String.format("%.0f", new Object[]{Float.valueOf((((float) iArr[4]) / totalBalls) * 100.0f)}) + "%", 18.0f * xunit, 16.0f * yunit, paint2);
            paint2.setStyle(Style.FILL);
            paint2.setStrokeWidth(0.0f);
            paint2.setColor(-1);
            canvas.drawText(String.format("%.0f", new Object[]{Float.valueOf((((float) iArr[4]) / totalBalls) * 100.0f)}) + "%", 18.0f * xunit, 16.0f * yunit, paint2);
            paint2.setStyle(Style.STROKE);
            paint2.setStrokeWidth(6.0f);
            paint2.setColor(ViewCompat.MEASURED_STATE_MASK);
            canvas.drawText(String.format("%.0f", new Object[]{Float.valueOf((((float) iArr[5]) / totalBalls) * 100.0f)}) + "%", 18.0f * xunit, 19.5f * yunit, paint2);
            paint2.setStyle(Style.FILL);
            paint2.setStrokeWidth(0.0f);
            paint2.setColor(-1);
            canvas.drawText(String.format("%.0f", new Object[]{Float.valueOf((((float) iArr[5]) / totalBalls) * 100.0f)}) + "%", 18.0f * xunit, 19.5f * yunit, paint2);
        }
        String text = playerName + " ( " + total + " Runs )";
        paint = new Paint(1);
        paint.setStyle(Style.FILL);
        paint.setColor(-1);
        paint.setStrokeWidth(4.0f);
        float scaledPx = 20.0f * densityMultiplier;
        paint.setTextSize(scaledPx);
        int width = (int) paint.measureText(text);
        Rect bounds = new Rect();
        paint.getTextBounds("|", 0, 1, bounds);
        int letterHeight = bounds.height() + 20;
        int minLength = (canvas.getHeight() > canvas.getWidth() ? canvas.getWidth() : canvas.getHeight()) / 25;
        paint.setColor(0);
        canvas.drawRect(new Rect(5, 5, minLength + width, letterHeight), paint);
        paint.setColor(-1);
        canvas.drawText(text, 8.0f, (float) minLength, paint);
        paint.setColor(SupportMenu.CATEGORY_MASK);
        canvas.drawRect(new Rect(5, (minLength / 2) + (minLength * 1), minLength, (minLength * 1) + letterHeight), paint);
        paint.setColor(-1);
        canvas.drawText("W", 8.0f, (float) ((minLength * 2) + 5), paint);
        paint.setColor(-65281);
        canvas.drawRect(new Rect(5, (minLength / 2) + (minLength * 2), minLength, (minLength * 2) + letterHeight), paint);
        paint.setTextSize(scaledPx / 1.5f);
        paint.setColor(-1);
        canvas.drawText("wd", 8.0f, (float) ((minLength * 3) + 5), paint);
        paint.setColor(-16776961);
        canvas.drawRect(new Rect(5, (minLength / 2) + (minLength * 3), minLength, (minLength * 3) + letterHeight), paint);
        paint.setColor(-1);
        canvas.drawText("nb", 8.0f, (float) ((minLength * 4) + 5), paint);
        paint.setColor(InputDeviceCompat.SOURCE_ANY);
        canvas.drawRect(new Rect(5, (minLength / 2) + (minLength * 4), minLength, (minLength * 4) + letterHeight), paint);
        paint.setTextSize(scaledPx);
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawText("4", 8.0f, (float) ((minLength * 5) + 5), paint);
        paint.setColor(-16711936);
        canvas.drawRect(new Rect(5, (minLength / 2) + (minLength * 5), minLength, (minLength * 5) + letterHeight), paint);
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawText("6", 8.0f, (float) ((minLength * 6) + 5), paint);
        paint.setColor(-1);
        canvas.drawRect(new Rect(5, (minLength / 2) + (minLength * 6), minLength, (minLength * 6) + letterHeight), paint);
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawText("R", 8.0f, (float) ((minLength * 7) + 5), paint);
        paint.setColor(-7829368);
        canvas.drawRect(new Rect(5, (minLength / 2) + (minLength * 7), minLength, (minLength * 7) + letterHeight), paint);
        paint.setColor(-1);
        canvas.drawText("-", 8.0f, (float) ((minLength * 8) + 5), paint);
        return output;
    }

    public static int getBallSpotLineColor(int run) {
        switch (run) {
            case 0:
                return -7829368;
            case 4:
                return InputDeviceCompat.SOURCE_ANY;
            case 6:
                return -16711936;
            default:
                return -1;
        }
    }

    public static int getThemeBackColor(Activity activity) {
        TypedArray array = activity.getTheme().obtainStyledAttributes(new int[]{16842801});
        int result_color = array.getColor(0, 16711935);
        array.recycle();
        return result_color;
    }

    public static int getThemeTextColor(Activity activity) {
        TypedArray array = activity.getTheme().obtainStyledAttributes(new int[]{16842806});
        int result_color = array.getColor(0, 16711935);
        array.recycle();
        return result_color;
    }

    public static int getColorInverse(int color) {
        if (String.format("%06X", new Object[]{Integer.valueOf(ViewCompat.MEASURED_SIZE_MASK & color)}).compareToIgnoreCase("888888") < 1) {
            return Color.parseColor("#FFCC00");
        }
        return Color.parseColor("#0063D4");
    }

    public static String createStyleSheet() {
        return ((((((((((((((((((((((((((((((((((((((((((((("" + "<head>") + "   <meta name='viewport' content='width=device-width'>") + "\t <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>") + "   <style type=\"text/css\">") + "      * { margin:0px;padding:0px; }") + "\t\t.CSSTableGenerator {") + "\t\tmargin:0px;padding:0px;") + "\t\tbackground-color:#000066;") + "\t\twidth:100%;") + "\t\tborder:1px solid #000000;") + "\t\t}") + "      .CSSTableGenerator table{") + "\t\twidth:100%;") + "\t\tmargin:0px;padding:0px;") + "\t\tborder:0px solid #cccccc;") + "\t\t}") + "\t\t.CSSTableGenerator td{") + "\t\tvertical-align:middle;") + "\t\tbackground-color:#ffffff;") + "\t\tborder:0px solid #cccccc;") + "\t\ttext-align:left;") + "\t\tpadding:7px;") + "\t\tfont-size:12px;") + "\t\tfont-family:Arial;") + "\t\tfont-weight:normal;") + "\t\tcolor:#000000;") + "\t\t}") + "\t\t.CSSTableGenerator th{") + "\t\tbackground-color:#cccccc;") + "\t\tborder:0px solid #ffffff;") + "\t\ttext-align:left;") + "\t\tfont-size:14px;") + "\t\tfont-family:Arial;") + "\t\tfont-weight:bold;") + "\t\tcolor:#000000;") + "\t\t}") + "\t\th3 {") + "\t\tborder:0px solid #ffffff;") + "\t\ttext-align:center;") + "\t\tfont-size:18px;") + "\t\tfont-family:Arial;") + "\t\tfont-weight:bold;") + "\t\tcolor:#ffffff;") + "\t\t}") + "   </style>") + "</head>";
    }

    public static String createFooter() {
        return (("" + "<table>") + "\t <tr><th><div width=\"100%\">Created using Best Cricket Scorer by Ganapathy</div></th></tr>") + "</table>";
    }

    public static String toDisplayCase(String s) {
        String ACTIONABLE_DELIMITERS = " '-/";
        StringBuilder sb = new StringBuilder();
        boolean capNext = true;
        for (char c : s.toCharArray()) {
            char c2;
            if (capNext) {
                c2 = Character.toUpperCase(c2);
            } else {
                c2 = Character.toLowerCase(c2);
            }
            sb.append(c2);
            if (" '-/".indexOf(c2) >= 0) {
                capNext = true;
            } else {
                capNext = false;
            }
        }
        return sb.toString();
    }

    public static String trim(String text) {
        return text.replaceAll("\\u00A0", "").trim();
    }

    public static void zip(String[] files, String zipFile) throws IOException {
        Throwable th;
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        BufferedInputStream origin;
        try {
            byte[] data = new byte[1024];
            int i = 0;
            BufferedInputStream origin2 = null;
            while (i < files.length) {
                try {
                    origin = new BufferedInputStream(new FileInputStream(files[i]), 1024);
                    out.putNextEntry(new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1)));
                    while (true) {
                        int count = origin.read(data, 0, 1024);
                        if (count == -1) {
                            break;
                        }
                        out.write(data, 0, count);
                    }
                    if (origin != null) {
                        origin.close();
                    }
                    i++;
                    origin2 = origin;
                } catch (Throwable th2) {
                    th = th2;
                    origin = origin2;
                }
            }
            if (out != null) {
                out.close();
                return;
            }
            return;
        } catch (Throwable th3) {
            th = th3;
        }
        if (out != null) {
            out.close();
        }
        throw th;
    }

    public static void unzip(String zipFile, String location) throws IOException {
        unzip(zipFile, location, "");
    }

    public static void unzip(String zipFile, String location, String extractSpecificFile) throws IOException {
        BufferedOutputStream fout;
        byte[] buffer = new byte[1024];
        ZipInputStream zin;
        try {
            if (!location.endsWith("/")) {
                location = location + "/";
            }
            File f = new File(location);
            if (!f.isDirectory()) {
                f.mkdirs();
            }
            zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile), 1024));
            while (true) {
                ZipEntry ze = zin.getNextEntry();
                if (ze == null) {
                    break;
                }
                File unzipFile = new File(location + ze.getName());
                if (extractSpecificFile == null || extractSpecificFile.trim().equalsIgnoreCase("") || ze.getName().equalsIgnoreCase(extractSpecificFile)) {
                    if (!ze.isDirectory()) {
                        File parentDir = unzipFile.getParentFile();
                        if (!(parentDir == null || parentDir.isDirectory())) {
                            parentDir.mkdirs();
                        }
                        fout = new BufferedOutputStream(new FileOutputStream(unzipFile, false), 1024);
                        while (true) {
                            int size = zin.read(buffer, 0, 1024);
                            if (size == -1) {
                                break;
                            }
                            fout.write(buffer, 0, size);
                        }
                        zin.closeEntry();
                        fout.flush();
                        if (fout != null) {
                            fout.close();
                        }
                    } else if (!unzipFile.isDirectory()) {
                        unzipFile.mkdirs();
                    }
                }
            }
            if (zin != null) {
                zin.close();
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        } catch (Throwable th) {
            if (zin != null) {
                zin.close();
            }
        }
    }

    public static void setLargeTitle(TextView textTitle, Context context) {
        textTitle.setTextAppearance(context, 16973890);
    }

    @TargetApi(23)
    public static void setLargeTitle(TextView textTitle) {
        textTitle.setTextAppearance(16973890);
    }

    public static void setMediumTitle(TextView textTitle, Context context) {
        textTitle.setTextAppearance(context, 16973892);
    }

    public static Spanned fromHtml(String source) {
        if (VERSION.SDK_INT >= 24) {
            return Html.fromHtml(source, 0);
        }
        return Html.fromHtml(source);
    }

    public static String getMemoryDetail() {
        Runtime runtime = Runtime.getRuntime();
        return "Used: " + ((runtime.totalMemory() - runtime.freeMemory()) / 1048576) + ", Heap: " + (runtime.maxMemory() / 1048576);
    }

    public static boolean isBitSet(int number, int pos) {
        return pos == 0 ? (number & 1) == 1 : ((number >> pos) & 1) == 1;
    }

    public static void sendViaImage(Activity activity, View view, int totalHeight, int totalWidth, String fileName, String title) {
        Intent smsIntent = new Intent("android.intent.action.SEND");
        try {
            Bitmap bmp = Bitmap.createBitmap(totalWidth, totalHeight, Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            canvas.drawColor(getThemeBackColor(activity));
            view.draw(canvas);
            String file = "";
            if (bmp != null) {
                try {
                    file = CreateJpegFromBitmap(activity, bmp, fileName, true);
                } catch (Exception e) {
                    Toast.makeText(activity.getApplicationContext(), "Oops! Error writing captured screen!", 0).show();
                    Media.insertImage(activity.getContentResolver(), file, "Best Cricket Scorer", title);
                    smsIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(file)));
                    smsIntent.setType("image/jpeg");
                    activity.startActivity(Intent.createChooser(smsIntent, title));
                } finally {
                    bmp.recycle();
                }
            }
            Media.insertImage(activity.getContentResolver(), file, "Best Cricket Scorer", title);
            smsIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(file)));
            smsIntent.setType("image/jpeg");
            activity.startActivity(Intent.createChooser(smsIntent, title));
        } catch (FileNotFoundException e2) {
            Toast.makeText(activity.getApplicationContext(), "Oops! Error capturing/creating Screen!", 0).show();
        } catch (Exception e3) {
            Toast.makeText(activity.getApplicationContext(), "Oops! Error writing captured screen!", 0).show();
        }
    }

    public static void showNotSupportedAlert(final Activity activity) {
        new Builder(activity).setTitle(C0252R.string.fullVersionRequiredText).setMessage(C0252R.string.notSupportedFreeText).setPositiveButton(C0252R.string.buyText, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://market.android.com/details?id=com.ganapathy.best.cricket.scorer")));
            }
        }).setNegativeButton(C0252R.string.laterText, null).show();
    }
}
