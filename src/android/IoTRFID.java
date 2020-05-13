package iot2.rfid.cordova.plugin;

import org.apache.cordova.*;

import android.os.Handler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import com.module.interaction.ModuleConnector;
import com.nativec.tools.ModuleManager;
import com.rfid.RFIDReaderHelper;
import com.rfid.ReaderConnector;
import com.rfid.rxobserver.RXObserver;
import com.rfid.rxobserver.bean.RXInventoryTag;

public class IoTRFID extends CordovaPlugin {
    private static ArrayList<String> activosDiferentes = new ArrayList<String>();
    private ReadTagListener mRTListener;
    private ModuleConnector mConnectorRFID = new ReaderConnector();
    private static RFIDReaderHelper mReader;
    private RXObserver rxObserver = new RXObserver() {

        @Override
        protected void onInventoryTag(RXInventoryTag tag) {
            tag.strEPC = tag.strEPC.replaceAll(" ", "");
            if (!activosDiferentes.contains(tag.strEPC)) {
                //Log.d("TAG", "tags encontrados: " + tag.strEPC);
                activosDiferentes.add(tag.strEPC);
            }
        }

        @Override
        protected void onInventoryTagEnd(RXInventoryTag.RXInventoryTagEnd tagEnd) {
            super.onInventoryTagEnd(tagEnd);
            if (mRTListener != null)
                mRTListener.onReadTags(activosDiferentes);
            activosDiferentes = new ArrayList<String>();
        }
    };

    private static Handler mLoopHandler = new Handler();
    private static Runnable mLoopRunnable = new Runnable() {
        public void run() {
            startScan();
        }
    };

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        if (mConnectorRFID.connectCom("dev/ttyS4", 115200)) {
            try {
                mReader = RFIDReaderHelper.getDefaultHelper();
                mReader.registerObserver(rxObserver);
                ModuleManager.newInstance().setUHFStatus(true);
                ModuleManager.newInstance().setScanStatus(false);
            } catch (Exception ignored) {

            }
        }
    }

    private static void startScan() {
        mReader.realTimeInventory((byte) -1, (byte) 1);
        mLoopHandler.removeCallbacks(mLoopRunnable);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("scanTag")) {
            String message = args.getString(0);
            startScan();
            if (activosDiferentes.size() > 0)
                message = activosDiferentes.get(0);
            this.scanTag(message, callbackContext);
            return true;
        }

        return false;
    }
    private void scanTag(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("");
        }
    }
}
