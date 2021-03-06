package com.charlieliu.itsch.dailyjournal;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;

public class PreviewNotesAsyncTask extends AsyncTask <Void, Void, ArrayList<Entry>>
{
    private WeakReference<Activity> mActivity;
    private CalendarDay mDay;
    private boolean printAll = false;

    private final String TAG = "PreviewNotesAsyncTask";

    PreviewNotesAsyncTask(Activity activity)
    {
        mActivity = new WeakReference<>(activity);
        mDay = null;
        printAll = true;
    }

    PreviewNotesAsyncTask(Activity activity, CalendarDay day)
    {
        mActivity = new WeakReference<>(activity);
        mDay = day;
    }



    @Override
    protected void onPreExecute()
    {

    }


    @Override
    protected ArrayList<Entry> doInBackground(Void ... voids) {

        ArrayList<Entry> entries = new ArrayList<>();
        for (Entry e : Entry.EntriesList)
        {
            if (printAll)
            {
                entries.add(e);
            }
            else if (e.getDate().get(Calendar.MONTH) == mDay.getCalendar().get(Calendar.MONTH)
                    && e.getDate().get(Calendar.DAY_OF_MONTH) == mDay.getCalendar().get(Calendar.DAY_OF_MONTH)
                    && e.getDate().get(Calendar.YEAR) == mDay.getCalendar().get(Calendar.YEAR))
            {
                entries.add(e);
                Log.d(TAG, "Note matched date");
            }
        }


        return entries;
    }

    @Override
    protected void onPostExecute(ArrayList<Entry> param)
    {
        TableLayout tableLayout = mActivity.get().findViewById(R.id.NotesPreview);
        tableLayout.removeAllViews();



        if (param.size() == 0)
        {
            TextView note = new TextView(mActivity.get().getBaseContext());
            note.setText("No Entries");
            tableLayout.addView(note);
        }

        for (int i = 0; i < param.size(); ++i)
        {
            final Entry e = param.get(i);

            TextView numStars = new TextView(mActivity.get().getBaseContext());
            ImageView star = new ImageView(mActivity.get().getBaseContext());
            TextView note = new TextView(mActivity.get().getBaseContext());

            final float f = e.getRating();
            final String str = e.getNotes();
            final int idx = i;
            final Calendar cal = e.getDate();

            numStars.setText(Float.toString(f));
            numStars.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
            star.setImageResource(R.drawable.ic_star);
//            star.setLayoutParams(new TableRow.LayoutParams(50, 50));

            note.setText(str);
            note.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            note.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
//            note.setSingleLine(false);
//            note.setHorizontallyScrolling(false);
//            note.setMinLines(2);

            if (!SettingsActivity.themeIsLight)
            {
                numStars.setTextColor(ContextCompat.getColor(mActivity.get(), R.color.white));
                star.setColorFilter(ContextCompat.getColor(mActivity.get(), R.color.white));
                note.setTextColor(ContextCompat.getColor(mActivity.get(), R.color.white));
            }


            TableRow tr = new TableRow(mActivity.get().getBaseContext());
            tr.addView(numStars);
            tr.addView(star);
            tr.addView(note);

            tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d(TAG, "onClick");

                    Intent intent = new Intent(mActivity.get().getApplicationContext(), AddEntryActivity.class);
                    intent.putExtra("rating", f);
                    intent.putExtra("notes", str);
                    intent.putExtra("idx", Entry.EntriesList.indexOf(e));
                    intent.putExtra("cal", cal);

                    mActivity.get().startActivity(intent);

                }
            });

            tableLayout.addView(tr);

        }

    }
}
