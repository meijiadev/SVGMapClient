package ddr.example.com.svgmapclient.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.DialogInterface
import android.os.Environment
import android.util.TypedValue
import android.view.View
import android.widget.TextView

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import cat.ereza.customactivityoncrash.config.CaocConfig
import ddr.example.com.svgmapclient.R
import ddr.example.com.svgmapclient.common.DDRActivity
import ddr.example.com.svgmapclient.common.GlobalParameter
import ddr.example.com.svgmapclient.other.Logger

/**
 * time   : 2019/10/27
 * desc   : 崩溃捕捉界面
 */
class CrashActivity : DDRActivity() {

    private var mConfig: CaocConfig? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss") //用于格式化日期，作为日志文件名的一部分

    override fun getLayoutId(): Int {
        return R.layout.activity_crash
    }

    override fun initView() {

    }

    override fun initData() {
        mConfig = CustomActivityOnCrash.getConfigFromIntent(intent)
        if (mConfig == null) {
            // 这种情况永远不会发生，只要完成该活动就可以避免递归崩溃。
            finish()
        }
        saveCrashInfo2File(CustomActivityOnCrash.getAllErrorDetailsFromIntent(this@CrashActivity, intent))
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.btn_crash_restart -> CustomActivityOnCrash.restartApplication(this@CrashActivity, mConfig!!)
            R.id.btn_crash_log -> {
                val dialog = AlertDialog.Builder(this@CrashActivity)
                        .setTitle(R.string.crash_error_details)
                        .setMessage(CustomActivityOnCrash.getAllErrorDetailsFromIntent(this@CrashActivity, intent))
                        .setPositiveButton(R.string.crash_close, null)
                        .setNeutralButton(R.string.crash_copy_log) { _, _ -> copyErrorToClipboard() }
                        .show()
                val textView = dialog.findViewById<TextView>(android.R.id.message)
                textView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            }
            else -> {
            }
        }
    }

    /**
     * 复制报错信息到剪贴板
     */
    private fun copyErrorToClipboard() {
        val errorInformation = CustomActivityOnCrash.getAllErrorDetailsFromIntent(this@CrashActivity, intent)
        ContextCompat.getSystemService(this, ClipboardManager::class.java)!!.primaryClip = ClipData.newPlainText(getString(R.string.crash_error_info), errorInformation)
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    /**
     * errorMessage
     * @param errorMessage
     * @return
     */
    private fun saveCrashInfo2File(errorMessage: String): String {
        val dir = File(GlobalParameter.ROBOT_FOLDER)
        if (dir.exists()) {
            //  Logger.e("文件夹已存在，无须创建");
        } else {
            Logger.e("创建文件")
            dir.mkdirs()
        }
        val sb = StringBuffer()
        sb.append(errorMessage)
        //存到文件
        val time = dateFormat.format(Date())
        val fileName = "crash-$time.txt"
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            try {
                Logger.e("建立log文件")
                val path = File(GlobalParameter.ROBOT_FOLDER)
                val fos = FileOutputStream(path.toString() + "/" + fileName)
                fos.write(sb.toString().toByteArray())
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return fileName
    }
}