package com.example.mauiviewcontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.List;
import java.util.Vector;

import k3900.K3900;

public class PatientsActivity extends AppCompatActivity {
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);

        /*Vector<String> patients = mBackend.onGetPatients("");
        ListView mainListView = findViewById(R.id.mainListView);
        if (null != patients)
        {
            ArrayAdapter<String> patientAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, patients);
            mainListView.setAdapter(patientAdapter);
        }*/
        SeekBar simpleSeekBar=(SeekBar)findViewById(R.id.simpleSeekBar);
        // perform seek bar change listener event used for getting the progress value
        simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            float progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = (float)progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(PatientsActivity.this, "Seek bar progress is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
                //K3900.BeamformerParametersResponse response = mBackend.onAcousticPowerChanged((float) 0.2);
                mBackend.onAcousticPowerChanged(progressChangedValue);
            }
        });
    }

    public void requestStartExam(View view) {
        boolean result = mBackend.onStartExam("ExamNameTest1", "CommentsTest1", "Pid1", "FirstName1", "LastName1", "09/31/2030", "male");
    }

    public void requestStopExam(View view) {
        //K3900.ResponseMsg responseMsg = mBackend.onEndExam();
    }
}
