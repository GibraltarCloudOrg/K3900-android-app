package com.example.mauiviewcontrol;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ThreadedToasterForAutomatedTesting implements Runnable
{
    Context mContext;
    CharSequence mMessage;
    int mDuration;
    //MainActivity theActivity;
    AutomatedTestingElement mAutomatedTestingElement;

    public ThreadedToasterForAutomatedTesting(/*MainActivity a,*/AutomatedTestingElement automatedTestingElement, Context context, CharSequence message, int duration )
    {
        //theActivity = a;
        mAutomatedTestingElement = automatedTestingElement;
        mContext = context;
        mMessage = message;
        mDuration = duration;
    }

    @Override
    public void run()
    {
        Toast toast = Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT );
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

        Thread t = new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    //Thread.sleep(theDuration);
                    Thread.sleep(mDuration * 1000);
                    //Thread.sleep(theDuration == Toast.LENGTH_SHORT ? 2500 : 4000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                //theActivity.finish();
                //theActivity.executeAutomatedTesting();
                mAutomatedTestingElement.executeAutomatedTesting();
            }

        });
        t.start();
    }
}