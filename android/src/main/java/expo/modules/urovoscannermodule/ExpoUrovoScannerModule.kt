package expo.modules.urovoscannermodule

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

// Import Urovo SDK classes (Java classes) in Kotlin
import android.device.ScanManager

// Import Android framework classes
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log

class ExpoUrovoScannerModule : Module() {

  private var mReceiver: BroadcastReceiver? = null
  private var mScanManager: ScanManager? = null
  // Each module class must implement the definition function. The definition consists of components
  // that describes the module's functionality and behavior.
  // See https://docs.expo.dev/modules/module-api for more details about available components.
  override fun definition() = ModuleDefinition {
    // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
    // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
    // The module will be accessible from `requireNativeModule('ExpoUrovoScannerModule')` in JavaScript.
    Name("ExpoUrovoScannerModule")

    // Sets constant properties on the module. Can take a dictionary or a closure that returns a dictionary.
    Constants(
      "PI" to Math.PI
    )

    // Defines event names that the module can send to JavaScript.
    Events("onChange")

    // Defines a JavaScript synchronous function that runs the native code on the JavaScript thread.
    Function("hello") {
      "Hello world! 👋"
    }

    Function("scanner") {
      mScanManager = ScanManager()
      var powerOn = mScanManager?.scannerState

      mScanManager?.switchOutputMode(0) // DECODE_OUTPUT_MODE_INTENT = 0

      registerReceiver()
    }

    Function("scan") {
      mScanManager?.startDecode()
    }

    // Defines a JavaScript function that always returns a Promise and whose native code
    // is by default dispatched on the different thread than the JavaScript runtime runs on.
    AsyncFunction("setValueAsync") { value: String ->
      // Send an event to JavaScript.
      sendEvent("onChange", mapOf(
        "value" to value
      ))
    }

    OnDestroy {
      unregisterReceiver()
    }
  }

  private val BARCODE_TYPE_TAG: String = ScanManager.BARCODE_TYPE_TAG
  private val BARCODE_LENGTH_TAG: String = ScanManager.BARCODE_LENGTH_TAG
  private val DECODE_DATA_TAG: String = ScanManager.DECODE_DATA_TAG
  private val BARCODE_STRING_TAG: String = ScanManager.BARCODE_STRING_TAG

  // Define a shortcut to appContext.reactContext with null-checking
  private val context
    get() = requireNotNull(appContext.reactContext)

   private fun registerReceiver() {
    if (mReceiver == null) {
      mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
          Log.d("onRECEIVE", "onReceive")

          val barcode: ByteArray? = intent.getByteArrayExtra(DECODE_DATA_TAG)
          val barcodeLen: Int = intent.getIntExtra(BARCODE_LENGTH_TAG, 0)
          val temp: Byte = intent.getByteExtra(BARCODE_TYPE_TAG, 0)
          val barcodeStr: String? = intent.getStringExtra(BARCODE_STRING_TAG)

          Log.d("barcode", "barcode type: $temp")
          val scanResult = barcode?.let { String(it, 0, barcodeLen) }
          Log.d("barcode", "scanResult: $scanResult")


          // Send scan result to JavaScript
          scanResult?.let {
            sendEvent("onChange", mapOf("scanResult" to it))
          }
        }
      }
      // Register the receiver with the appropriate IntentFilter
      val filter = IntentFilter()
      filter.addAction("android.intent.ACTION_DECODE_DATA")
      context.registerReceiver(mReceiver, filter)

    }
  }

  private fun unregisterReceiver() {
   mReceiver?.let {
      context.unregisterReceiver(it)
      mReceiver = null
    }
  }
}
