package info.aario.killcamera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private void toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void kill_camera() {
        ExecuteAsRootBase root = new ExecuteAsRootBase();
        String text;
        if (!root.canRunRootCommands()) {
            toast("I cannot get root access. Are you sure this device is rooted?");
            return;
        }

        String output;

        output = root.execute("ps");
        if (output.startsWith("Error")) {
            toast(output);
            return;
        }

        String[] ps_output = output.split(System.getProperty("line.separator"));
        for (String line: ps_output) {
            if (line.contains("camera")) {
                if (line.contains("killcamera")) {
                    continue; //Watch out don't kill yourself!
                }
                String[] columns = line.split("\\s+");
                if (columns.length >= 2) {
                    root.execute("kill "+columns[1]);
                    toast("Killed " + columns[columns.length - 1] + " PID: " + columns[1]);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button button = (Button) findViewById(R.id.kill_switch);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                kill_camera();
            }
        });
    }
}
