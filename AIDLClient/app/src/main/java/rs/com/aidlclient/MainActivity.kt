package rs.com.aidlclient

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import rs.com.aidlserver.IAddAidlInterface

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        try {
            if (num1.text.length >= 0 && num2.text.length >= 0 && isServiceBound) {
                result.text = ""
                result.text = addService?.addNumbers(Integer.parseInt(num1.text.toString()),
                        Integer.parseInt(num2.text.toString())).toString()

            }
        } catch (remoteException: RemoteException) {
            remoteException.printStackTrace()
        }

    }

    val TAG = "MainActivity"
    private var isServiceBound = false;

    private var addService: IAddAidlInterface? = null


    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            Log.d(TAG, "Service Connected")
            addService = IAddAidlInterface.Stub.asInterface(iBinder)
            isServiceBound = true
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            Log.d(TAG, "Service Disconnected")
            isServiceBound = false

        }
    };


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnAdd.setOnClickListener(this)
        initConnection()


    }

    private fun initConnection() {
        if (!isServiceBound) {

            val intent = Intent(IAddAidlInterface::class.java.name)

            /*this is service name*/
            intent.setAction("service.calc")

            /*From 5.0 annonymous intent calls are suspended so replacing with server app's package name*/
            intent.setPackage("rs.com.aidlserver")

            // binding to remote service
            bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }

    override fun onResume() {
        super.onResume()
        if (!isServiceBound) {
            initConnection()
        }
    }
}
