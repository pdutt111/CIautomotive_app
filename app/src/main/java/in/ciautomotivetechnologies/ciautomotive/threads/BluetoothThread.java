package in.ciautomotivetechnologies.ciautomotive.threads;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.github.pires.obd.commands.engine.AbsoluteLoadCommand;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
import com.github.pires.obd.commands.temperature.AmbientAirTemperatureCommand;
import com.github.pires.obd.enums.ObdProtocols;

/**
 * Created by pariskshitdutt on 30/05/16.
 */
public class BluetoothThread implements Runnable {
    BluetoothSocket socket;
    public BluetoothThread(BluetoothSocket socket){
        this.socket=socket;
    }
    @Override
    public void run() {
        try {
            new EchoOffCommand().run(socket.getInputStream(), socket.getOutputStream());
            new LineFeedOffCommand().run(socket.getInputStream(), socket.getOutputStream());
            new TimeoutCommand(125).run(socket.getInputStream(), socket.getOutputStream());
            new SelectProtocolCommand(ObdProtocols.AUTO).run(socket.getInputStream(), socket.getOutputStream());
            new AmbientAirTemperatureCommand().run(socket.getInputStream(), socket.getOutputStream());
            AbsoluteLoadCommand absLoad=new AbsoluteLoadCommand();
            Log.i("data from OBD",absLoad.getFormattedResult());
        } catch (Exception e) {
            // handle errors
        }
    }
}
