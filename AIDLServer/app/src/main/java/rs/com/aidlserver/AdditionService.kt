package rs.com.aidlserver

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException

class AdditionService : Service() {


    override fun onBind(intent: Intent): IBinder {
        return object : IAddAidlInterface.Stub() {
            @Throws(RemoteException::class)
            override fun addNumbers(firstNumber: Int, secondNumber: Int): Int {
                return firstNumber * secondNumber
            }
        }
    }

}

