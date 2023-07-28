package com.example.medbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.text.ParseException;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String phone,message,disease="",name="";
    int cnt=0;
    EditText na,ph,di;
    TextView t;
    Spinner s;
    VideoView v;
    String selectedDate,cur_date;
    boolean f=true;
        Button b,date;
    String time;
    String[] timings = {"10 A.M - 11 A.M","11.30 A.M - 12.30 P.M","2 P.M - 3 P.M","4 P.M - 5 P.M","6 P.M - 7 P.M"};

    static Map<String,String> register = new HashMap<>();

    public void sendMessage()
    {
        try{
            SmsManager sms=SmsManager.getDefault();
            sms.sendTextMessage(phone,null,message,null,null);
            cnt+=1;
            t.setText(String.valueOf(cnt));
            register.put(time,selectedDate);
            na.setText("");ph.setText("");di.setText("");date.setText("Select Date");


            Toast.makeText(getApplicationContext(), "Appointment Booked Successfully :)",Toast.LENGTH_LONG).show();

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Message Not Sent :(",Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        na = findViewById(R.id.editTextTextPersonName);
        di =findViewById(R.id.editTextTextPersonName2);
        t=findViewById(R.id.textView);
        ph = findViewById(R.id.editTextPhone);
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.SEND_SMS},
                    0);
        }

        b = findViewById(R.id.button);



        //Spinner - Timings

                s = findViewById(R.id.spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                         android.R.layout.simple_spinner_item,timings);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s.setAdapter(adapter);

                s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        time = parent.getItemAtPosition(position).toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

         //Button - Date picker

            date=findViewById(R.id.button2);
            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    cur_date = sdf.format(calendar.getTime());



                    DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                    Date c_date = null;
                                    Date s_date = null;
                                    try {
                                        c_date = sdf.parse(cur_date);
                                         s_date = sdf.parse(selectedDate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if(s_date.compareTo(c_date)>0)
                                    {
                                        date.setText(selectedDate);
                                        f=true;
                                    }
                                    else
                                    {
                                        date.setText("Invalid Date");
                                        f=false;
                                    }
                                }
                            }, year, month, day);

                    datePickerDialog.show();
                }
            });



        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean n=false,p=false,d=false;



                //Name
                if(!na.getText().toString().trim().equals("")) {name = na.getText().toString(); n=true;}
                else  {Toast.makeText(MainActivity.this, "Enter name :(",Toast.LENGTH_SHORT).show(); n=false;}

                //Phone
                if(ph.getText().toString().length()==10) {phone = ph.getText().toString(); p=true;}
                else {
                    p = false;
                    Toast.makeText(MainActivity.this, "Enter phone number :(", Toast.LENGTH_SHORT).show();
                }
                //Disease
                if(!di.getText().toString().trim().equals("")){ disease = di.getText().toString(); d=true;}
                else { Toast.makeText(MainActivity.this, "Enter Disease :(",Toast.LENGTH_SHORT).show();
                    d=false;}

                message="--- MEPCO CLINIC ---\n\n";
                message+="Appoinment Confirmation\n\n";
                message+="Name: "+name+"\n";
                message+="Phone Number: "+phone+"\n";
                message+="Date: "+selectedDate+"\n";
                message+="Timing: "+time+"\n";
                message+="Disease: "+disease+"\n";
                message+="\nTHANK YOU :) \n";
                for(Map.Entry<String,String> entry: register.entrySet())
                {
                    if(selectedDate.equals(entry.getValue())&&time.equals(entry.getKey()))
                    {
                        f=false;
                        Toast.makeText(getApplicationContext(),"Already Booked :)",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                       f=true;
                    }
                }

              if(p&&f&&d&&n)  sendMessage();

            }
        });


    }
}
